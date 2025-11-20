package ru.project.buySellStore.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для обновления товара
 */
public class ProductSellerUpdateDTO {

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Стоимость обязательна")
    @Positive(message = "Стоимость должна быть больше нуля")
    private Integer sellerCost;

    /**
     * Получить описание
     */
    public String getDescription() {
        return description;
    }

    /**
     * Установить описание
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Получить стоимость
     */
    public Integer getSellerCost() {
        return sellerCost;
    }

    /**
     * Установить стоимость
     */
    public void setSellerCost(Integer sellerCost) {
        this.sellerCost = sellerCost;
    }
}
