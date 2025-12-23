package ru.project.buySellStore.exception.userEx;

import ru.project.buySellStore.exception.globalEx.BuySellStoreNotSuitableRoleException;

/**
 * Exception для ситуаций, когда роль пользователя не подходит для выполнения действия
 */
public class UserNotSuitableRoleException extends BuySellStoreNotSuitableRoleException {
    public UserNotSuitableRoleException(String message) {
        super(message);
    }
}
