package ru.project.buy_sell_store.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buy_sell_store.model.Product;
import ru.project.buy_sell_store.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

/**
 * Тесты для проверки функционала ProductService
 */
@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    /**
     * Подготовка тестовых данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("Футболка Nike");
        product.setDescription("Отличная");
        product.setCategory("CLOTHES");
        product.setCost(2499);
    }

    /**
     * Проверяет сохранение товара в базу данных
     *
     * Ожидается, что после вызова метода товар будет сохранен
     * и у него будет индификатор
     */
    @Test
    void saveTest() {
        Product savedProduct = productService.save(product);

        Assertions.assertNotNull(savedProduct.getId());
        Assertions.assertEquals(product.getName(), savedProduct.getName());
    }

    /**
     * Проверяет корректность получения товара по id из базы данных
     *
     * Ожидается, что сохранненый товар можно найти
     */
    @Test
    void findByIdTest() {
        Product savedProduct = productService.save(product);

        Product foundProduct = productService.findById(savedProduct.getId());
        Assertions.assertEquals(savedProduct.getId(), foundProduct.getId());
    }

    /**
     * Проверяет корректность получения всех товаров из базы данных
     *
     * После добавления двух товаров ожидается, что
     * вернётся непустой список с сохраннеными товарами
     */
    @Test
    void findAllTest() {
        Product newProduct = new Product();
        newProduct.setName("Футболка с длинным рукавом");
        newProduct.setDescription("Удобная");
        newProduct.setCategory("CLOTHES");
        newProduct.setCost(3000);

        productService.save(product);
        productService.save(newProduct);

        List<Product> products = productService.findAll();

        Assertions.assertFalse(products.isEmpty());
        Assertions.assertTrue(
                products.stream().anyMatch(p -> p.getName().equals("Футболка Nike"))
        );
        Assertions.assertTrue(
                products.stream().anyMatch(
                        p -> p.getName().equals("Футболка с длинным рукавом"))
        );
    }

    /**
     * Проверяет корректность обновления данных товара из базы данных
     *
     * Ожидается, что товар должен иметь обновленные поля
     */
    @Test
    void updateTest() {
        Product savedProduct = productService.save(product);

        Product updatedProduct = new Product();
        updatedProduct.setName("Футболка Adidas");
        updatedProduct.setDescription("Отличная, удобная");
        updatedProduct.setCost(2799);

        productService.update(savedProduct.getId(), updatedProduct);

        Product foundProduct = productService.findById(savedProduct.getId());

        Assertions.assertEquals(savedProduct.getId(), foundProduct.getId());
        Assertions.assertEquals("Футболка Adidas", foundProduct.getName());
    }

    /**
     * Проверяет корректность удаления товара из базы данных
     *
     * Ожидается, что при попытке получить товар по id после удаления
     * выбросится исключение
     */
    @Test
    void deleteTest() {
        Product savedProduct = productService.save(product);
        productService.delete(savedProduct.getId());
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
            productService.findById(savedProduct.getId());
        });
        Assertions.assertEquals("Товар не найден", ex.getMessage());
    }

    /**
     * Проверяет процесс архивирования товара
     *
     * Ожидается, значение поля archived станет true
     */
    @Test
    void archiveTest() {
        Product savedProduct = productService.save(product);
        productService.archive(savedProduct.getId());

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        Assertions.assertTrue(foundProduct.get().isArchived());
    }

    /**
     * Проверяет восстановление товара из архива
     *
     * Ожидается, значение поля archived станет false
     */
    @Test
    void restoreTest() {
        Product savedProduct = productService.save(product);
        productService.archive(savedProduct.getId());
        productService.restore(savedProduct.getId());

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        Assertions.assertFalse(foundProduct.get().isArchived());
    }
}