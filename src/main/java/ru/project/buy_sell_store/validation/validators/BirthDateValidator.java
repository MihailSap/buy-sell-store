package ru.project.buy_sell_store.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.project.buy_sell_store.validation.annotations.BirthDate;

import java.util.Date;

/**
 * Валидатор для проверки корректности даты рождения
 * @author SapeginMihail
 */
public class BirthDateValidator implements ConstraintValidator<BirthDate, Date> {

    /**
     * Проверяет, чтобы указанная дата была прошедшей
     * @param birthDate дата рождения, указанная пользователем
     * @param constraintValidatorContext контекст валидации
     * @return {@code true}, если дата рождения указана верно {@code false}, если нет
     */
    @Override
    public boolean isValid(Date birthDate, ConstraintValidatorContext constraintValidatorContext) {
        return birthDate == null || !birthDate.after(new Date());
    }
}
