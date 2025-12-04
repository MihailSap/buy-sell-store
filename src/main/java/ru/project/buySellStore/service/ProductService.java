package ru.project.buySellStore.service;

import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
import ru.project.buySellStore.dto.ProductSupplierUpdateDTO;
import ru.project.buySellStore.exception.productEx.*;
import ru.project.buySellStore.exception.userEx.UserNotSuitableRoleException;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.User;

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
     * <p>Пользователь получает только свои неархивные товары</>
     */
    List<Product> findAll(User user);

    /**
     * Получить товар по id
     * <p>
     * Нужен для внутренней работы приложения
     * @throws ProductNotFoundException если товара с указанным id не существует
     */
    Product findById(Long id) throws ProductNotFoundException;

    /**
     * Получить товар по id
     * <p>
     * Нужен, чтобы пользователь получил только свои продукты
     * @throws ProductNotFoundException если товара с указанным id не существует
     */
    Product findById(Long id, User user) throws ProductNotFoundException;

    /**
     * Удалить товар по id
     * @throws ProductNotFoundException если товара с указанным id не существует
     */
    void delete(Long id) throws ProductNotFoundException;

    /**
     * Архивировать товар по id
     * @throws ProductArchiveException при попытке архивировать товар, который уже находится в архиве
     */
    void archive(Long id) throws ProductNotFoundException, ProductArchiveException;

    /**
     * Восстановить из архива по id
     * @throws ProductRestoreException при попытке удалить из архива товар, которого там нет
     */
    void restore(Long id) throws ProductNotFoundException, ProductRestoreException;

    /**
     * Назначить продавца на товар
     */
    void assignSeller(Product product, User seller) throws UserNotSuitableRoleException;

    /**
     * Покупка товара
     */
    void buyProduct(Long productId, User buyer)
      throws ProductArchiveException, ProductWithoutSellerException, ProductAlreadyBoughtException, ProductNotFoundException;

    /**
     * Получить товары по продавцу, категории и временному промежутку
     * <p>Если категория равна ALL - findBySellerAndBoughtDateBetween
     */
    List<Product> findBySellerAndCategoryAndBoughtDateBetween(String category,
                                                             User seller, String period);

    /**
     * Получить товары по поставщику, категории и временному промежутку
     *
     * <p>Если категория равна ALL - findBySellerAndBoughtDateBetween
     */
    List<Product> findBySupplierAndCategoryAndBoughtDateBetween(String category,
                                                                User supplier, String period);
}
