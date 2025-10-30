package ru.project.buy_sell_store.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.project.buy_sell_store.validation.validators.BirthDateValidator;

import java.lang.annotation.*;

/**
 * Аннотация для проверки корректности введенной даты рождения.
 * При нарушении правила возвращает указанное сообщение по умолчанию.
 * @author SapeginMihail
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthDateValidator.class)
public @interface BirthDate {

    /**
     * Сообщение об ошибке.
     * Возвращается, если дата указана некорректно
     * @return строка с сообщением об ошибке
     */
    String message() default "Дата должна быть корректной, в формате: yyyy-mm-dd";

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
