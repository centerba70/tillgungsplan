package com.coding_challenge.tilgungsplan;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record InputForm(@Digits(integer = 10, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) @NotNull BigDecimal darlehensbetrag,
                        @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) @NotNull BigDecimal zinssatz,
                        @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) @NotNull BigDecimal anfaenglichenTilgung,
                        @Positive @NotNull Long zinsbindung) {
}
// TODO: check again the contraints
// https://www.baeldung.com/javax-bigdecimal-validation