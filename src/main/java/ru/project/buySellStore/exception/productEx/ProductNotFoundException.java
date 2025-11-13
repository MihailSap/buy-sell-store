package ru.project.buySellStore.exception.productEx;

import ru.project.buySellStore.exception.globalEx.BuySellStoreNotFoundException;

/**
 * Exception при ненахождении Товара
 */
public class ProductNotFoundException extends BuySellStoreNotFoundException {
    public ProductNotFoundException(Long id) {
        super("Товар с id = " + id + " не найден");
    }
}
