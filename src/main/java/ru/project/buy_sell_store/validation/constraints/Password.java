package ru.project.buy_sell_store.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.lang.annotation.*;

@NotEmpty(message = "Пароль не должен быть пустым")
@Length(max = 30, message = "Длина пароля не должна превышать 30 символов")
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface Password {

    String message() default "Некорректный пароль";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
