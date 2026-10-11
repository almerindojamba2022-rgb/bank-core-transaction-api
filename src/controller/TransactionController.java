package com.bank.core.controller;

import com.bank.core.model.Transacao;
import com.bank.core.service.TransactionService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/transferencias/health", this::health);
        server.createContext("/api/transferencias", this::transferir);
        server.start();
        System.out.println("Bank Core API a correr na porta " + port);
    }

    // GET /api/transferencias/health
    private void health(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) {
            send(ex, 405, "{\"erro\":\"Metodo nao permitido\"}");
            return;
        }
        send(ex, 200, "{\"status\":\"Bank Core API BAI - Running - Oracle Connected\"}");
    }

    // POST /api/transferencias
    private void transferir(HttpExchange ex) throws IOException {
        // Aqui entra o API Gateway com rate limiting na frente
        if (!ex.getRequestURI().getPath().equals("/api/transferencias")) {
            send(ex, 404, "{\"erro\":\"Rota nao encontrada\"}");
            return;
        }
        if (!"POST".equals(ex.getRequestMethod())) {
            send(ex, 405, "{\"erro\":\"Metodo nao permitido\"}");
            return;
        }

        try {
            String body = readBody(ex.getRequestBody());
            String origem = extrairTexto(body, "contaOrigem");
            String destino = extrairTexto(body, "contaDestino");
            Double valor = extrairNumero(body, "valor");

            if (origem == null || destino == null || valor == null) {
                send(ex, 400, "{\"erro\":\"Campos obrigatorios: contaOrigem, contaDestino, valor\"}");
                return;
            }

            Transacao resultado = transactionService.processar(new Transacao(origem, destino, valor));
            int status = "REJEITADA".equals(resultado.getStatus())
                      || "REJEITADA_FRAUDE".equals(resultado.getStatus()) ? 422 : 200;
            send(ex, status, toJson(resultado));

        } catch (Exception e) {
            send(ex, 500, "{\"erro\":\"Erro interno\"}");
        }
    }

    // ---------- utilitários ----------

    private String readBody(InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }

    private String extrairTexto(String json, String campo) {
        Matcher m = Pattern.compile("\"" + campo + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private Double extrairNumero(String json, String campo) {
        Matcher m = Pattern.compile("\"" + campo + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)").matcher(json);
        return m.find() ? Double.valueOf(m.group(1)) : null;
    }

    private String toJson(Transacao t) {
        return "{"
            + "\"contaOrigem\":\"" + esc(t.getContaOrigem()) + "\","
            + "\"contaDestino\":\"" + esc(t.getContaDestino()) + "\","
            + "\"valor\":" + t.getValor() + ","
            + "\"status\":\"" + esc(t.getStatus()) + "\","
            + "\"motivoRejeicao\":" + (t.getMotivoRejeicao() == null
                    ? "null" : "\"" + esc(t.getMotivoRejeicao()) + "\"")
            + "}";
    }

    private String esc(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void send(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}
