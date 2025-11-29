package ru.project.buySellStore.dto;

import jakarta.validation.constraints.*;

/**
 * DTO товара для передачи данных между клиентом и сервером
 */
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 30, message = "Название не должно превышать 30 символов")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-я0-9_\\- ]+$",
            message = "Название может содержать только буквы, цифры, дефис и нижнее подчёркивание"
    )
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotBlank(message = "Категория не может быть пустой")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-я ]+$",
            message = "Категория может содержать только буквы"
    )
    private String category;

    @NotNull(message = "Стоимость обязательна")
    @Positive(message = "Стоимость должна быть больше нуля")
    private Integer supplierCost;

    /**
     * Конструктор для создания продукта
     * @param name название
     * @param description описание
     * @param category категория
     * @param supplierCost цена
     */
    public ProductDTO(String name, String description, String category, Integer supplierCost) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.supplierCost = supplierCost;
    }

    /**
     * Конструктор с id и всем полями для получения информации
     * о товаре
     */
    public ProductDTO(Long id, String name, String description, String category, Integer cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.cost = cost;
    }

    /**
     * Установить id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить id
     */
    public Long getId() {
        return id;
    }

    /**
     * Создание экземпляра без полей
     */
    public ProductDTO() {
    }

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
     * Получить категорию
     */
    public String getCategory() {
        return category;
    }

    /**
     * Установить категорию
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Получить стоимость
     */
    public Integer getSupplierCost() {
        return supplierCost;
    }

    /**
     * Установить стоимость
     */
    public void setSupplierCost(Integer supplierCost) {
        this.supplierCost = supplierCost;
    }
}
