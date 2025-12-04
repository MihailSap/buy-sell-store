package ru.project.buySellStore.dto.productView;

import ru.project.buySellStore.model.Product;

/**
 * DTO для отображения продукта поставщику
 */
public class ProductSupplierDTO implements ProductViewDTO {
    private final Long id;
    private final String name;
    private final String description;
    private final String category;
    private final Integer supplierCost;

    /**
     * Конструктор, создающий DTO из сущности Product
     */
    public ProductSupplierDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.supplierCost = product.getSupplierCost();
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
     * Получить стоимость продукта для поставщика
     */
    public Integer getSupplierCost() {
        return supplierCost;
    }
}
