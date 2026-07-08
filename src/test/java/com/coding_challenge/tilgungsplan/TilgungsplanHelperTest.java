package com.coding_challenge.tilgungsplan;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TilgungsplanHelperTest {

    private TilgungsplanHelper tilgungsplanHelper = new TilgungsplanHelper();

    @Test
    void testCalculateStartingTilgungsplan() {
        // Given
        LocalDate startDate = LocalDate.now();
        BigDecimal startingRestschuld = new BigDecimal("1000.00");
        BigDecimal startingZinsen = BigDecimal.ZERO;
        Darlehensbetrag darlehensbetrag = Darlehensbetrag.builder().vorMonatBetrag(startingRestschuld).build();

        // When
        Tilgungsplan tilgungsplan = tilgungsplanHelper.calculateStartingTilgungsplan(startDate, darlehensbetrag);

        // Then
        assertNotNull(tilgungsplan);
        assertEquals(startingRestschuld.negate(), tilgungsplan.restschuld());
        assertEquals(startingZinsen, tilgungsplan.zinsen());
        assertEquals(startingRestschuld.negate(), tilgungsplan.rate());
        assertEquals(startingRestschuld.negate(), tilgungsplan.tilgung());
    }

    @Test
    void calculateEndingTilgungsplan() {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(3);
        BigDecimal startingRestschuld = new BigDecimal("1000.00");
        BigDecimal partialZinsen = new BigDecimal("15.00");
        BigDecimal partialTilgung = new BigDecimal("10.00");
        Darlehensbetrag darlehensbetrag = Darlehensbetrag.builder().vorMonatBetrag(startingRestschuld).build();

        // Calculate tilgungsplan per Month
        List<Tilgungsplan> tilgungsplans = new ArrayList<>();
        tilgungsplans.add(tilgungsplanHelper.calculateStartingTilgungsplan(startDate, darlehensbetrag));
        darlehensbetrag.setVorMonatBetrag(startingRestschuld.subtract(partialTilgung));
        tilgungsplans.add(new Tilgungsplan(startDate.plusMonths(1).toString(), darlehensbetrag.getVorMonatBetrag(), partialZinsen, partialTilgung, partialTilgung.add(partialZinsen)));
        darlehensbetrag.setVorMonatBetrag(darlehensbetrag.getVorMonatBetrag().subtract(partialTilgung));
        tilgungsplans.add(new Tilgungsplan(startDate.plusMonths(2).toString(), darlehensbetrag.getVorMonatBetrag(), partialZinsen, partialTilgung, partialTilgung.add(partialZinsen)));
        darlehensbetrag.setVorMonatBetrag(darlehensbetrag.getVorMonatBetrag().subtract(partialTilgung));
        tilgungsplans.add(new Tilgungsplan(startDate.plusMonths(3).toString(), darlehensbetrag.getVorMonatBetrag(), partialZinsen, partialTilgung, partialTilgung.add(partialZinsen)));

        // When
        Tilgungsplan endingPlan = tilgungsplanHelper.calculateEndingTilgungsplan(endDate, darlehensbetrag, tilgungsplans);

        // Then
        assertNotNull(endingPlan);
        assertEquals(30L, endingPlan.tilgung().doubleValue());
        assertEquals(45L, endingPlan.zinsen().doubleValue());
        assertEquals(75L, endingPlan.rate().doubleValue());
        assertEquals(-970L, darlehensbetrag.getVorMonatBetrag().negate().doubleValue());
    }

    @Test
    void calculateFixedRate() {
        // Given
        BigDecimal betrag = BigDecimal.valueOf(100);
        BigDecimal anfaenglichenTilgung = BigDecimal.valueOf(2);
        BigDecimal zinsen = BigDecimal.valueOf(2.5);

        // When
        BigDecimal fixedRate = tilgungsplanHelper.calculateFixedRate(betrag, anfaenglichenTilgung, zinsen);

        // Then
        assertNotNull(fixedRate);
        assertEquals(0.37, fixedRate.doubleValue());
    }

    @Test
    void calculateTilgungPerMonth() {
        // Given
        BigDecimal betrag = BigDecimal.valueOf(100);
        BigDecimal anfaenglichenTilgung = BigDecimal.valueOf(2.5);

        // When
        BigDecimal tilgungPerMonth = tilgungsplanHelper.calculateTilgungPerMonth(betrag, anfaenglichenTilgung);

        // Then
        assertNotNull(tilgungPerMonth);
        assertEquals(0.20, tilgungPerMonth.doubleValue());
    }

    @Test
    void calculateZinsenPerMonth() {
        // Given
        BigDecimal betrag = BigDecimal.valueOf(100);
        BigDecimal zinsen = BigDecimal.valueOf(1.5);

        // When
        BigDecimal zinsenPerMonth = tilgungsplanHelper.calculateZinsenPerMonth(betrag, zinsen);

        // Then
        assertNotNull(zinsenPerMonth);
        assertEquals(0.12, zinsenPerMonth.doubleValue());
    }
}