package com.coding_challenge.tilgungsplan.input_validation;

import com.coding_challenge.tilgungsplan.InputForm;
import com.coding_challenge.tilgungsplan.TilgungsplanHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TotalRatesInputValidation implements InputValidationStrategy {

    private final TilgungsplanHelper tilgungsplanHelper;
    private static final int NUMBER_OF_MONTHS = 12;

    public TotalRatesInputValidation(TilgungsplanHelper tilgungsplanHelper) {
        this.tilgungsplanHelper = tilgungsplanHelper;
    }

    @Override
    public boolean validate(InputForm inputForm) {
        // Calculate Monthly Rate
        BigDecimal fixedRate = tilgungsplanHelper.calculateFixedRate(inputForm.darlehensbetrag(), inputForm.anfaenglicheTilgung(), inputForm.zinssatz());
        // Der Gesamtbetrag der Zinszahlungen innerhalb der Zinsbindungsfrist muss geringer sein als der Darlehensbetrag.
        return (fixedRate.doubleValue() * NUMBER_OF_MONTHS * inputForm.zinsbindung()) < inputForm.darlehensbetrag().doubleValue();
    }
}
