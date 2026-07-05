package com.coding_challenge.tilgungsplan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import static com.coding_challenge.tilgungsplan.TilgungsplanUtil.*;


@Service
public class TilgungsplanService {
    // TODO: add null safety checks with JSpecify (Spring boot 4)

    private static final Logger logger = LoggerFactory.getLogger(TilgungsplanService.class);

    // TODO: Create a class with this parameters and pass it to this method?
    public List<Tilgungsplan> calculateTilgung(InputForm inputForm) {
        // TODO: refactor
        // get all the inserted values from Inputform
        BigDecimal anfaenglichenTilgung = inputForm.anfaenglichenTilgung();
        BigDecimal zinssatz = inputForm.zinssatz();
        long zinsbindung = inputForm.zinsbindung(); // in Jahren

        // Create Darlehensbetrag object
        Darlehensbetrag darlehensbetrag = Darlehensbetrag.builder()
                .vorMonatBetrag(round_big_dec(inputForm.darlehensbetrag(), RoundingMode.HALF_EVEN))
                .laufenderMonatBetrag(round_big_dec(inputForm.darlehensbetrag(), RoundingMode.HALF_EVEN))
                .fixedRate(round_big_dec(calculateFixedRate(round_big_dec(inputForm.darlehensbetrag(), RoundingMode.HALF_EVEN), anfaenglichenTilgung, zinssatz), RoundingMode.HALF_EVEN))
                .build();


        // Calculate tilgung periods and number of months
        LocalDate tilgungStartingDate = LocalDate.of(2015, 11, 30);
        LocalDate tilgungEndingDate = tilgungStartingDate.plusYears(zinsbindung);
        long tilgungDurationInMonths = ChronoUnit.MONTHS.between(tilgungStartingDate, tilgungEndingDate);

        // Create list of tilgungs and add starting tilgung
        Tilgungsplan startingPlan = calculateStartingTilgungsplan(tilgungStartingDate, darlehensbetrag);
        List<Tilgungsplan> tilgungsplanPerMonths = new ArrayList<>();
        tilgungsplanPerMonths.add(startingPlan);

        int i = 0;
        while (i < tilgungDurationInMonths) {
            BigDecimal tilgungsPerMonth = round_big_dec(new BigDecimal(darlehensbetrag.getVorMonatBetrag().doubleValue() / 100 * anfaenglichenTilgung.doubleValue() / 12), RoundingMode.FLOOR); //Floor?
            BigDecimal zinsenPerMonth = round_big_dec(new BigDecimal(darlehensbetrag.getVorMonatBetrag().doubleValue() / 100 * zinssatz.doubleValue() / 12), RoundingMode.HALF_EVEN); //Ceiling?
            if (!darlehensbetrag.getFixedRate().equals(tilgungsPerMonth.add(zinsenPerMonth))) {
                // adjust tillgung
                tilgungsPerMonth = darlehensbetrag.getFixedRate().subtract(zinsenPerMonth);
            }
            darlehensbetrag.setLaufenderMonatBetrag(darlehensbetrag.getVorMonatBetrag().subtract(tilgungsPerMonth));
            tilgungsplanPerMonths.add(new Tilgungsplan(tilgungStartingDate.plusMonths(i).with(TemporalAdjusters.lastDayOfMonth()),
                    darlehensbetrag.getLaufenderMonatBetrag().negate(), zinsenPerMonth, tilgungsPerMonth, darlehensbetrag.getFixedRate()));
            darlehensbetrag.setVorMonatBetrag(darlehensbetrag.getLaufenderMonatBetrag());
            darlehensbetrag.setLaufenderMonatBetrag(new BigDecimal("0.0"));
            i++;
        }

        // add last resume element
        Tilgungsplan endingPlan = calculateEndingTillgungsplan(LocalDate.now(), darlehensbetrag, tilgungsplanPerMonths);
        tilgungsplanPerMonths.add(endingPlan);

        logger.info("Done");
        return tilgungsplanPerMonths;
    }


    private Tilgungsplan calculateStartingTilgungsplan(LocalDate startingDate, Darlehensbetrag darlehensbetrag) {
        return new Tilgungsplan(startingDate.with(TemporalAdjusters.lastDayOfMonth()), round_big_dec(darlehensbetrag.getVorMonatBetrag(), RoundingMode.HALF_EVEN).negate(), new BigDecimal("0.0"),
                round_big_dec(darlehensbetrag.getVorMonatBetrag(), RoundingMode.HALF_EVEN).negate(),
                round_big_dec(darlehensbetrag.getVorMonatBetrag(), RoundingMode.HALF_EVEN).negate());
    }

    private Tilgungsplan calculateEndingTillgungsplan(LocalDate startingDate, Darlehensbetrag darlehensbetrag, List<Tilgungsplan> plans) {
        return new Tilgungsplan(LocalDate.now(), darlehensbetrag.getVorMonatBetrag().negate(),
                plans.stream().map(Tilgungsplan::zinsen).reduce(BigDecimal.ZERO, BigDecimal::add),
                plans.stream().skip(1).map(Tilgungsplan::tilgung).reduce(BigDecimal.ZERO, BigDecimal::add),
                plans.stream().skip(1).map(Tilgungsplan::rate).reduce(BigDecimal.ZERO, BigDecimal::add));

    }

    private BigDecimal calculateFixedRate(BigDecimal darlehensbetrag, BigDecimal anfaenglichenTilgung, BigDecimal zinssatz) {
        BigDecimal tillgungsPerMonth = new BigDecimal(darlehensbetrag.doubleValue() / 100 * anfaenglichenTilgung.doubleValue() / 12);
        BigDecimal zinsenPerMonth = new BigDecimal(darlehensbetrag.doubleValue() / 100 * zinssatz.doubleValue() / 12);
        return tillgungsPerMonth.add(zinsenPerMonth);
    }
}
