package ru.project.buy_sell_store.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.project.buy_sell_store.validation.annotations.BirthDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Валидатор для проверки корректности даты рождения
 * Проверяет, что указанная дата не превышает текущую дату,
 * и что дата указана в правильном формате
 * @author SapeginMihail
 */
public class BirthDateValidator implements ConstraintValidator<BirthDate, String> {

    /**
     * Шаблон для парсинга даты
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Проверяет, соответствует ли значение дате рождения в нужном формате и не находится ли оно в будущем
     * @param birthDate дата рождения, указанная пользователем
     * @param constraintValidatorContext контекст валидации
     * @return {@code true}, если дата рождения указана верно; {@code false}, если нет
     */
    @Override
    public boolean isValid(String birthDate, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate date = LocalDate.parse(birthDate, FORMATTER);
            return !date.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
