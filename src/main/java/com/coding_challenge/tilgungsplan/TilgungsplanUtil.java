package com.coding_challenge.tilgungsplan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TilgungsplanUtil {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static BigDecimal round_big_dec(BigDecimal value, RoundingMode roundingMode) {
        value = value.setScale(2, roundingMode);
        return value;
    }

    public static String formatDate(LocalDate localDate) {
        return localDate.format(PATTERN);
    }

}
