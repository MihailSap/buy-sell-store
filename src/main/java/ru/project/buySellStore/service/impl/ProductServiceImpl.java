package ru.project.buySellStore.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
import ru.project.buySellStore.dto.ProductSupplierUpdateDTO;
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
import java.util.stream.Collectors;

/**
 * Сервис для управления сущностью товара
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Создание экземпляра с внедрением нужных зависимостей
     * @param productRepository репозиторий для работы с сущностью Товара
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserServiceImpl userServiceImpl) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .filter(product -> !product.isArchived()).collect(Collectors.toList());
    }

    @Override
    public Product findById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
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
    public void buyProduct(Long id, User buyer) throws ProductArchiveException,
            ProductWithoutSellerException, ProductAlreadyBoughtException,
            ProductNotFoundException {
        Product product = findById(id);

        if (product.isArchived()) {
            throw new ProductArchiveException(id);
        }

        if (product.getSeller() == null) {
            throw new ProductWithoutSellerException(id);
        }

        if (product.getBuyer() != null) {
            throw new ProductAlreadyBoughtException(id);
        }

        product.setBuyer(buyer);

        productRepository.save(product);
    }
}