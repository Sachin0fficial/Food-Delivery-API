package com.codestorykh.utils;

public final class StringClazzUtils {
    private StringClazzUtils() {}

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String sanitize(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("[<>\"'&]", "");
    }

    public static String generateSlug(String input) {
        if (input == null) return null;
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }
}
