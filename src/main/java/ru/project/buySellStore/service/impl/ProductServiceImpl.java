package ru.project.buySellStore.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
import ru.project.buySellStore.dto.ProductSupplierUpdateDTO;
import ru.project.buySellStore.exception.productEx.*;
import ru.project.buySellStore.exception.userEx.UserNotSuitableRoleException;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.repository.ProductRepository;
import ru.project.buySellStore.service.ProductService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Севрис для управление сущности Товара
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final UserServiceImpl userServiceImpl;

    /**
     * Создание экземпляра с внедрением нужных зависимостей
     * @param productRepository репозиторий для работы с сущностью Товара
     */
    public ProductServiceImpl(ProductRepository productRepository, UserServiceImpl userServiceImpl) {
        this.productRepository = productRepository;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Product save(ProductDTO productDto, User supplier) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setSupplierCost(productDto.getSupplierCost());
        product.setSupplier(supplier);
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .filter(product -> !product.isArchived()).collect(Collectors.toList());
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void updateBySeller(Long id, ProductSellerUpdateDTO productSellerUpdateDTO,
                       User seller) {
        Product product = findById(id);

        if (product.getSeller() == null || !product.getSeller().equals(seller)) {
            throw new AccessDeniedException("Этот товар не назначен вам!");
        }

        product.setDescription(productSellerUpdateDTO.getDescription());
        product.setSellerCost(productSellerUpdateDTO.getSellerCost());

        productRepository.save(product);
    }

    @Override
    public void updateBySupplier(Long id, ProductSupplierUpdateDTO productSupplierUpdateDTO
            ,User supplier) {
        Product product = findById(id);

        if (!product.getSupplier().equals(supplier)) {
            throw new AccessDeniedException("Поставщик может изменять только свои товары!");
        }

        if (product.getSeller() != null) {
            throw new AccessDeniedException(
                    "Поставщик не может редактировать товар после назначения продавца!"
            );
        }

        product.setName(productSupplierUpdateDTO.getName());
        product.setDescription(productSupplierUpdateDTO.getDescription());
        product.setSupplierCost(productSupplierUpdateDTO.getSupplierCost());

        productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        productRepository.deleteById(id);
    }

    @Override
    public void archive(Long id) {
        Product product = findById(id);

        if(product.isArchived()) {
            throw new ProductArchiveException(id);
        }

        product.setArchived(true);
        productRepository.save(product);
    }

    @Override
    public void restore(Long id) {
        Product product = findById(id);

        if (!product.isArchived()) {
            throw new ProductRestoreException(id);
        }

        product.setArchived(false);
        productRepository.save(product);
    }

    @Override
    public void assignSeller(Product product, User seller){
        if(!seller.getRole().equals(Role.SELLER)) {
            throw new UserNotSuitableRoleException(
                    "Продавцом можно назначить только пользователя с ролью SELLER");
        }

        product.setSeller(seller);
        productRepository.save(product);
    }

    @Override
    public void buyProduct(Long id, User buyer) {
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