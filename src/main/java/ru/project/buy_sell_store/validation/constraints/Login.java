package ru.project.buy_sell_store.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.lang.annotation.*;

@Pattern(
        regexp = "^[A-Za-zА-Яа-я0-9]+$",
        message = "Логин может содержать только буквы и цифры"
)
@NotEmpty(message = "Логин не должен быть пустым")
@Length(max = 30, message = "Длина логина не должна превышать 30 символов")
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface Login {

    String message() default "Некорректный логин";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
