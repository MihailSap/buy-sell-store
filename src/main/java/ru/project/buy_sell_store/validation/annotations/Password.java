package ru.project.buy_sell_store.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.lang.annotation.*;

/**
 * Аннотация для проверки корректности введенного пароля.
 * При нарушении правила возвращает соответствующее сообщение об ошибке.
 * @author SapeginMihail
 */
@NotEmpty(message = "Пароль не должен быть пустым")
@Length(max = 30, message = "Длина пароля не должна превышать 30 символов")
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface Password {

    /**
     * Сообщение об ошибке.
     * Возвращается, если пароль указан некорректно
     * @return строка с сообщением об ошибке
     */
    String message() default "Некорректный пароль";

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
