package ru.project.buy_sell_store.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для обновления товара
 */
public class ProductUpdateDTO {

    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 30, message = "Название не должно превышать 30 символов")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-я0-9_\\- ]+$",
            message = "Название может содержать только буквы, цифры, дефис и нижнее подчёркивание"
    )
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Стоимость обязательна")
    @Positive(message = "Стоимость должна быть больше нуля")
    private Integer cost;

    /**
     * Получить имя
     */
    public String getName() {
        return name;
    }

    /**
     * Установить имя
     */
    public void setName(String name) {
        this.name = name;
    }

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
    public Integer getCost() {
        return cost;
    }

    /**
     * Установить стоимость
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
