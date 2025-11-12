package ru.project.buySellStore.exception.productEx;

import ru.project.buySellStore.exception.globalEx.BuySellStoreNotFoundException;

public class UserNotFoundException extends BuySellStoreNotFoundException {
    public UserNotFoundException(Long id) {
        super("Пользователь с id = " + id + " не найден");
    }
}
