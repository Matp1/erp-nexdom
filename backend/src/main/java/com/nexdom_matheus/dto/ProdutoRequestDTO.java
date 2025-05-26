package com.nexdom_matheus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProdutoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    private Double preco;

    @NotNull(message = "Quantidade em estoque é obrigatória")
    @Positive(message = "Estoque deve ser maior ou igual a zero")
    private Integer quantidadeEstoque;

    @NotBlank(message = "Tipo do produto é obrigatório")
    private String tipoProduto;
}
