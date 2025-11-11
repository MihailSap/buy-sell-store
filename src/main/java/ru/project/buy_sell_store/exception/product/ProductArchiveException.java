package ru.project.buy_sell_store.exception.product;

import ru.project.buy_sell_store.exception.globalEx.BuySellConflictException;

/**
 * Exception при повторной архивации
 */
public class ProductArchiveException extends BuySellConflictException {

    public ProductArchiveException(Long id) {
        super("Товар с id = " + id + " находится уже в архиве");
    }
}
