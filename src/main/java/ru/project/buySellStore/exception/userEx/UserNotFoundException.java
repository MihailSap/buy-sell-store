package ru.project.buySellStore.exception.userEx;

import ru.project.buySellStore.exception.globalEx.BuySellStoreNotFoundException;

/**
 * Exception, возникающий, когда пользователь не найден
 */
public class UserNotFoundException extends BuySellStoreNotFoundException {
    public UserNotFoundException(Long id) {
        super("Пользователь с id = " + id + " не найден");
    }
}
