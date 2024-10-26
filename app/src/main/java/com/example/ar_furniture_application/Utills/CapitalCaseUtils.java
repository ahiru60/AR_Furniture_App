package com.example.ar_furniture_application.Utills;

public class CapitalCaseUtils {

    public static String toCapitalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split("\\s+");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                // Capitalize first letter, and append the rest in lowercase
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return capitalized.toString().trim(); // Remove the trailing space
    }
}

