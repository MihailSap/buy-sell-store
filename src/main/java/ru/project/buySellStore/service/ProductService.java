package ru.project.buySellStore.service;

import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
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
    Product save(ProductDTO productDto, User user);

    /**
     * Получить все товары
     */
    List<Product> findAll();

    /**
     * Получить товар по id
     */
    Product findById(Long id);

    /**
     * Продавец обновляет товар по id
     */
    void update(Long id, ProductSellerUpdateDTO productSellerUpdateDTO,
                       User seller);

    /**
     * Удалить товар по id
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

    /**
     * Назначить продавца на товар
     */
    void assignSeller(Product product, User seller);

    /**
     * Покупка товара
     */
    void buyProduct(Long id, User buyer);
}
