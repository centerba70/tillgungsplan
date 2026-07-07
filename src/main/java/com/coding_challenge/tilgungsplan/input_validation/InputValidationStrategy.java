package com.coding_challenge.tilgungsplan.input_validation;

import com.coding_challenge.tilgungsplan.InputForm;

public interface InputValidationStrategy {

    boolean validate(InputForm inputForm);
}