package ru.project.buySellStore.mapper;

import org.springframework.stereotype.Component;
import ru.project.buySellStore.dto.productView.*;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;

/**
 * Маппер для преобразования между Product товара и сущностью
 */
@Component
public class ProductMapper {

    /**
     * Преобразование товара в DTO для конкретной роли пользователя
     */
    public BaseProductDTO toDtoByRole(Product product, Role role) {
        return switch (role) {
            case SUPPLIER -> toProductSupplierDTO(product);
            case SELLER -> toProductSellerDTO(product);
            case BUYER -> toProductBuyerDTO(product);
        };
    }

    /**
     * Преобразование товара в DTO для пользователя с ролью {@link Role#SUPPLIER}
     */
    public ProductSupplierDTO toProductSupplierDTO(Product product) {
        return new ProductSupplierDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getSupplierCost()
        );
    }

    /**
     * Преобразование товара в DTO для пользователя с ролью {@link Role#SELLER}
     */
    public ProductSellerDTO toProductSellerDTO(Product product) {
        return new ProductSellerDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getSellerCost()
        );
    }

    /**
     * Преобразование товара в DTO для пользователя с ролью {@link Role#BUYER}
     */
    public ProductBuyerDTO toProductBuyerDTO(Product product) {
        return new ProductBuyerDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getSellerCost()
        );
    }
}
