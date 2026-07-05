package com.coding_challenge.tilgungsplan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class TillgungsplanServiceTest {

    private TilgungsplanService tillgungsplanService;

    @BeforeEach
    void setUp() {
        this.tillgungsplanService = new TilgungsplanService();
    }

    @Test
    void test1() {
        // Given
        InputForm inputForm = new InputForm(new BigDecimal("100000.0"), new BigDecimal("2.12"), new BigDecimal("2.0"), 10);

        double test = 166.66666666666666;
        //double res = TillgungsplanUtil.round(test);
        // When
        tillgungsplanService.calculateTilgung(inputForm);

        // Then

    }
}