package ru.project.buySellStore.mapper;

import org.springframework.stereotype.Component;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.model.Product;

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
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getCost()
        );
    }
}
