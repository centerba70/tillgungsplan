package com.coding_challenge.tilgungsplan;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static com.coding_challenge.tilgungsplan.TilgungsplanUtil.formatDate;
import static com.coding_challenge.tilgungsplan.TilgungsplanUtil.roundValue;

@Service
public class TilgungsplanHelper {

    public Tilgungsplan calculateStartingTilgungsplan(LocalDate startingDate, Darlehensbetrag darlehensbetrag) {
        return new Tilgungsplan(formatDate(startingDate.with(TemporalAdjusters.lastDayOfMonth())), roundValue(darlehensbetrag.getVorMonatBetrag()).negate(), BigDecimal.ZERO,
                roundValue(darlehensbetrag.getVorMonatBetrag()).negate(),
                roundValue(darlehensbetrag.getVorMonatBetrag()).negate());
    }

    public Tilgungsplan calculateEndingTilgungsplan(LocalDate endingDate, Darlehensbetrag darlehensbetrag, List<Tilgungsplan> plans) {
        return new Tilgungsplan(formatDate(endingDate), darlehensbetrag.getVorMonatBetrag().negate(),
                plans.stream().map(Tilgungsplan::zinsen).reduce(BigDecimal.ZERO, BigDecimal::add),
                plans.stream().skip(1).map(Tilgungsplan::tilgung).reduce(BigDecimal.ZERO, BigDecimal::add),
                plans.stream().skip(1).map(Tilgungsplan::rate).reduce(BigDecimal.ZERO, BigDecimal::add));

    }

    public BigDecimal calculateFixedRate(BigDecimal darlehensbetrag, BigDecimal anfaenglichenTilgung, BigDecimal zinssatz) {
        BigDecimal tillgungsPerMonth = calculateTilgungPerMonth(darlehensbetrag, anfaenglichenTilgung);
        BigDecimal zinsenPerMonth = calculateZinsenPerMonth(darlehensbetrag, zinssatz);
        return tillgungsPerMonth.add(zinsenPerMonth);
    }

    public BigDecimal calculateTilgungPerMonth(BigDecimal darlehensbetrag, BigDecimal anfaenglichenTilgung) {
        return roundValue(new BigDecimal(darlehensbetrag.doubleValue() / 100 * anfaenglichenTilgung.doubleValue() / 12), RoundingMode.FLOOR);
    }

    public BigDecimal calculateZinsenPerMonth(BigDecimal darlehensbetrag, BigDecimal zinssatz) {
        return roundValue(new BigDecimal(darlehensbetrag.doubleValue() / 100 * zinssatz.doubleValue() / 12));
    }
}
