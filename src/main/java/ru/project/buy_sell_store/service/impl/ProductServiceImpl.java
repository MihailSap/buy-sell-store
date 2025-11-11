package ru.project.buy_sell_store.service.impl;

import org.springframework.stereotype.Service;
import ru.project.buy_sell_store.dto.ProductDTO;
import ru.project.buy_sell_store.dto.ProductUpdateDTO;
import ru.project.buy_sell_store.exception.product.ProductArchiveException;
import ru.project.buy_sell_store.exception.product.ProductNotFoundException;
import ru.project.buy_sell_store.exception.product.ProductRestoreException;
import ru.project.buy_sell_store.model.Product;
import ru.project.buy_sell_store.repository.ProductRepository;
import ru.project.buy_sell_store.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Севрис для управление сущности Товара
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Создание экземпляра с внедрением нужных зависимостей
     * @param productRepository репозиторий для работы с сущностью Товара
     */
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(ProductDTO productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setCost(productDto.getCost());
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
    public void update(Long id, ProductUpdateDTO updatedProductDto) {
        Product product = findById(id);
        product.setName(updatedProductDto.getName());
        product.setDescription(updatedProductDto.getDescription());
        product.setCost(updatedProductDto.getCost());

        productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
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
}