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
     * Преобразование товара из DTO в сущность
     */
    public Product toEntity(ProductDTO productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setCost(productDto.getCost());
        return product;
    }

    /**
     * Преобразование обновленного товара из DTO в сущность
     */
    public Product toEntity(ProductUpdateDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCost(dto.getCost());
        return product;
    }

    /**
     * Преобразование товара из сущности в DTO
     */
    public ProductDTO toDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(product.getCategory());
        productDTO.setCost(product.getCost());
        return productDTO;
    }
}
