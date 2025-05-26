package com.nexdom_matheus.erp_nexdom.model.enums;

public enum TipoProduto {
    ELETRONICO,
    ELETRODOMESTICO,
    MOVEL;

    public static TipoProduto fromString(String value) {
        try {
            return TipoProduto.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Tipo de produto inválido. Valores permitidos: " + java.util.Arrays.toString(TipoProduto.values())
            );
        }
    }
}
