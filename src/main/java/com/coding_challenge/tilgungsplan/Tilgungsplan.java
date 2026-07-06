package com.coding_challenge.tilgungsplan;

import java.math.BigDecimal;

public record Tilgungsplan(String datum, BigDecimal restschuld, BigDecimal zinsen, BigDecimal tilgung, BigDecimal rate) {
}
