package ru.project.buySellStore.dto;

/**
 * DTO для отображения информации о товаре
 * Содержит базовую информацию, которая видна пользователю в зависимости от его роли
 *
 * @param id          уникальный идентификатор товара
 * @param name        название товара
 * @param description описание товара
 * @param category    категория товара
 * @param cost        стоимость товара для текущей роли пользователя
 */
public record ViewProductDTO(Long id,
                             String name,
                             String description,
                             String category,
                             Integer cost) {
}
