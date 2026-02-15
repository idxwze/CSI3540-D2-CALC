package com.seifeddine.calculator;

public enum Operation {
    ADD("add", "+"),
    SUB("sub", "-"),
    MUL("mul", "×"),
    DIV("div", "÷");

    private final String value;
    private final String symbol;

    Operation(String value, String symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    public String getValue() {
        return value;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Operation fromParam(String value) {
        for (Operation operation : values()) {
            if (operation.value.equals(value)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Opération non valide.");
    }
}
