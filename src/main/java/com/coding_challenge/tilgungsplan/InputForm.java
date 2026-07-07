package com.coding_challenge.tilgungsplan;

import com.coding_challenge.tilgungsplan.input_validation.ConsistentTilgungsplan;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

//@ConsistentTilgungsplan
public record InputForm(@Digits(integer = 10, fraction = 2) @DecimalMin(value = "0.0", inclusive = false) @NotNull BigDecimal darlehensbetrag,
                        @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0") @DecimalMax(value = "100") @NotNull BigDecimal zinssatz,
                        @Digits(integer = 5, fraction = 2) @DecimalMin(value = "0.0") @DecimalMax(value = "100") @NotNull BigDecimal anfaenglicheTilgung,
                        @Positive @NotNull @Max(50) Long zinsbindung) {
}