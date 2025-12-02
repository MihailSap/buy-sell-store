package ru.project.buySellStore.exception.userEx;

import ru.project.buySellStore.exception.globalEx.BuySellStoreConflictException;

/**
 * Exception, возникающий при попытке создать аккаунт с уже существующим в БД логином
 */
public class UserAlreadyExistsException extends BuySellStoreConflictException {
    public UserAlreadyExistsException(String login) {
        super(String.format("Пользователь с логином '%s' уже существует", login));
    }
}
