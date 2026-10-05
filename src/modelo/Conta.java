package com.bank.core.model;

public class Conta {
    private Long id;
    private String numeroConta;
    private String nomeCliente;
    private Double saldo;
    private String status;

    public Conta() {}

    public Conta(String numeroConta, String nomeCliente, Double saldo) {
        this.numeroConta = numeroConta;
        this.nomeCliente = nomeCliente;
        this.saldo = saldo;
        this.status = "ATIVA";
    }

    public boolean temSaldo(Double valor) {
        return this.saldo >= valor;
    }

    public void debitar(Double valor) {
        this.saldo -= valor;
    }

    public void creditar(Double valor) {
        this.saldo += valor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public Double getSaldo() { return saldo; }
    public void setSaldo(Double saldo) { this.saldo = saldo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}