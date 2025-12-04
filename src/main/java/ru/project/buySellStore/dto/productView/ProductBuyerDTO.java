package ru.project.buySellStore.dto.productView;

import ru.project.buySellStore.model.Product;

/**
 * DTO для отображения продукта покупателю
 */
public class ProductBuyerDTO implements ProductViewDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final String category;
    private final Integer finalCost;

    /**
     * Конструктор, создающий DTO из сущности Product
     */
    public ProductBuyerDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.finalCost = product.getSellerCost();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    /**
     * Получить конечную стоимость продукта для покупателя
     */
    public Integer getFinalCost() {
        return finalCost;
    }
}
