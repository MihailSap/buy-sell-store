package ru.project.buySellStore.dto.productView;

/**
 * DTO для отображения продукта продавцу
 */
public class ProductSellerDTO extends BaseProductDTO {

    private final Integer sellerCost;

    /**
     * Конструктор, создающий DTO из сущности Product
     */
    public ProductSellerDTO(Long id, String name, String description, String category, Integer sellerCost) {
        super(id, name, description, category);
        this.sellerCost = sellerCost;
    }

    /**
     * Получить стоимость продукта для продавца
     */
    public Integer getSellerCost() {
        return sellerCost;
    }
}
