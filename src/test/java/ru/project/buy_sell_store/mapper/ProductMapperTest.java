package ru.project.buy_sell_store.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.buy_sell_store.dto.ProductDTO;
import ru.project.buy_sell_store.dto.ProductUpdateDTO;
import ru.project.buy_sell_store.model.Product;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для проверки маппера Товара
 */
class ProductMapperTest {

    private ProductMapper productMapper;

    /**
     * Создаёт новый экземпляр маппера перед выполнением каждого теста
     */
    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    /**
     * Проверяет корректность преобразования ProductDTO в Product
     */
    @Test
    void toEntity_fromProductDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Футболка Nike");
        dto.setDescription("Отличная");
        dto.setCategory("CLOTHES");
        dto.setCost(2499);

        Product product = productMapper.toEntity(dto);

        assertNotNull(product);
        assertEquals(dto.getName(), product.getName());
        assertEquals(dto.getDescription(), product.getDescription());
        assertEquals(dto.getCategory(), product.getCategory());
        assertEquals(dto.getCost(), product.getCost());
    }

    /**
     * Проверяет корректность преобразования ProductUpdateDTO в Product
     */
    @Test
    void toEntity_fromProductUpdateDTO() {
        ProductUpdateDTO dto = new ProductUpdateDTO();
        dto.setName("Футболка Adidas");
        dto.setDescription("Удобная");
        dto.setCost(2999);

        Product product = productMapper.toEntity(dto);

        assertNotNull(product);
        assertEquals(dto.getName(), product.getName());
        assertEquals(dto.getDescription(), product.getDescription());
        assertEquals(dto.getCost(), product.getCost());
    }

    /**
     * Проверяет корректность преобразования Product в ProductDTO
     */
    @Test
    void toDto_fromProduct() {
        Product product = new Product();
        product.setName("Футболка Puma");
        product.setDescription("Комфортная");
        product.setCategory("CLOTHES");
        product.setCost(2799);

        ProductDTO dto = productMapper.toDto(product);

        assertNotNull(dto);
        assertEquals(product.getName(), dto.getName());
        assertEquals(product.getDescription(), dto.getDescription());
        assertEquals(product.getCategory(), dto.getCategory());
        assertEquals(product.getCost(), dto.getCost());
    }
}