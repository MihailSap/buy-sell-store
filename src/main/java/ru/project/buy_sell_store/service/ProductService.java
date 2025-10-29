package ru.project.buy_sell_store.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buy_sell_store.model.Product;
import ru.project.buy_sell_store.repository.ProductRepository;

import java.util.List;

/**
 * Севрис для управление сущности Товара
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
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
     * Получить все товары из базы данных
     */
    public List<Product> findAll() {
        return productRepository.findAll();
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
     * Удалить товар по id
     */
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Архивировать товар по id
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
     * Восстановить из архива по id
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
