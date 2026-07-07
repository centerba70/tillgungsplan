package com.coding_challenge.tilgungsplan.input_validation;

import com.coding_challenge.tilgungsplan.InputForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TilgungFormValidator implements ConstraintValidator<ConsistentTilgungsplan, InputForm> {

    private final List<InputValidationStrategy> inputValidationStrategies;

    public TilgungFormValidator(List<InputValidationStrategy> inputValidationStrategies) {
        this.inputValidationStrategies = inputValidationStrategies;
    }

    @Override
    public boolean isValid(InputForm value, ConstraintValidatorContext context) {
        return inputValidationStrategies.stream().allMatch(inputValidationStrategy -> inputValidationStrategy.validate(value));
    }
}
