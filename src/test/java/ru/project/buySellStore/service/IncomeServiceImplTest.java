package ru.project.buySellStore.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.service.impl.IncomeServiceImpl;

import java.util.List;

/**
 * Тесты для {@link IncomeServiceImpl}
 */
class IncomeServiceImplTest {

    private IncomeService incomeService;

    /**
     * Подготовка тестовых данных
     */
    @BeforeEach
    void setUp() {
        incomeService = new IncomeServiceImpl();
    }

    /**
     * Тестирование расчета дохода продавца от трех продуктов
     * <p>Ожидается, что доход будет верно посчитан: (150 - 100) + (260 - 200) + (400 - 300) = 210</p>
     *
     */
    @Test
    void testCalculateIncomeSellerWithBoughtProducts() {
        Product soldProduct1 = new Product();
        soldProduct1.setSupplierCost(100);
        soldProduct1.setSellerCost(150);

        Product soldProduct2 = new Product();
        soldProduct2.setSupplierCost(200);
        soldProduct2.setSellerCost(260);

        Product soldProduct3 = new Product();
        soldProduct3.setSupplierCost(300);
        soldProduct3.setSellerCost(400);

        int income = incomeService.calculateIncomeSeller(
                List.of(soldProduct1, soldProduct2, soldProduct3)
        );

        Assertions.assertEquals(210, income);
    }

    /**
     * Тестирование расчета дохода продавца без продуктов
     * <p>Доход - 0</p>
     */
    @Test
    void testCalculateIncomeSellerWithEmptyProducts() {
        int income = incomeService.calculateIncomeSeller(List.of());
        Assertions.assertEquals(0, income);
    }

    /**
     * Тестирование расчета дохода поставщика от трех продуктов
     * <p>Ожидается, что доход будет верно посчитан: 500 + 700 + 1000 = 2200</p>
     */
    @Test
    void testCalculateIncomeSupplierWithBoughtProducts() {
        Product soldProduct1 = new Product();
        soldProduct1.setSupplierCost(500);

        Product soldProduct2 = new Product();
        soldProduct2.setSupplierCost(700);

        Product soldProduct3 = new Product();
        soldProduct3.setSupplierCost(1000);

        int income = incomeService.calculateIncomeSupplier(
                List.of(soldProduct1, soldProduct2, soldProduct3)
        );

        Assertions.assertEquals(2200, income);
    }

    /**
     * Тестирование расчета дохода поставщика без продуктов
     * <p>Доход - 0</p>
     */
    @Test
    void testCalculateIncomeSupplierWithEmptyProducts() {
        int income = incomeService.calculateIncomeSupplier(List.of());

        Assertions.assertEquals(0, income);
    }
}
