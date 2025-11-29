package ru.project.buySellStore.service;

import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
import ru.project.buySellStore.dto.ProductSupplierUpdateDTO;
import ru.project.buySellStore.exception.productEx.ProductArchiveException;
import ru.project.buySellStore.exception.productEx.ProductNotFoundException;
import ru.project.buySellStore.exception.productEx.ProductRestoreException;
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
     * Если товара с таким id не существует, выбрасывается {@link ProductNotFoundException}
     */
    Product findById(Long id) throws ProductNotFoundException;

    /**
     * Продавец обновляет товар по id
     */
    void updateBySeller(Long id, ProductSellerUpdateDTO productSellerUpdateDTO,
                       User seller);


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

    /**
     * Назначить продавца на товар
     */
    void assignSeller(Product product, User seller);

    /**
     * Покупка товара
     */
    void buyProduct(Long id, User buyer);

    /**
     * Поставщик меняет товар по id. Он имеет эту возможность до того как
     * назначил продавца
     */
    void updateBySupplier(Long id, ProductSupplierUpdateDTO productSupplierUpdateDTO,
                          User supplier);
}
