package ru.project.buy_sell_store.exception.globalEx;

/**
 * Exception для конфликтов
 */
public class BuySellConflictException extends BuySellStoreException{
    public BuySellConflictException(String message) {
        super(message);
    }
}
