package com.bank.core.controller;
import com.bank.core.model.Transacao;
import com.bank.core.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transferencias")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Transacao transferir(@RequestBody Transacao transacao) {
        // Aqui entra o API Gateway com rate limiting na frente
        return transactionService.processar(transacao);
    }

    @GetMapping("/health")
    public String health() {
        return "Bank Core API BAI - Running - Oracle Connected";
    }
}