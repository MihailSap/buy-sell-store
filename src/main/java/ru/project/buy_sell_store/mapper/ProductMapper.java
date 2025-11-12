package ru.project.buy_sell_store.mapper;

import org.springframework.stereotype.Component;
import ru.project.buy_sell_store.dto.ProductDTO;
import ru.project.buy_sell_store.dto.ProductUpdateDTO;
import ru.project.buy_sell_store.model.Product;

/**
 * Маппер для преобразования между Product товара и сущностью
 */
@Component
public class ProductMapper {

    /**
     * Преобразование товара из сущности в DTO
     */
    public ProductDTO toDto(Product product) {
        return new ProductDTO(
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getCost()
        );
    }
}
