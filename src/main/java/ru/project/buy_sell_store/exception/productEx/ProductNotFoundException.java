package ru.project.buy_sell_store.exception.productEx;

import ru.project.buy_sell_store.exception.globalEx.BuySellStoreNotFoundException;

/**
 * Exception при ненахождении Товара
 */
public class ProductNotFoundException extends BuySellStoreNotFoundException {
    public ProductNotFoundException(Long id) {
        super("Товар с id = " + id + " не найден");
    }
}
