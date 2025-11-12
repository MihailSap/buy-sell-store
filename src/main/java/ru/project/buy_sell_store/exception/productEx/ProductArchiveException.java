package ru.project.buy_sell_store.exception.productEx;

import ru.project.buy_sell_store.exception.globalEx.BuySellStoreConflictException;

/**
 * Exception при повторной архивации
 */
public class ProductArchiveException extends BuySellStoreConflictException {

    public ProductArchiveException(Long id) {
        super("Товар с id = " + id + " находится уже в архиве");
    }
}
