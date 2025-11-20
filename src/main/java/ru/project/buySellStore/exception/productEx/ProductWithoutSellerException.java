package ru.project.buySellStore.exception.productEx;

import ru.project.buySellStore.exception.globalEx.BuySellStoreConflictException;

/**
 * Exception когда у товара нет назначенного продавца
 */
public class ProductWithoutSellerException extends BuySellStoreConflictException {
    public ProductWithoutSellerException(Long id) {
        super("Товар с id = " + id + " не имеет назначенного продавца");
    }
}
