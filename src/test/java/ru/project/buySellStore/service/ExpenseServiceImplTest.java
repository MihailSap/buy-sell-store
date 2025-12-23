package ru.project.buySellStore.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.service.impl.ExpenseServiceImpl;

import java.util.List;

/**
 * Тесты для {@link ExpenseServiceImpl}
 */
class ExpenseServiceImplTest {

    private ExpenseService expenseService;

    /**
     * Подготовка тестовых данных
     */
    @BeforeEach
    void setUp() {
        expenseService = new ExpenseServiceImpl();
    }

    /**
     * Тестирование расчета расхода у покупателя от трех покупок
     */
    @Test
    void testGetExpense_BoughtProducts() {
        Product product1 = new Product();
        product1.setSellerCost(1000);

        Product product2 = new Product();
        product2.setSellerCost(2500);

        Product product3 = new Product();
        product3.setSellerCost(500);

        int expense = expenseService.getExpense(
                List.of(product1, product2, product3)
        );

        Assertions.assertEquals(4000, expense);
    }

    /**
     * Тестирование расчета расхода у покупателя без купленных продуктов
     * Если список товаров пуст — расход 0
     */
    @Test
    void getExpense_emptyProducts() {
        int expense = expenseService.getExpense(List.of());
        Assertions.assertEquals(0, expense);
    }
}
