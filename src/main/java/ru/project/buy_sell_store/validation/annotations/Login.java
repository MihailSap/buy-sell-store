package ru.project.buy_sell_store.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.lang.annotation.*;

/**
 * Аннотация для проверки корректности введенного логина.
 * При нарушении правила возвращает соответствующее сообщение об ошибке.
 * @author SapeginMihail
 */
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

    /**
     * Сообщение об ошибке.
     * Возвращается, если логин указан некорректно
     * @return строка с сообщением об ошибке
     */
    String message() default "Некорректный логин";

    /**
     * Группы валидации
     * @return по умолчанию группы валидации пустые
     */
    Class<?>[] groups() default {};

    /**
     * Дополнительные данные
     */
    Class<? extends Payload>[] payload() default {};
}
