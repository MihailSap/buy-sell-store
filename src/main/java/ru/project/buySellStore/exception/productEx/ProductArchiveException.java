package ru.project.buySellStore.exception.productEx;

import ru.project.buySellStore.exception.globalEx.BuySellStoreConflictException;

/**
 * Exception при повторной архивации
 */
public class ProductArchiveException extends BuySellStoreConflictException {

    public ProductArchiveException(Long id) {
        super("Товар с id = " + id + " уже находится в архиве");
    }
}
