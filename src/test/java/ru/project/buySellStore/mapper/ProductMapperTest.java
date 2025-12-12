package ru.project.buySellStore.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.dto.ViewProductDTO;

/**
 * Тесты для {@link ProductMapper}
 */
class ProductMapperTest {

    private ProductMapper productMapper;
    private Product product;

    /**
     * Настройка тестовых данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();

        product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("description");
        product.setCategory("CLOTHES");
        product.setSupplierCost(100);
        product.setSellerCost(150);
    }

    /**
     * Проверяет корректное преобразование продукта в ViewProductDTO для роли SUPPLIER
     */
    @Test
    void testToDtoForSupplier() {
        ViewProductDTO dto = productMapper.toDto(product, Role.SUPPLIER);

        Assertions.assertEquals(product.getId(), dto.id());
        Assertions.assertEquals(product.getName(), dto.name());
        Assertions.assertEquals(product.getDescription(), dto.description());
        Assertions.assertEquals(product.getCategory(), dto.category());
        Assertions.assertEquals(product.getSupplierCost(), dto.cost());
    }

    /**
     * Проверяет корректное преобразование продукта в ViewProductDTO для роли SELLER
     */
    @Test
    void testToDtoForSeller() {
        ViewProductDTO dto = productMapper.toDto(product, Role.SELLER);

        Assertions.assertEquals(product.getId(), dto.id());
        Assertions.assertEquals(product.getName(), dto.name());
        Assertions.assertEquals(product.getDescription(), dto.description());
        Assertions.assertEquals(product.getCategory(), dto.category());
        Assertions.assertEquals(product.getSellerCost(), dto.cost());
    }

    /**
     * Проверяет корректное преобразование продукта в ViewProductDTO для роли BUYER
     */
    @Test
    void testToDtoForBuyer() {
        ViewProductDTO dto = productMapper.toDto(product, Role.BUYER);

        Assertions.assertEquals(product.getId(), dto.id());
        Assertions.assertEquals(product.getName(), dto.name());
        Assertions.assertEquals(product.getDescription(), dto.description());
        Assertions.assertEquals(product.getCategory(), dto.category());
        Assertions.assertEquals(product.getSellerCost(), dto.cost());
    }
}
