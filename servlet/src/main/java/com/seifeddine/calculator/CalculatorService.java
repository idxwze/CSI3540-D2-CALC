package com.seifeddine.calculator;

public class CalculatorService {

    public double compute(double a, double b, Operation operation) {
        return switch (operation) {
            case ADD -> a + b;
            case SUB -> a - b;
            case MUL -> a * b;
            case DIV -> {
                if (b == 0d) {
                    throw new IllegalArgumentException("Division par zéro impossible.");
                }
                yield a / b;
            }
        };
    }
}
