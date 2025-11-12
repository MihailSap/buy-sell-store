package ru.project.buy_sell_store.exception.productEx;

import ru.project.buy_sell_store.exception.globalEx.BuySellConflictException;

/**
 * Exception при повторного восстановления из архива
 */
public class ProductRestoreException extends BuySellConflictException {
    public ProductRestoreException(Long id) {
        super("Товар c id = " + id + " уже доступен и не находится в архиве");
    }
}
