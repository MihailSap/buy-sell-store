package ru.project.buySellStore.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.model.Product;


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
        product.setId(1L);
        product.setName("Футболка Puma");
        product.setDescription("Комфортная");
        product.setCategory("CLOTHES");
        product.setCost(2799);

        ProductDTO dto = productMapper.toDto(product);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(product.getId(), dto.getId());
        Assertions.assertEquals(product.getName(), dto.getName());
        Assertions.assertEquals(product.getDescription(), dto.getDescription());
        Assertions.assertEquals(product.getCategory(), dto.getCategory());
        Assertions.assertEquals(product.getCost(), dto.getCost());
    }
}