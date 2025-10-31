package ru.project.buy_sell_store.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Сохранить товар в базу данных
     */
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Получить все товары из базы данных, исключая те, которые в архиве
     *
     */
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .filter(product -> !product.isArchived()).collect(Collectors.toList());
    }

    /**
     * Получить товар из базы данных по id
     */
    public Product findById(Long id) {
        return productRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Товар не найден"));
    }

    /**
     * Обновить товар из базы данных по id
     */
    @Transactional
    public void update(Long id, Product updatedProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setCost(updatedProduct.getCost());

        productRepository.save(product);
    }

    /**
     * Удалить товар из базы данных по id
     */
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Архивировать товар из базы данных по id
     */
    @Transactional
    public void archive(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if(product.isArchived()) {
            throw new RuntimeException("Товар находится уже в архиве");
        }

        product.setArchived(true);
        productRepository.save(product);
    }

    /**
     * Восстановить из архива из базы данных по id
     */
    @Transactional
    public void restore(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (!product.isArchived()) {
            throw new RuntimeException("Товар уже доступен и не находится в архиве");
        }

        product.setArchived(false);
        productRepository.save(product);
    }
}
