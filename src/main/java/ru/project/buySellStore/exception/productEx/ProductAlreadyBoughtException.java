package ru.project.buySellStore.exception.productEx;


import ru.project.buySellStore.exception.globalEx.BuySellStoreConflictException;

/**
 * Exception когда товар уже куплен
 */
public class ProductAlreadyBoughtException extends BuySellStoreConflictException {
    public ProductAlreadyBoughtException(Long id) {
        super("Товар с id = " + id + " уже куплен");
    }
}
