package ru.project.buy_sell_store.exception.globalEx;

/**
 * Главный Exception проекта
 */
public class BuySellStoreException extends RuntimeException {

    /**
     * Создание c указанным сообщением
     */
    public BuySellStoreException(String message) {
        super(message);
    }
}
