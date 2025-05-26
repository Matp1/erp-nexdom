package com.nexdom_matheus.dto;

import java.io.Serializable;

public class LucroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private double lucroTotal;
    private int quantidadeTotalSaida;

    public LucroDTO() {
    }

    public LucroDTO(double lucroTotal, int quantidadeTotalSaida) {
        this.lucroTotal = lucroTotal;
        this.quantidadeTotalSaida = quantidadeTotalSaida;
    }

    public double getLucroTotal() {
        return lucroTotal;
    }

    public void setLucroTotal(double lucroTotal) {
        this.lucroTotal = lucroTotal;
    }

    public int getQuantidadeTotalSaida() {
        return quantidadeTotalSaida;
    }

    public void setQuantidadeTotalSaida(int quantidadeTotalSaida) {
        this.quantidadeTotalSaida = quantidadeTotalSaida;
    }
}