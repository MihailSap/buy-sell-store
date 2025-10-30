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
     * Получить описание
     */
    public String getDescription() {
        return description;
    }

    /**
     * Получить стоимость
     */
    public Integer getCost() {
        return cost;
    }
}
