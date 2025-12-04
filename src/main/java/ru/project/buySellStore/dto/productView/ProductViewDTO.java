package ru.project.buySellStore.dto.productView;

/**
 * Интерфейс для представления общих свойств продукта
 */
public interface ProductViewDTO {

    /**
     * Получить идентификатор продукта
     */
    Long getId();

    /**
     * Получить название продукта
     */
    String getName();

    /**
     * Получить описание продукта
     */
    String getDescription();

    /**
     * Получить категорию продукта
     */
    String getCategory();
}
