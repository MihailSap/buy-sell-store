package ru.project.buy_sell_store.service;

import ru.project.buy_sell_store.model.Product;

import java.util.List;

/**
 * Сервисный интерфейс для работы с сущностью Товара
 */
public interface ProductService {

    /**
     * Сохранить товар
     */
    Product save(Product product);

    /**
     * Получить все товары
     */
    List<Product> findAll();

    /**
     * Получить товар по id
     */
    Product findById(Long id);

    /**
     * Обновить товар по id
     */
    void update(Long id, Product updatedProduct);

    /**
     * Удалить товар  по id
     */
    void delete(Long id);

    /**
     * Архивировать товар по id
     */
    void archive(Long id);

    /**
     * Восстановить из архива по id
     */
    void restore(Long id);
}

