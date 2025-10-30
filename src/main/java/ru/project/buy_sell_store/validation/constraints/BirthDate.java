package ru.project.buy_sell_store.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.project.buy_sell_store.validation.validators.BirthDateValidator;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthDateValidator.class)
public @interface BirthDate {

    String message() default "Дата должна быть корректной, в формате: yyyy-mm-dd";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
