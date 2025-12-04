package ru.project.buySellStore.dto.productView;

/**
 * Базовый класс для DTO товара
 */
public abstract class BaseProductDTO {

    private final Long id;

    private final String name;

    private final String description;

    private final String category;

    /**
     * Конструктор для базового ProductDTO
     */
    public BaseProductDTO(Long id, String name, String description, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    /**
     * Получение id данного DTO продукта
     */
    public Long getId() {
        return id;
    }

    /**
     * Получение названия данного DTO продукта
     */
    public String getName() {
        return name;
    }

    /**
     * Получение описания данного DTO продукта
     */
    public String getDescription() {
        return description;
    }

    /**
     * Получение категории данного DTO продукта
     */
    public String getCategory() {
        return category;
    }
}
