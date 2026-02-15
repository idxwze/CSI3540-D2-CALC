package com.seifeddine.calculator;

public final class InputValidator {

    private InputValidator() {
    }

    public static double parseDouble(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " est requis.");
        }

        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " doit être un nombre valide.");
        }
    }
}
