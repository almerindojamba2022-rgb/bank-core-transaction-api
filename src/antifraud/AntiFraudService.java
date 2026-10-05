package com.bank.core.antifraud;
import com.bank.core.model.Transacao;
import org.springframework.stereotype.Service;

@Service
public class AntiFraudService {

    private static final Double LIMITE_SUSPEITO = 1000000.00;
    private static final Double LIMITE_DIARIO = 5000000.00;

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