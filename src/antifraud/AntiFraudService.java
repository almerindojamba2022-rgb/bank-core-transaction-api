package com.bank.core.antifraud;

import com.bank.core.model.Transacao;

public class AntiFraudService {

    private static final double LIMITE_SUSPEITO = 1_000_000.00;
    private static final double LIMITE_DIARIO = 5_000_000.00; // ainda não usado

    public boolean validar(Transacao t) {
        if (t.getValor() <= 0) {
            t.setStatus("REJEITADA");
            t.setMotivoRejeicao("Valor invalido");
            return false;
        }
        if (t.getContaOrigem().equals(t.getContaDestino())) {
            t.setStatus("REJEITADA");
            t.setMotivoRejeicao("Conta origem igual destino");
            return false;
        }
        if (t.getValor() > LIMITE_SUSPEITO) {
            t.setStatus("REJEITADA_FRAUDE");
            t.setMotivoRejeicao("Valor suspeito > 1M KZ - requer aprovacao manual");
            System.out.println("[ANTI-FRAUDE] ALERTA: Transacao suspeita " + t.getValor() + " KZ");
            return false;
        }
        return true;
    }
}
