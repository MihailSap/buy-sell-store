package ru.project.buy_sell_store.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import ru.project.buy_sell_store.validation.validators.RoleValidator;

import java.lang.annotation.*;

@NotEmpty
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
public @interface Role {

    String message() default "Некорректная роль. Выберите нужную роль: SUPPLIER, SELLER, BUYER";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
