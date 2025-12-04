package ru.project.buySellStore.dto.productView;

/**
 * DTO для отображения продукта поставщику
 */
public class ProductSupplierDTO extends BaseProductDTO {

    private final Integer supplierCost;

    /**
     * Конструктор, создающий DTO из сущности Product
     */
    public ProductSupplierDTO(Long id, String name, String description, String category, Integer supplierCost) {
        super(id, name, description, category);
        this.supplierCost = supplierCost;
    }

    /**
     * Получить стоимость продукта для поставщика
     */
    public Integer getSupplierCost() {
        return supplierCost;
    }
}
