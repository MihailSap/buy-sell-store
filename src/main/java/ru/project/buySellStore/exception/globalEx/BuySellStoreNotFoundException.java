package ru.project.buySellStore.exception.globalEx;

/**
 *  Exception при отсутствии объекта из базы данных
 */
public class BuySellStoreNotFoundException extends BuySellStoreException {
    public BuySellStoreNotFoundException(String message) {
        super(message);
    }
}
