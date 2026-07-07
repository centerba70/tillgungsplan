package com.coding_challenge.tilgungsplan;

import com.coding_challenge.tilgungsplan.input_validation.ConsistentTilgungsplan;
import com.coding_challenge.tilgungsplan.input_validation.FieldCheck;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;

import java.math.BigDecimal;

@GroupSequence({FieldCheck.class, InputForm.class})
@ConsistentTilgungsplan(groups = Default.class)
public record InputForm(
        @Digits(integer = 10, fraction = 2, groups = FieldCheck.class)
        @DecimalMin(value = "0.0", inclusive = false, groups = FieldCheck.class)
        @NotNull(groups = FieldCheck.class)
        BigDecimal darlehensbetrag,
        @Digits(integer = 5, fraction = 2, groups = FieldCheck.class)
        @DecimalMin(value = "0.0", groups = FieldCheck.class)
        @DecimalMax(value = "100", groups = FieldCheck.class)
        @NotNull(groups = FieldCheck.class)
        BigDecimal zinssatz,
        @Digits(integer = 5, fraction = 2, groups = FieldCheck.class)
        @DecimalMin(value = "0.0", groups = FieldCheck.class)
        @DecimalMax(value = "100", groups = FieldCheck.class)
        @NotNull(groups = FieldCheck.class)
        BigDecimal anfaenglicheTilgung,
        @Positive(groups = FieldCheck.class)
        @NotNull(groups = FieldCheck.class)
        @Max(value = 50, groups = FieldCheck.class)
        Long zinsbindung) {
}