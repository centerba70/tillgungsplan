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
                .vorMonatBetrag(roundValue(inputForm.darlehensbetrag()))
                .laufenderMonatBetrag(roundValue(inputForm.darlehensbetrag()))
                .fixedRate(roundValue(calculateFixedRate(roundValue(inputForm.darlehensbetrag()), anfaenglichenTilgung, zinssatz)))
                .build();


        // Calculate tilgung periods and number of months
        LocalDate tilgungStartingDate = LocalDate.of(2015, 11, 30); // TODO: change this
        LocalDate tilgungEndingDate = tilgungStartingDate.plusYears(zinsbindung);

        // Create list of tilgungs and add starting tilgung
        Tilgungsplan startingPlan = calculateStartingTilgungsplan(tilgungStartingDate, darlehensbetrag);
        List<Tilgungsplan> tilgungsplanPerMonths = new ArrayList<>();
        tilgungsplanPerMonths.add(startingPlan);

        // Set up iteration variables
        int i = 0;
        long tilgungDurationInMonths = ChronoUnit.MONTHS.between(tilgungStartingDate, tilgungEndingDate);

        // Start iteration
        while (i < tilgungDurationInMonths) {
            BigDecimal tilgungsPerMonth = calculateTilgungPerMonth(darlehensbetrag.getVorMonatBetrag(), anfaenglichenTilgung);
            BigDecimal zinsenPerMonth = roundValue(new BigDecimal(darlehensbetrag.getVorMonatBetrag().doubleValue() / 100 * zinssatz.doubleValue() / 12));
            if (!darlehensbetrag.getFixedRate().equals(tilgungsPerMonth.add(zinsenPerMonth))) {
                // adjust tilgung
                tilgungsPerMonth = darlehensbetrag.getFixedRate().subtract(zinsenPerMonth);
            }
            darlehensbetrag.setLaufenderMonatBetrag(darlehensbetrag.getVorMonatBetrag().subtract(tilgungsPerMonth));
            tilgungsplanPerMonths.add(new Tilgungsplan(formatDate(tilgungStartingDate.plusMonths(i).with(TemporalAdjusters.lastDayOfMonth())),
                    darlehensbetrag.getLaufenderMonatBetrag().negate(), zinsenPerMonth, tilgungsPerMonth, darlehensbetrag.getFixedRate()));
            darlehensbetrag.setVorMonatBetrag(darlehensbetrag.getLaufenderMonatBetrag());
            darlehensbetrag.setLaufenderMonatBetrag(new BigDecimal("0.0"));
            i++;
        }

        // add last resume element
        Tilgungsplan endingPlan = calculateEndingTilgungsplan(tilgungEndingDate, darlehensbetrag, tilgungsplanPerMonths);
        tilgungsplanPerMonths.add(endingPlan);

        logger.info("Tilgungsplan erfolgreich erstellt. ");
        return tilgungsplanPerMonths;
    }


    private Tilgungsplan calculateStartingTilgungsplan(LocalDate startingDate, Darlehensbetrag darlehensbetrag) {
        return new Tilgungsplan(formatDate(startingDate.with(TemporalAdjusters.lastDayOfMonth())), roundValue(darlehensbetrag.getVorMonatBetrag()).negate(), BigDecimal.ZERO,
                roundValue(darlehensbetrag.getVorMonatBetrag()).negate(),
                roundValue(darlehensbetrag.getVorMonatBetrag()).negate());
    }

    private Tilgungsplan calculateEndingTilgungsplan(LocalDate endingDate, Darlehensbetrag darlehensbetrag, List<Tilgungsplan> plans) {
        return new Tilgungsplan(formatDate(endingDate), darlehensbetrag.getVorMonatBetrag().negate(),
                plans.stream().map(Tilgungsplan::zinsen).reduce(BigDecimal.ZERO, BigDecimal::add),
                plans.stream().skip(1).map(Tilgungsplan::tilgung).reduce(BigDecimal.ZERO, BigDecimal::add),
                plans.stream().skip(1).map(Tilgungsplan::rate).reduce(BigDecimal.ZERO, BigDecimal::add));

    }

    private BigDecimal calculateFixedRate(BigDecimal darlehensbetrag, BigDecimal anfaenglichenTilgung, BigDecimal zinssatz) {
        BigDecimal tillgungsPerMonth = calculateTilgungPerMonth(darlehensbetrag, anfaenglichenTilgung);
        BigDecimal zinsenPerMonth = calculateZinsenPerMonth(darlehensbetrag, zinssatz);
        return tillgungsPerMonth.add(zinsenPerMonth);
    }

    private BigDecimal calculateTilgungPerMonth(BigDecimal darlehensbetrag, BigDecimal anfaenglichenTilgung) {
        return roundValue(new BigDecimal(darlehensbetrag.doubleValue() / 100 * anfaenglichenTilgung.doubleValue() / 12), RoundingMode.FLOOR);
    }

    private BigDecimal calculateZinsenPerMonth(BigDecimal darlehensbetrag, BigDecimal zinssatz) {
        return roundValue(new BigDecimal(darlehensbetrag.doubleValue() / 100 * zinssatz.doubleValue() / 12));
    }
}
