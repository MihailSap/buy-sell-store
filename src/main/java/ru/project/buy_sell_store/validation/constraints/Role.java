package ru.project.buy_sell_store.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import ru.project.buy_sell_store.validation.validators.RoleValidator;

import java.lang.annotation.*;

/**
 * Аннотация для проверки корректности введенной роли.
 * При нарушении правила возвращает соответствующее сообщение об ошибке.
 * @author SapeginMihail
 */
@NotEmpty
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
public @interface Role {

    /**
     * Сообщение об ошибке.
     * Возвращается, если роль указана некорректно
     * @return строка с сообщением об ошибке
     */
    String message() default "Некорректная роль. Выберите нужную роль: SUPPLIER, SELLER, BUYER";

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
