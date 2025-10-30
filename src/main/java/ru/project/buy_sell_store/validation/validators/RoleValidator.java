package ru.project.buy_sell_store.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.project.buy_sell_store.enums.RoleEnum;
import ru.project.buy_sell_store.validation.annotations.Role;

/**
 * Валидатор для проверки корректности указанной роли.
 * Проверяет, что роль, которую указал пользователь, существует в системе
 * @author SapeginMihail
 */
public class RoleValidator implements ConstraintValidator<Role, String> {

    /**
     * Проверка, существует ли в системе роль, указанная пользователем
     * @param inputRole роль, которую ввел пользователь
     * @param constraintValidatorContext контекст валидации
     * @return {@code true}, если указанная роль корректна; {@code false}, если нет
     */
    @Override
    public boolean isValid(String inputRole, ConstraintValidatorContext constraintValidatorContext) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.name().equals(inputRole)) {
                return true;
            }
        }
        return false;
    }
}
