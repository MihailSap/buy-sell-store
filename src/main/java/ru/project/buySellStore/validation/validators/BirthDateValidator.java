package ru.project.buySellStore.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.project.buySellStore.validation.annotations.BirthDate;

import java.util.Date;

/**
 * Валидатор для проверки корректности даты рождения
 * @author SapeginMihail
 */
public class BirthDateValidator implements ConstraintValidator<BirthDate, Date> {

    @Override
    public boolean isValid(Date birthDate, ConstraintValidatorContext constraintValidatorContext) {
        return birthDate == null || !birthDate.after(new Date());
    }
}
