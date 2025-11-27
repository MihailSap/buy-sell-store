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
     * Если товара с таким id не существует, выбрасывается {@link ProductNotFoundException}
     */
    Product findById(Long id) throws ProductNotFoundException;

    /**
     * Удалить товар по id
     * Если товара с таким id не существует, выбрасывается {@link ProductNotFoundException}
     */
    void delete(Long id) throws ProductNotFoundException;

    /**
     * Архивировать товар по id
     * При попытке архивировать товар,
     * который уже находится в архиве, выбрасывается {@link ProductArchiveException}
     */
    void archive(Long id) throws ProductNotFoundException, ProductArchiveException;

    /**
     * Восстановить из архива по id
     * При попытке удалить из архива товар,
     * которого там нет, выбрасывается {@link ProductRestoreException}
     */
    void restore(Long id) throws ProductNotFoundException, ProductRestoreException;
}
