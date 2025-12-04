package ru.project.buySellStore.mapper;

import org.springframework.stereotype.Component;
import ru.project.buySellStore.dto.productView.ProductBuyerDTO;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.productView.ProductSellerDTO;
import ru.project.buySellStore.dto.productView.ProductSupplierDTO;
import ru.project.buySellStore.dto.productView.ProductViewDTO;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;

/**
 * Маппер для преобразования между Product товара и сущностью
 */
@Component
public class ProductMapper {

    /**
     * Преобразование товара из сущности в DTO
     * <p>Нужен для создания и обновления товара</>
     */
    public ProductDTO toDto(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getSupplierCost()
        );
    }

    /**
     * Преобразование товара в DTO для конкретной роли пользователя
     */
    public ProductViewDTO toDtoByRole(Product product, Role role) {
        return switch (role) {
            case SUPPLIER -> new ProductSupplierDTO(product);
            case SELLER -> new ProductSellerDTO(product);
            case BUYER -> new ProductBuyerDTO(product);
        };
    }
}
