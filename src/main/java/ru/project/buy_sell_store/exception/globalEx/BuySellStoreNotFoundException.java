package ru.project.buy_sell_store.exception.globalEx;

/**
 *  Exception при отсутствии объекта из базы данных
 */
public class BuySellStoreNotFoundException extends BuySellStoreException {
    public BuySellStoreNotFoundException(String message) {
        super(message);
    }
}
