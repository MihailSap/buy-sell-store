package ru.project.buySellStore.service;

import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductUpdateDTO;
import ru.project.buySellStore.exception.productEx.ProductArchiveException;
import ru.project.buySellStore.exception.productEx.ProductNotFoundException;
import ru.project.buySellStore.exception.productEx.ProductRestoreException;
import ru.project.buySellStore.model.Product;

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
    Product findById(Long id) throws ProductNotFoundException;

    /**
     * Удалить товар по id
     */
    void delete(Long id) throws ProductNotFoundException;

    /**
     * Архивировать товар по id
     */
    void archive(Long id) throws ProductNotFoundException, ProductArchiveException;

    /**
     * Восстановить из архива по id
     */
    void restore(Long id) throws ProductNotFoundException, ProductRestoreException;
}
