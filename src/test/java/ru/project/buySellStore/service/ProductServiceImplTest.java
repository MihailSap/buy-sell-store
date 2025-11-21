package ru.project.buySellStore.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
import ru.project.buySellStore.exception.productEx.*;
import ru.project.buySellStore.exception.userEx.UserNotSuitableRoleException;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.repository.ProductRepository;
import ru.project.buySellStore.service.impl.ProductServiceImpl;
import ru.project.buySellStore.service.impl.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Тесты для проверки функционала {@link ProductServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private User buyer;

    private Product product;

    /**
     * Явное создание сущностей для тестов
     */
    @BeforeEach
    void setUp(){
        buyer = new User();
        buyer.setRole(Role.BUYER);

        product = new Product();
        product.setId(1L);
    }

    /**
     * Проверяет нахождения товара, которого не существует
     *
     * Ожидается, что сохраненный товар можно найти
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

    /**
     * Проверяет назначение продавца на товар
     */
    @Test
    void testAssignSeller(){
        User seller = new User();
        seller.setRole(Role.SELLER);

        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        productService.assignSeller(product, seller);

        Assertions.assertEquals(seller, product.getSeller());
        Mockito.verify(productRepository).save(product);
    }

    /**
     * Проверяет попытку назначить продавцом пользователя без роли SELLER
     */
    @Test
    void testAssignSellerNotSeller(){
        User seller = new User();
        seller.setRole(Role.SUPPLIER);

        UserNotSuitableRoleException ex = Assertions.assertThrows(
                UserNotSuitableRoleException.class,
                () -> productService.assignSeller(product, seller)
        );

        Assertions.assertEquals(
                "Продавцом можно назначить только пользователя с ролью SELLER",
                ex.getMessage()
        );
    }

    /**
     * Проверяет успешное обновление товара продавцом
     */
    @Test
    void testUpdateSeller() {
        User seller = new User();
        seller.setRole(Role.SELLER);

        product.setSeller(seller);

        ProductSellerUpdateDTO dto = new ProductSellerUpdateDTO();
        dto.setDescription("New description");
        dto.setSellerCost(1000);

        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        productService.updateBySeller(product, dto, seller);

        Assertions.assertEquals("New description", product.getDescription());
        Assertions.assertEquals(1000, product.getSellerCost());
        Mockito.verify(productRepository)
                .save(product);
    }

    /**
     * Проверяется успешная покупка
     */
    @Test
    void testBuy(){
        product.setSeller(new User());

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        productService.buyProduct(1L, buyer);

        Assertions.assertEquals(buyer, product.getBuyer());
        Mockito.verify(productRepository, Mockito.times(1))
                .save(product);
    }

    /**
     * Проверяется попытка купить архивированный товар
     */
    @Test
    void testBuyArchivedProduct(){
        product.setArchived(true);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductArchiveException ex = Assertions.assertThrows(
                ProductArchiveException.class,
                () -> productService.buyProduct(1L, buyer)
        );

        Assertions.assertEquals("Товар с id = 1 находится уже в архиве", ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * Проверяется попытка купить товар без продавца
     */
    @Test
    void testBuyProductWithoutSeller(){
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductWithoutSellerException ex = Assertions.assertThrows(
                ProductWithoutSellerException.class,
                () -> productService.buyProduct(1L, buyer)
        );

        Assertions.assertEquals(
                "Товар с id = 1 не имеет назначенного продавца", ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * Проверяется попытка купить уже купленный товар
     */
    @Test
    void testBuyAlreadyBoughtProduct(){
        product.setSeller(new User());
        product.setBuyer(new User());

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductAlreadyBoughtException ex = Assertions.assertThrows(
                ProductAlreadyBoughtException.class,
                () -> productService.buyProduct(1L, buyer)
        );

        Assertions.assertEquals("Товар с id = 1 уже куплен", ex.getMessage());

        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }
}