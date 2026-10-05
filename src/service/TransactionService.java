package com.bank.core.service;
import com.bank.core.antifraud.AntiFraudService;
import com.bank.core.model.Conta;
import com.bank.core.model.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private AntiFraudService antiFraudService;

    // Simulando Oracle DB com Map (para teste)
    private Map<String, Conta> contasDB = new HashMap<>();

    public TransactionService() {
        contasDB.put("123456", new Conta("123456", "Almerindo Jamba", 2000000.00));
        contasDB.put("654321", new Conta("654321", "Cliente BAI", 500000.00));
    }

    public Transacao processar(Transacao transacao) {
        // Passo 1: Valida antes de tocar no Core/Oracle (economiza recursos)
        if (!antiFraudService.validar(transacao)) {
            return transacao;
        }

        // Passo 2: Valida saldo no Core
        Conta origem = contasDB.get(transacao.getContaOrigem());
        Conta destino = contasDB.get(transacao.getContaDestino());

        if (origem == null || destino == null) {
            transacao.setStatus("REJEITADA");
            transacao.setMotivoRejeicao("Conta nao existe no Core");
            return transacao;
        }

        if (!origem.temSaldo(transacao.getValor())) {
            transacao.setStatus("REJEITADA");
            transacao.setMotivoRejeicao("Saldo insuficiente");
            return transacao;
        }

        // Passo 3: Processa no Core
        origem.debitar(transacao.getValor());
        destino.creditar(transacao.getValor());
        transacao.setStatus("APROVADA");
        
        System.out.println("[CORE BAI] Transferencia aprovada: " + transacao.getValor() + " KZ");
        return transacao;
    }
}