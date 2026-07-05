package com.coding_challenge.tilgungsplan;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Tilgungsplan(LocalDate datum, BigDecimal restschuld, BigDecimal zinsen, BigDecimal tilgung, BigDecimal rate) {
}
