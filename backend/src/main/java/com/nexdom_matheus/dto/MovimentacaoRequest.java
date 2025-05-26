package com.nexdom_matheus.dto;

public class MovimentacaoRequest {
    private int quantidade;
    private double valorVenda;

    // Construtores
    public MovimentacaoRequest() {
    }

    public MovimentacaoRequest(int quantidade, double valorVenda) {
        this.quantidade = quantidade;
        this.valorVenda = valorVenda;
    }

    // Getters e setters
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }
}