package ru.project.buy_sell_store.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buy_sell_store.exception.productEx.ProductArchiveException;
import ru.project.buy_sell_store.exception.productEx.ProductNotFoundException;
import ru.project.buy_sell_store.exception.productEx.ProductRestoreException;
import ru.project.buy_sell_store.model.Product;
import ru.project.buy_sell_store.repository.ProductRepository;
import ru.project.buy_sell_store.service.impl.ProductServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Тесты для проверки функционала ProductServiceImpl
 */
@SpringBootTest
@Transactional
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    /**
     * Проверяет нахождения товара, которого не существует
     *
     * Ожидается, что сохранненый товар можно найти
     */
    @Test
    void findByIdTest() {
        Mockito.when(productRepository.findById(1000L)).thenReturn(Optional.empty());

        ProductNotFoundException ex = Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.findById(1000L));

        Assertions.assertEquals("Товар с id = 1000 не найден", ex.getMessage());
    }

    /**
     * Проверяет корректность получения всех товаров из базы данных
     *
     * Ожидается, что мы получим все товары, которые не в архиве
     */
    @Test
    void findAllTest() {
        Product product = new Product();
        product.setId(1L);

        Product productArchive = new Product();
        productArchive.setId(2L);
        productArchive.setArchived(true);

        Product newProduct = new Product();
        productArchive.setId(3L);

        Mockito.when(productRepository.findAll())
                .thenReturn(List.of(product, productArchive, newProduct));

        Set<Product> products = productService.findAll().stream().collect(Collectors.toSet());

        Assertions.assertEquals(2, products.size());
        Assertions.assertTrue(products.contains(product));
        Assertions.assertTrue(products.contains(newProduct));
        Assertions.assertFalse(products.contains(productArchive));

        Mockito.verify(productRepository).findAll();
    }

    /**
     * Проверяет удаление товара, которого не существует
     *
     * Ожидается, что выпадет исключение ProductNotFoundException
     */
    @Test
    void deleteTest() {
        Mockito.when(productRepository.findById(1000L))
                .thenReturn(Optional.empty());

        ProductNotFoundException ex = Assertions.assertThrows(
                ProductNotFoundException.class,
                () -> productService.delete(1000L)
        );

        Assertions.assertEquals(
                "Товар с id = 1000 не найден",
                ex.getMessage()
        );

        Mockito.verify(productRepository, Mockito.never()).delete(Mockito.any());
    }

    /**
     * Проверяет процесс архивирования товара
     *
     * Ожидается, значение поля archived станет true
     */
    @Test
    void archiveTest() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        productService.archive(1L);

        Assertions.assertTrue(product.isArchived());

        Mockito.verify(productRepository)
                .save(product);
    }

    /**
     * Проверяет процесс архивирования товара, когда товар был уже в архиве
     *
     * Ожидается, что выпадет исключение ProductArchiveException
     */
    @Test
    void archiveArchivedProductTest() {
        Product product = new Product();
        product.setId(1L);
        product.setArchived(true);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductArchiveException ex = Assertions.assertThrows(
                ProductArchiveException.class,
                () -> productService.archive(1L)
        );

        Assertions.assertEquals(
                "Товар с id = 1 находится уже в архиве",
                ex.getMessage()
        );

        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * Проверяет восстановление товара из архива
     *
     * Ожидается, значение поля archived станет false
     */
    @Test
    void restoreTest() {
        Product product = new Product();
        product.setId(1L);
        product.setArchived(true);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        productService.restore(1L);

        Assertions.assertFalse(product.isArchived());

        Mockito.verify(productRepository)
                .save(product);
    }

    /**
     * Проверяет восстановление товара из архива, когда товар и так не в архиве
     *
     * Ожидается, что выпадет исключение ProductRestoreException
     */
    @Test
    void restoreNoArchivedProductTest() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductRestoreException ex = Assertions.assertThrows(
                ProductRestoreException.class,
                () -> productService.restore(1L)
        );

        Assertions.assertEquals(
                "Товар c id = 1 уже доступен и не находится в архиве",
                ex.getMessage()
        );

        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }
}