package ru.project.buy_sell_store.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.project.buy_sell_store.enums.RoleEnum;
import ru.project.buy_sell_store.validation.constraints.Role;

public class RoleValidator implements ConstraintValidator<Role, RoleEnum> {

    @Override
    public boolean isValid(RoleEnum role, ConstraintValidatorContext context) {
        for (RoleEnum r : RoleEnum.values()) {
            if (r == role) {
                return true;
            }
        }
        return false;
    }
}
