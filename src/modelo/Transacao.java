package com.bank.core.model;
import java.time.LocalDateTime;

public class Transacao {
    private Long id;
    private String contaOrigem;
    private String contaDestino;
    private Double valor;
    private String status;
    private LocalDateTime dataHora;
    private String motivoRejeicao;

    public Transacao() {
        this.dataHora = LocalDateTime.now();
        this.status = "PENDENTE";
    }

    public Transacao(String origem, String destino, Double valor) {
        this.contaOrigem = origem;
        this.contaDestino = destino;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
        this.status = "PENDENTE";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContaOrigem() { return contaOrigem; }
    public void setContaOrigem(String contaOrigem) { this.contaOrigem = contaOrigem; }
    public String getContaDestino() { return contaDestino; }
    public void setContaDestino(String contaDestino) { this.contaDestino = contaDestino; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getMotivoRejeicao() { return motivoRejeicao; }
    public void setMotivoRejeicao(String motivoRejeicao) { this.motivoRejeicao = motivoRejeicao; }
}