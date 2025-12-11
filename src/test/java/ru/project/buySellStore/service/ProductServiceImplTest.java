package ru.project.buySellStore.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.project.buySellStore.exception.productEx.*;
import ru.project.buySellStore.exception.userEx.UserNotSuitableRoleException;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.repository.ProductRepository;
import ru.project.buySellStore.service.impl.ProductServiceImpl;

import java.util.List;
import java.util.Optional;

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
     * Тестирование метода findAll(User user) для роли SUPPLIER
     * <p>
     * Проверяется, что поставщик видит только свои активные товары,
     * архивные и чужие товары не возвращаются
     */
    @Test
    void testFindAllForSupplier() {
        User supplier = new User();
        supplier.setId(1L);
        supplier.setRole(Role.SUPPLIER);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setSupplier(supplier);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setSupplier(supplier);
        product2.setArchived(true);

        Product product3 = new Product();
        product3.setId(3L);
        product3.setSupplier(new User());

        Mockito.when(productRepository.findAll())
                .thenReturn(List.of(product1, product2, product3));

        List<Product> products = productService.findAll(supplier);

        Assertions.assertEquals(1, products.size());
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertFalse(products.contains(product2));
        Assertions.assertFalse(products.contains(product3));

        Mockito.verify(productRepository).findAll();
    }

    /**
     * Тестирование метода findAll(User user) для роли SELLER
     * <p>
     * Проверяется, что покупатель видит только свои активные покупки,
     * архивные и чужие покупки не возвращаются
     */
    @Test
    void testFindAllForSeller() {
        User seller = new User();
        seller.setId(10L);
        seller.setRole(Role.SELLER);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setSeller(seller);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setSeller(seller);
        product2.setArchived(true);

        Product product3 = new Product();
        product3.setId(3L);
        product3.setSeller(new User());

        Mockito.when(productRepository.findAll())
                .thenReturn(List.of(product1, product2, product3));

        List<Product> products = productService.findAll(seller);

        Assertions.assertEquals(1, products.size());
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertFalse(products.contains(product2));
        Assertions.assertFalse(products.contains(product3));

        Mockito.verify(productRepository).findAll();
    }

    /**
     * Тестирование метода findAll(User user) для роли BUYER
     * <p>
     * Проверяется, что покупатель видит только свои активные покупки,
     * архивные и чужие покупки не возвращаются
     */
    @Test
    void testFindAllForBuyer() {
        User buyer = new User();
        buyer.setId(20L);
        buyer.setRole(Role.BUYER);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setBuyer(buyer);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setBuyer(buyer);
        product2.setArchived(true);

        Product product3 = new Product();
        product3.setId(3L);
        product3.setBuyer(new User());

        Mockito.when(productRepository.findAll())
                .thenReturn(List.of(product1, product2, product3));

        List<Product> products = productService.findAll(buyer);

        Assertions.assertEquals(1, products.size());
        Assertions.assertTrue(products.contains(product1));
        Assertions.assertFalse(products.contains(product2));
        Assertions.assertFalse(products.contains(product3));

        Mockito.verify(productRepository).findAll();
    }

    /**
     * <b>Проверяет удаление несуществующего товара</b>
     * <p>Ожидается появление исключения с сообщением "Товар с id = 1000 не найден"</p>
     */
    @Test
    void testDeleteNonExistingProduct() {
        Mockito.when(productRepository.findById(1000L))
                .thenReturn(Optional.empty());

        ProductNotFoundException ex = Assertions.assertThrows(
                ProductNotFoundException.class, () -> productService.delete(1000L));

        Assertions.assertEquals("Товар с id = 1000 не найден",ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .delete(Mockito.any());
    }

    /**
     * <b>Проверяет процесс архивирования товара</b>
     * <p>Ожидается, значение поля archived станет true</p>
     */
    @Test
    void testArchive() throws ProductArchiveException, ProductNotFoundException {
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
     * <b>Проверяется архивирование несуществующего товара</b>
     * <p>Ожидается появление исключения с сообщением "Товар с id = 1 не найден"</p>
     */
    @Test
    void testArchiveNonExistingProduct() {
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        ProductNotFoundException ex = Assertions.assertThrows(
                ProductNotFoundException.class, () -> productService.archive(1L));

        Assertions.assertEquals("Товар с id = 1 не найден", ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет процесс архивирования архивированного ранее товара</b>
     * <p>Ожидается появление исключения с сообщением "Товар с id = 1 уже находится в архиве"</p>
     */
    @Test
    void testArchiveArchivedProduct() {
        product.setArchived(true);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductArchiveException ex = Assertions.assertThrows(
                ProductArchiveException.class, () -> productService.archive(1L));

        Assertions.assertEquals("Товар с id = 1 уже находится в архиве",ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any());
    }

    /**
     * <b>Проверяет восстановление товара из архива</b>
     * <p>Ожидается, значение поля {@code archived} станет {@code false}</p>
     */
    @Test
    void testRestore() throws ProductNotFoundException, ProductRestoreException {
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
     * <b>Проверяет восстановление из архива не архивированного товара</b>
     * <p>Ожидается появление исключения с сообщением "Товар c id = 1 уже доступен и не находится в архиве"</p>
     */
    @Test
    void testRestoreNonArchivedProduct() {
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductRestoreException ex = Assertions.assertThrows(
                ProductRestoreException.class, () -> productService.restore(1L));

        Assertions.assertEquals(
                "Товар c id = 1 уже доступен и не находится в архиве", ex.getMessage());

        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет восстановление несуществующего товара</b>
     * <p>Ожидается появление исключения с сообщением "Товар с id = 1 не найден"</p>
     */
    @Test
    void testRestoreNonExistingProduct() {
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        ProductNotFoundException ex = Assertions.assertThrows(
                ProductNotFoundException.class, () -> productService.restore(1L));

        Assertions.assertEquals("Товар с id = 1 не найден", ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет назначение продавца на товар</b>
     * <p>Ожидается, что у товара появится продавец</p>
     */
    @Test
    void testAssignSeller() throws UserNotSuitableRoleException {
        User seller = new User();
        seller.setRole(Role.SELLER);

        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        productService.assignSeller(product, seller);

        Assertions.assertEquals(seller, product.getSeller());
        Mockito.verify(productRepository)
                .save(product);
    }

    /**
     * <b>Проверяет назначение продавцом на товар пользователю без роли {@link Role#SELLER}</b>
     * <p>Ожидается появление исключения с сообщением "Продавцом можно назначить только пользователя с ролью SELLER"</p>
     */
    @Test
    void testAssignSellerWrongRole() {
        UserNotSuitableRoleException ex = Assertions.assertThrows(
                UserNotSuitableRoleException.class, () -> productService.assignSeller(product, buyer));

        Assertions.assertEquals(
                "Продавцом можно назначить только пользователя с ролью SELLER", ex.getMessage());

        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }


    /**
     * <b>Проверяет успешную покупку товара</b>
     * <p>Ожидается, что у товара появится покупатель</p>
     */
    @Test
    void testBuyProduct() throws Exception {
        Long productId = 1L;
        User seller = new User();
        seller.setRole(Role.SELLER);
        product.setSeller(seller);

        Mockito.when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        productService.buyProduct(productId, buyer);

        Assertions.assertEquals(buyer, product.getBuyer());
        Mockito.verify(productRepository)
                .save(product);
    }

    /**
     * <b>Проверяется покупка несуществующего товара</b>
     * <p>Ожидается появление исключения с сообщением "Товар с id = 1 не найден"</p>
     */
    @Test
    void testBuyNonExistingProduct() {
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        ProductNotFoundException ex = Assertions.assertThrows(
                ProductNotFoundException.class, () -> productService.buyProduct(1L, buyer));

        Assertions.assertEquals("Товар с id = 1 не найден", ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяется покупка архивированного товара</b>
     * <p>Ожидается появление исключения с сообщением "Товар с id = 1 уже находится в архиве"</p>
     */
    @Test
    void testBuyArchivedProduct() {
        product.setArchived(true);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductArchiveException ex = Assertions.assertThrows(
                ProductArchiveException.class, () -> productService.buyProduct(1L, buyer));

        Assertions.assertEquals("Товар с id = 1 уже находится в архиве", ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяется покупка товара без продавца</b>
     * <p>Ожидается появление исключения с сообщением "Товар с id = 1 не имеет назначенного продавца"</p>
     */
    @Test
    void testBuyProductWithoutSeller() {
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductWithoutSellerException ex = Assertions.assertThrows(
                ProductWithoutSellerException.class, () -> productService.buyProduct(1L, buyer));

        Assertions.assertEquals("Товар с id = 1 не имеет назначенного продавца", ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяется покупка уже купленного товара</b>
     * <p>Ожидается появление исключения с сообщением "Товар с id = 1 уже куплен"</p>
     */
    @Test
    void testBuyAlreadyBoughtProduct() {
        User seller = new User();
        seller.setRole(Role.SELLER);

        product.setSeller(seller);
        product.setBuyer(buyer);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductAlreadyBoughtException ex = Assertions.assertThrows(
                ProductAlreadyBoughtException.class,() -> productService.buyProduct(1L, buyer));

        Assertions.assertEquals("Товар с id = 1 уже куплен", ex.getMessage());
        Mockito.verify(productRepository, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет поиск существующего товара с доступом пользователя</b>
     * <p>Ожидается, что пользователь получить свой товар</p>
     */
    @Test
    void testFindExistingProductByIdWithUser() throws ProductNotFoundException {

        product.setBuyer(buyer);

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        Product found = productService.findById(1L, buyer);

        Assertions.assertEquals(product, found);
    }

    /**
     * <b>Проверяет попытку поиска существующего товара у другого пользователя</b>
     * <p>Ожидается ProductNotFoundException</p>
     */
    @Test
    void testFindExistingProductByIdWithoutAccess() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setRole(Role.BUYER);

        product.setBuyer(new User());

        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductNotFoundException ex = Assertions.assertThrows(
                ProductNotFoundException.class,
                () -> productService.findById(1L, otherUser)
        );

        Assertions.assertEquals("Товар с id = 1 не найден", ex.getMessage());
    }
}