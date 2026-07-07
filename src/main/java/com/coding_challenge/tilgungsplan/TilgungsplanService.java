package com.coding_challenge.tilgungsplan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import static com.coding_challenge.tilgungsplan.TilgungsplanUtil.*;


@Service
public class TilgungsplanService {
    // TODO: add null safety checks with JSpecify (Spring boot 4)

    private TilgungsplanHelper tilgungsplanHelper;

    private final LocalDate tilgungStartingDate;

    private static final Logger logger = LoggerFactory.getLogger(TilgungsplanService.class);

    public TilgungsplanService(TilgungsplanHelper tilgungsplanHelper, @Value("${tilgung.starting_date}") String tilgungStartingDate) {
        this.tilgungStartingDate = formatDate(tilgungStartingDate);
        this.tilgungsplanHelper = tilgungsplanHelper;
    }

    public List<Tilgungsplan> calculateTilgung(InputForm inputForm) {
        // TODO: refactor
        // get all the inserted values from Inputform
        BigDecimal anfaenglichenTilgung = inputForm.anfaenglicheTilgung();
        BigDecimal zinssatz = inputForm.zinssatz();
        long zinsbindung = inputForm.zinsbindung();

        // Create Darlehensbetrag object
        Darlehensbetrag darlehensbetrag = Darlehensbetrag.builder()
                .vorMonatBetrag(roundValue(inputForm.darlehensbetrag()))
                .laufenderMonatBetrag(roundValue(inputForm.darlehensbetrag()))
                .fixedRate(roundValue(tilgungsplanHelper.calculateFixedRate(roundValue(inputForm.darlehensbetrag()), anfaenglichenTilgung, zinssatz)))
                .build();

        // Calculate tilgung periods and number of months
        LocalDate tilgungEndingDate = tilgungStartingDate.plusYears(zinsbindung);

        // Create list of tilgungs and add starting tilgung
        Tilgungsplan startingPlan = tilgungsplanHelper.calculateStartingTilgungsplan(tilgungStartingDate, darlehensbetrag);
        List<Tilgungsplan> tilgungsplanPerMonths = new ArrayList<>();
        tilgungsplanPerMonths.add(startingPlan);

        // Set up iteration variables
        int month_counter = 0;
        long tilgungDurationInMonths = ChronoUnit.MONTHS.between(tilgungStartingDate, tilgungEndingDate);

        // Start iteration
        while (month_counter < tilgungDurationInMonths) {
            BigDecimal tilgungsPerMonth = tilgungsplanHelper.calculateTilgungPerMonth(darlehensbetrag.getVorMonatBetrag(), anfaenglichenTilgung);
            BigDecimal zinsenPerMonth = tilgungsplanHelper.calculateZinsenPerMonth(darlehensbetrag.getVorMonatBetrag(), zinssatz);
            if (!darlehensbetrag.getFixedRate().equals(tilgungsPerMonth.add(zinsenPerMonth))) {
                // adjust tilgung
                tilgungsPerMonth = darlehensbetrag.getFixedRate().subtract(zinsenPerMonth);
            }
            darlehensbetrag.setLaufenderMonatBetrag(darlehensbetrag.getVorMonatBetrag().subtract(tilgungsPerMonth));
            tilgungsplanPerMonths.add(new Tilgungsplan(formatDate(tilgungStartingDate.plusMonths(month_counter).with(TemporalAdjusters.lastDayOfMonth())),
                    darlehensbetrag.getLaufenderMonatBetrag().negate(), zinsenPerMonth, tilgungsPerMonth, darlehensbetrag.getFixedRate()));
            darlehensbetrag.setVorMonatBetrag(darlehensbetrag.getLaufenderMonatBetrag());
            darlehensbetrag.setLaufenderMonatBetrag(new BigDecimal("0.0"));
            month_counter++;
        }

        // add last resume element
        Tilgungsplan endingPlan = tilgungsplanHelper.calculateEndingTilgungsplan(tilgungEndingDate, darlehensbetrag, tilgungsplanPerMonths);
        tilgungsplanPerMonths.add(endingPlan);

        logger.info("Tilgungsplan erfolgreich erstellt. ");
        return tilgungsplanPerMonths;
    }
}
