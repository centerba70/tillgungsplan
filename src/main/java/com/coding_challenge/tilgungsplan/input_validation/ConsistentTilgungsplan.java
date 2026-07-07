package com.coding_challenge.tilgungsplan.input_validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TilgungFormValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsistentTilgungsplan {

    String message() default "Aus diesen parameters wird sich ein inkonsistenter Tilgungsplan ergeben. Bitte geben die erneut.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}