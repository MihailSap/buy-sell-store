package ru.project.buySellStore.dto.productView;

/**
 * DTO для отображения продукта покупателю
 */
public class ProductBuyerDTO extends BaseProductDTO{

    private final Integer finalCost;

    /**
     * Конструктор, создающий DTO из сущности Product
     */
    public ProductBuyerDTO(Long id, String name, String description, String category, Integer finalCost) {
        super(id, name, description, category);
        this.finalCost = finalCost;
    }

    /**
     * Получить конечную стоимость продукта для покупателя
     */
    public Integer getFinalCost() {
        return finalCost;
    }
}
