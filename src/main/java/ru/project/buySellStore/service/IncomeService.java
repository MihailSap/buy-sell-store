package ru.project.buySellStore.service;

import ru.project.buySellStore.model.Product;

import java.util.List;

public interface IncomeService {

    /**
     * Подсчет дохода: Товар куплен, то к сумме добавляется
     * разница между цены поставщика с ценой продавца
     */
    int calculateIncomeSeller(List<Product> products);

    /**
     * Подсчет дохода: Товар куплен, то к сумме добавляется
     * цена поставщика
     */
    int calculateIncomeSupplier(List<Product> products);
}
