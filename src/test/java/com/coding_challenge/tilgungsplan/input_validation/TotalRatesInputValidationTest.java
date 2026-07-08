package com.coding_challenge.tilgungsplan.input_validation;

import com.coding_challenge.tilgungsplan.InputForm;
import com.coding_challenge.tilgungsplan.TilgungsplanHelper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TotalRatesInputValidationTest {

    private final TilgungsplanHelper tilgungsplanHelper = new TilgungsplanHelper();
    private final TotalRatesInputValidation totalRatesInputValidation = new TotalRatesInputValidation(tilgungsplanHelper);

    @Test
    void test_valid_input_forms_should_be_accepted() {
        // Given
        InputForm inputForm = new InputForm(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2.5), BigDecimal.valueOf(2), 10L);

        // When
        boolean isValid = totalRatesInputValidation.validate(inputForm);

        // Then
        assertTrue(isValid);
    }

    @Test
    void test_invalid_input_forms_should_be_refused() {
        // Given
        InputForm inputForm = new InputForm(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2.5), BigDecimal.valueOf(2), 50L);

        // When
        boolean isValid = totalRatesInputValidation.validate(inputForm);

        // Then
        assertFalse(isValid);
    }

}