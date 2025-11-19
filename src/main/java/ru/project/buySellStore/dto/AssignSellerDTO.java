package ru.project.buySellStore.dto;

/**
 * DTO для определения продавца товара
 */
public class AssignSellerDTO {

    /**
     * id продавца
     */
    private Long sellerId;

    /**
     * Конструктор
     */
    public AssignSellerDTO(Long sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * Определить id продавца
     */
    public Long getSellerId() {
        return sellerId;
    }
}
