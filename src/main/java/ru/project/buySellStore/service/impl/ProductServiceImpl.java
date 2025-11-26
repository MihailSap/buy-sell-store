package ru.project.buySellStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductUpdateDTO;
import ru.project.buySellStore.exception.productEx.ProductArchiveException;
import ru.project.buySellStore.exception.productEx.ProductNotFoundException;
import ru.project.buySellStore.exception.productEx.ProductRestoreException;
import ru.project.buySellStore.model.Product;
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
    public Product findById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void update(Long id, ProductUpdateDTO updatedProductDto) throws ProductNotFoundException {
        Product product = findById(id);
        product.setName(updatedProductDto.getName());
        product.setDescription(updatedProductDto.getDescription());
        product.setCost(updatedProductDto.getCost());

        productRepository.save(product);
    }

    @Override
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
}