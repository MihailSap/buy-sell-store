package ru.project.buySellStore.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
import ru.project.buySellStore.exception.productEx.*;
import ru.project.buySellStore.model.Product;
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
    public Product save(ProductDTO productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setSupplierCost(productDto.getSupplierCost());
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
    public void update(Long id, ProductSellerUpdateDTO productSellerUpdateDTO,
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
    public void assignSeller(Long productId, Long sellerId){
        Product product = findById(productId);
        User seller = userServiceImpl.getUserById(sellerId);
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