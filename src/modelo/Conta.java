package com.bank.core.model;

public class Conta {
    private Long id;
    private String numeroConta;
    private String nomeCliente;
    private double saldo;
    private String status;

    public Conta() {
        this.status = "ATIVA";
    }

    public Conta(String numeroConta, String nomeCliente, double saldo) {
        this.numeroConta = numeroConta;
        this.nomeCliente = nomeCliente;
        this.saldo = saldo;
        this.status = "ATIVA";
    }

    public boolean temSaldo(double valor) {
        return this.saldo >= valor;
    }

    public void debitar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de debito invalido");
        }
        if (!temSaldo(valor)) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        this.saldo -= valor;
    }

    public void creditar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de credito invalido");
        }
        this.saldo += valor;
    }

    public boolean estaAtiva() {
        return "ATIVA".equals(status);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
