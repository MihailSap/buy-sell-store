package ru.project.buy_sell_store.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.project.buy_sell_store.dto.ProductDTO;
import ru.project.buy_sell_store.model.Product;


/**
 * Тесты для проверки маппера Товара
 */
class ProductMapperTest {

    /**
     * Проверяет корректность преобразования Product в ProductDTO
     */
    @Test
    void toDto_fromProduct() {
        ProductMapper productMapper = new ProductMapper();
        Product product = new Product();
        product.setName("Футболка Puma");
        product.setDescription("Комфортная");
        product.setCategory("CLOTHES");
        product.setCost(2799);

        ProductDTO dto = productMapper.toDto(product);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(product.getName(), dto.getName());
        Assertions.assertEquals(product.getDescription(), dto.getDescription());
        Assertions.assertEquals(product.getCategory(), dto.getCategory());
        Assertions.assertEquals(product.getCost(), dto.getCost());
    }
}