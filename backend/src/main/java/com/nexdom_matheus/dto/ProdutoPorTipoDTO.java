package com.nexdom_matheus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class ProdutoPorTipoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("codigo")
    private Long codigo;

    @JsonProperty("name")
    private String name;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("tipoProduto")
    private String tipoProduto;

    @JsonProperty("valorFornecedor")
    private double valorFornecedor;

    @JsonProperty("quantidadeDisponivel")
    private int quantidadeDisponivel;

    @JsonProperty("quantidadeSaida")
    private int quantidadeSaida;

    public ProdutoPorTipoDTO() {
    }

    public ProdutoPorTipoDTO(Long codigo, String name, String descricao, String tipoProduto, double valorFornecedor,
                             int quantidadeDisponivel, int quantidadeSaida) {
        this.codigo = codigo;
        this.name = name;
        this.descricao = descricao;
        this.tipoProduto = tipoProduto;
        this.valorFornecedor = valorFornecedor;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.quantidadeSaida = quantidadeSaida;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public double getValorFornecedor() {
        return valorFornecedor;
    }

    public void setValorFornecedor(double valorFornecedor) {
        this.valorFornecedor = valorFornecedor;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public int getQuantidadeSaida() {
        return quantidadeSaida;
    }

    public void setQuantidadeSaida(int quantidadeSaida) {
        this.quantidadeSaida = quantidadeSaida;
    }
}