package com.codestorykh.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateTimeUtils {
    private DateTimeUtils() {}

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static String formatToIso(Date date) {
        if (date == null) return null;
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(ISO_FORMATTER);
    }

    public static Date now() {
        return new Date();
    }

    public static String currentTimestamp() {
        return LocalDateTime.now().format(ISO_FORMATTER);
    }
}
