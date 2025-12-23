package ru.project.buySellStore.service;

import ru.project.buySellStore.model.Product;

import java.util.List;

/**
 * Интерфейс для получения суммы расхода
 */
public interface ExpenseService {

    /**
     * Получить расход
     * @param products купленные продукты
     */
    int getExpense(List<Product> products);
}
