package ru.project.buySellStore.mapper;

import org.springframework.stereotype.Component;
import ru.project.buySellStore.dto.ViewProductDTO;
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
    public ViewProductDTO toDto(Product product, Role role) {

        return new ViewProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                switch (role) {
                    case SUPPLIER -> product.getSupplierCost();
                    case SELLER, BUYER -> product.getSellerCost();
                }
        );
    }
}