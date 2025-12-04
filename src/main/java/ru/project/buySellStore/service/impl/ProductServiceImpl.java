package ru.project.buySellStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.buySellStore.exception.productEx.*;
import ru.project.buySellStore.exception.userEx.UserNotSuitableRoleException;
import ru.project.buySellStore.exception.productEx.ProductArchiveException;
import ru.project.buySellStore.exception.productEx.ProductNotFoundException;
import ru.project.buySellStore.exception.productEx.ProductRestoreException;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.repository.ProductRepository;
import ru.project.buySellStore.service.ProductService;
import java.util.List;

/**
 * Сервис для управления товаром
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Создание экземпляра с внедрением нужных зависимостей
     * @param productRepository репозиторий для работы с сущностью Товара
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll(User user) {
        return productRepository.findAll().stream()
                .filter(p -> !p.isArchived())
                .filter(p -> hasAccess(p, user))
                .toList();
    }

    public Product findById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product findById(Long id, User user) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (!hasAccess(product, user) || product.isArchived()) {
            throw new ProductNotFoundException(id);
        }

        return product;
    }

    public void delete(Long id) throws ProductNotFoundException {
        try{
            Product product = findById(id);
            productRepository.delete(product);
        } catch (ProductNotFoundException e) {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public void archive(Long id) throws ProductNotFoundException, ProductArchiveException {
        Product product = findById(id);

        if(product.isArchived()) {
            throw new ProductArchiveException(id);
        }

        product.setArchived(true);
        productRepository.save(product);
    }

    @Override
    public void restore(Long id) throws ProductNotFoundException, ProductRestoreException {
        Product product = findById(id);

        if (!product.isArchived()) {
            throw new ProductRestoreException(id);
        }

        product.setArchived(false);
        productRepository.save(product);
    }

    @Override
    public void assignSeller(Product product, User seller) throws UserNotSuitableRoleException {
        if(!seller.getRole().equals(Role.SELLER)) {
            throw new UserNotSuitableRoleException(
                    "Продавцом можно назначить только пользователя с ролью SELLER");
        }

        product.setSeller(seller);
        productRepository.save(product);
    }

    @Override
    public void buyProduct(Long productId, User buyer) throws ProductArchiveException,
            ProductWithoutSellerException, ProductAlreadyBoughtException,
            ProductNotFoundException {
        Product product = findById(productId);

        if (product.isArchived()) {
            throw new ProductArchiveException(productId);
        }

        if (product.getSeller() == null) {
            throw new ProductWithoutSellerException(productId);
        }

        if (product.getBuyer() != null) {
            throw new ProductAlreadyBoughtException(productId);
        }

        product.setBuyer(buyer);

        productRepository.save(product);
    }

    /**
     * Проверка доступа к продукту для конкретного пользователя
     */
    private boolean hasAccess(Product product, User user) {
        return switch (user.getRole()) {
            case SUPPLIER -> product.getSupplier() != null && user.equals(product.getSupplier());
            case SELLER   -> product.getSeller() != null && user.equals(product.getSeller());
            case BUYER    -> product.getBuyer() != null && user.equals(product.getBuyer());
        };
    }
}