package com.coding_challenge.tilgungsplan;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class Darlehensbetrag {
    private BigDecimal vorMonatBetrag;

    private BigDecimal laufenderMonatBetrag;

    private BigDecimal fixedRate;
}
