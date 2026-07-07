package com.coding_challenge.tilgungsplan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TilgungsplanServiceTest {

    private TilgungsplanService tillgungsplanService;

    @BeforeEach
    void setUp() {
        this.tillgungsplanService = new TilgungsplanService(new TilgungsplanHelper(), "30.05.2015");
    }

    @Test
    void test_correct_creation_of_tilgungsplan_with_valid_data() {
        // Given
        InputForm inputForm = new InputForm(new BigDecimal("100000.00"), new BigDecimal("2.12"), new BigDecimal("2.00"), 10L);

        // When
        List<Tilgungsplan> tilgungsplanList = tillgungsplanService.calculateTilgung(inputForm);

        // Then
        assertEquals(122, tilgungsplanList.size());

        // Verify first element
        Tilgungsplan firstTilgungsplan = tilgungsplanList.getFirst();
        assertEquals("-100000.00", firstTilgungsplan.restschuld().toString());
        assertEquals("0", firstTilgungsplan.zinsen().toString());
        assertEquals("-100000.00", firstTilgungsplan.tilgung().toString());
        assertEquals("-100000.00", firstTilgungsplan.rate().toString());

        // Verify second element
        Tilgungsplan secondTilgungsplan = tilgungsplanList.get(1);
        assertEquals("-99833.34", secondTilgungsplan.restschuld().toString());
        assertEquals("176.67", secondTilgungsplan.zinsen().toString());
        assertEquals("166.66", secondTilgungsplan.tilgung().toString());
        assertEquals("343.33", secondTilgungsplan.rate().toString());

        // Verify last element
        Tilgungsplan lastTilgungsplan = tilgungsplanList.getLast();
        assertEquals("-77744.14", lastTilgungsplan.restschuld().toString());
        assertEquals("18943.74", lastTilgungsplan.zinsen().toString());
        assertEquals("22255.86", lastTilgungsplan.tilgung().toString());
        assertEquals("41199.60", lastTilgungsplan.rate().toString());
    }
}

// TODO: add some more not working case to test app resiliance?