package ru.project.buySellStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.project.buySellStore.dto.AssignSellerDTO;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
import ru.project.buySellStore.dto.ProductSupplierUpdateDTO;
import ru.project.buySellStore.dto.productView.ProductBuyerDTO;
import ru.project.buySellStore.dto.productView.ProductSupplierDTO;
import ru.project.buySellStore.exception.productEx.*;
import ru.project.buySellStore.exception.userEx.UserNotFoundException;
import ru.project.buySellStore.mapper.ProductMapper;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.AuthService;
import ru.project.buySellStore.service.ProductService;
import ru.project.buySellStore.service.impl.UserServiceImpl;

import java.util.List;

/**
 * Тесты для {@link ProductController}
 */
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserServiceImpl userService;

    private User supplierUser;

    private User sellerUser;

    private User buyerUser;

    private Product product;

    private ProductDTO productDTO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Явное создание сущностей для тестов
     */
    @BeforeEach
    void setUp(){
        supplierUser = new User();
        supplierUser.setId(1L);
        supplierUser.setLogin("supplier-user");
        supplierUser.setRole(Role.SUPPLIER);

        sellerUser = new User();
        sellerUser.setId(2L);
        sellerUser.setLogin("seller-user");
        sellerUser.setRole(Role.SELLER);

        buyerUser = new User();
        buyerUser.setId(3L);
        buyerUser.setLogin("buyer-user");
        buyerUser.setRole(Role.BUYER);

        product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("description");
        product.setCategory("CLOTHES");
        product.setSupplierCost(1000);

        productDTO = new ProductDTO("name",
                "description",
                "CLOTHES",
                1000);
    }

    /**
     * <b>Проверяет получение списка всех товаров</b>
     * <p>Ожидается - корректное отображение данных всех продуктов в JSON-ответе</p>
     */
    @Test
    void testFindAll() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setRole(Role.SUPPLIER);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("name1");
        product1.setDescription("description1");
        product1.setCategory("CLOTHES");
        product1.setSupplierCost(1000);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("name2");
        product2.setDescription("description2");
        product2.setCategory("ELECTRONICS");
        product2.setSupplierCost(2000);

        ProductSupplierDTO dto1 = new ProductSupplierDTO(
                product1.getId(),
                product1.getName(),
                product1.getDescription(),
                product1.getCategory(),
                product1.getSupplierCost()
        );
        ProductSupplierDTO dto2 = new ProductSupplierDTO(
                product2.getId(),
                product2.getName(),
                product2.getDescription(),
                product2.getCategory(),
                product2.getSupplierCost()
        );

        Mockito.when(authService.getAuthenticatedUser()).thenReturn(user);
        Mockito.when(productService.findAll(user)).thenReturn(List.of(product1, product2));
        Mockito.when(productMapper.toDtoByRole(product1, user.getRole())).thenReturn(dto1);
        Mockito.when(productMapper.toDtoByRole(product2, user.getRole())).thenReturn(dto2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(dto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(dto1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(dto1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(dto1.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].supplierCost").value(dto1.getSupplierCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(dto2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(dto2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(dto2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].category").value(dto2.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].supplierCost").value(dto2.getSupplierCost()));

        Mockito.verify(productService, Mockito.times(1)).findAll(user);
    }

    /**
     * <b>Проверяет получение товара по существующему {@code id} для авторизованного пользователя</b>
     * <p>Ожидается - корректное отображение свойств одного продукта в JSON-ответе</p>
     */
    @Test
    void testFindExistingProductByIdWithRole() throws Exception {

        Mockito.when(authService.getAuthenticatedUser()).thenReturn(buyerUser);

        product.setBuyer(buyerUser);
        Mockito.when(productService.findById(product.getId(), buyerUser)).thenReturn(product);

        ProductBuyerDTO productBuyerDTO = new ProductBuyerDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getSupplierCost()
        );

        Mockito.when(productMapper.toDtoByRole(product, buyerUser.getRole())).thenReturn(productBuyerDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value(productBuyerDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value(productBuyerDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category")
                        .value(productBuyerDTO.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finalCost")
                        .value(productBuyerDTO.getFinalCost()));

        Mockito.verify(authService, Mockito.times(1)).getAuthenticatedUser();
        Mockito.verify(productService, Mockito.times(1))
                .findById(product.getId(), buyerUser);
        Mockito.verify(productMapper, Mockito.times(1))
                .toDtoByRole(product, buyerUser.getRole());
    }

    /**
     * <b>Проверяет попытку получения несуществующего товара по {@code id}</b>
     * <p>Ожидается - отображение ошибки и код 404</p>
     */
    @Test
    void testFindNonExistingProductById() throws Exception {
        Long productId = 1L;

        Mockito.when(authService.getAuthenticatedUser()).thenReturn(buyerUser);

        Mockito.when(productService.findById(productId, buyerUser))
                .thenThrow(new ProductNotFoundException(productId));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(authService, Mockito.times(1)).getAuthenticatedUser();
        Mockito.verify(productService, Mockito.times(1)).findById(productId, buyerUser);
    }

    /**
     * <b>Проверяет создание нового товара пользователем с ролью {@link Role#SUPPLIER}</b>
     * <p>Ожидается - тело ответа содержит сообщение: "Поставщик 'supplier-user' создал товар 'name'"</p>
     */
    @Test
    void testCreateBySupplier() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        "Поставщик 'supplier-user' создал товар 'name'"));

        Mockito.verify(productService, Mockito.times(1))
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет создание нового товара пользователем с ролью {@link Role#SELLER}</b>
     * <p>Ожидается - отображение ошибки и код 403</p>
     */
    @Test
    void testCreateBySeller() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Только поставщик может создавать товар!"));
        Mockito.verify(productService, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет обновление своего товара пользователем с ролью {@link Role#SELLER}</b>
     * <p>Ожидается - тело ответа содержит сообщение:
     * "Продавец 'seller-user' изменил стоимость и описание товара 'name'!"</p>
     */
    @Test
    void testUpdateBySellerLikeSeller() throws Exception {
        ProductSellerUpdateDTO updateDTO = new ProductSellerUpdateDTO();
        updateDTO.setDescription("description");
        updateDTO.setSellerCost(1000);

        product.setSeller(sellerUser);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);
        Mockito.when(productService.findById(1L, sellerUser))
                .thenReturn(product);
        Mockito.when(productService.save(product))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Продавец 'seller-user' изменил стоимость и описание товара 'name'!"));

        Mockito.verify(productService)
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяется обновление описания товара и цены продавца пользователем без роли {@link Role#SELLER}</b>
     * <p>Ожидается - код 403 и сообщение: "Только продавец может менять описание и цену!"</p>
     */
    @Test
    void testUpdateBySellerLikeNotSeller() throws Exception {
        ProductSellerUpdateDTO updateDTO =
                new ProductSellerUpdateDTO();

        updateDTO.setDescription("description");
        updateDTO.setSellerCost(1000);
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(buyerUser);
        Mockito.when(productService.findById(1L, buyerUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Только продавец может менять описание и цену!"));

        Mockito.verify(productService, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяется обновление товара, который принадлежит другому пользователю</b>
     * <p>Ожидается - код 403 и сообщение: "Этот товар не назначен вам!"</p>
     */
    @Test
    void testUpdateBySellerLikeAnotherSeller() throws Exception {
        ProductSellerUpdateDTO updateDTO = new ProductSellerUpdateDTO();
        updateDTO.setDescription("description");
        updateDTO.setSellerCost(1000);

        product.setSeller(sellerUser);

        User anotherSeller = new User();
        anotherSeller.setRole(Role.SELLER);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(anotherSeller);
        Mockito.when(productService.findById(1L, anotherSeller))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Этот товар не назначен вам!"));

        Mockito.verify(productService, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет обновление своего товара пользователем с ролью {@link Role#SUPPLIER}</b>
     * <p>Ожидается - тело ответа содержит сообщение:
     * "Поставщик 'supplier-user' изменил товар 'New Name'"</p>
     */
    @Test
    void testUpdateBySupplierLikeSupplier() throws Exception {
        ProductSupplierUpdateDTO updateDTO = new ProductSupplierUpdateDTO();
        updateDTO.setName("New Name");
        updateDTO.setDescription("New Description");
        updateDTO.setSupplierCost(2000);

        Mockito.when(authService.getAuthenticatedUser()).thenReturn(supplierUser);
        product.setSupplier(supplierUser);
        Mockito.when(productService.findById(1L, supplierUser)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Поставщик 'supplier-user' изменил товар 'New Name'"));

        Mockito.verify(productService).save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет обновление товара пользователем с ролью отличной от {@link Role#SUPPLIER}</b>
     * <p>Ожидается - код 403 и сообщение:
     * "Только поставщик может редактировать название, описание и изначальную цену товара!"</p>
     */
    @Test
    void testUpdateBySupplierLikeNotSupplier() throws Exception {
        ProductSupplierUpdateDTO updateDTO = new ProductSupplierUpdateDTO();
        updateDTO.setName("New Name");
        updateDTO.setDescription("New Description");
        updateDTO.setSupplierCost(101);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(buyerUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(
                                "Только поставщик может редактировать название, описание и изначальную цену товара!"));

        Mockito.verify(productService, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет обновление чужого товара пользователем с ролью {@link Role#SUPPLIER}</b>
     * <p>Ожидается - код 403 и сообщение: "Поставщик может изменять только свои товары!"</p>
     */
    @Test
    void testUpdateBySupplierLikeNotOwner() throws Exception {
        ProductSupplierUpdateDTO updateDTO = new ProductSupplierUpdateDTO();
        updateDTO.setName("New Name");
        updateDTO.setDescription("New Description");
        updateDTO.setSupplierCost(101);

        User otherSupplier = new User();
        otherSupplier.setId(2L);
        otherSupplier.setLogin("other-supplier");
        otherSupplier.setRole(Role.SUPPLIER);

        Mockito.when(authService.getAuthenticatedUser()).thenReturn(supplierUser);
        product.setSupplier(otherSupplier);
        Mockito.when(productService.findById(1L, supplierUser)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Поставщик может изменять только свои товары!"));

        Mockito.verify(productService, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет обновление товара после назначения продавца</b>
     * <p>Ожидается - код 403 и сообщение: "Поставщик не может редактировать товар после назначения продавца!"</p>
     */
    @Test
    void testUpdateBySupplierAfterSellerAssigned() throws Exception {
        ProductSupplierUpdateDTO updateDTO = new ProductSupplierUpdateDTO();
        updateDTO.setName("New Name");
        updateDTO.setDescription("New Description");
        updateDTO.setSupplierCost(101);

        product.setSupplier(supplierUser);
        product.setSeller(sellerUser);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);
        Mockito.when(productService.findById(1L, supplierUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Поставщик не может редактировать товар после назначения продавца!"));

        Mockito.verify(productService, Mockito.never())
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет удаление товара по id пользователем с ролью {@link Role#SUPPLIER}</b>
     * <p>Ожидается - тело ответа содержит сообщение "Товар 'name' удален поставщиком 'supplier-user'"</p>
     */
    @Test
    void testDeleteBySupplier() throws Exception {
        product.setSupplier(supplierUser);
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);

        Mockito.when(productService.findById(1L, supplierUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Товар 'name' удален поставщиком 'supplier-user'"));

        Mockito.verify(productService)
                .delete(1L);
    }

    /**
     * <b>Проверяет удаление чужого товара по {@code id} пользователем с ролью {@link Role#SUPPLIER}</b>
     * <p>Ожидается - код 403 и сообщение: "Поставщик может удалять только свой товар!"</p>
     */
    @Test
    void testDeleteByAnotherSupplier() throws Exception {
        User anotherSupplier = new User();
        anotherSupplier.setId(9L);
        anotherSupplier.setRole(Role.SUPPLIER);

        product.setSupplier(anotherSupplier);
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);

        Mockito.when(productService.findById(1L, supplierUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Поставщик может удалять только свой товар!"));

        Mockito.verify(productService, Mockito.never())
                .delete(Mockito.anyLong());
    }

    /**
     * <b>Проверяет удаление товара по {@code id} пользователем с ролью {@link Role#SELLER}</b>
     * <p>Ожидается - код 403 и сообщение "Только поставщик может удалять товар!"</p>
     */
    @Test
    void testDeleteByNotSupplier() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);
        Mockito.when(productService.findById(1L, sellerUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Только поставщик может удалять товар!"));

        Mockito.verify(productService, Mockito.never())
                .delete(Mockito.anyLong());
    }

    /**
     * <b>Проверяет архивирование товара</b>
     * <p>Ожидается - тело ответа содержит сообщение "Товар добавлен в архив"</p>
     */
    @Test
    void testArchive() throws Exception {
        Mockito.doNothing()
                .when(productService)
                .archive(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/archive"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Товар добавлен в архив"));

        Mockito.verify(productService)
                .archive(1L);
    }

    /**
     * <b>Проверяется архивирование ранее архивированного товара</b>
     * <b>Ожидается - код 409 и сообщение: "Товар с id = 1 уже находится в архиве"</b>
     */
    @Test
    void testArchiveArchivedProduct() throws Exception {
        Long productId = 1L;

        Mockito.doThrow(new ProductArchiveException(productId))
                .when(productService)
                .archive(productId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/archive", productId))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар с id = 1 уже находится в архиве"));

        Mockito.verify(productService)
                .archive(productId);
    }

    /**
     * <b>Проверяется архивирование несуществующего товара</b>
     * <b>Ожидается - код 404 и сообщение: "Товар с id = 1 не найден"</b>
     */
    @Test
    void testArchiveNonExistingProduct() throws Exception {
        Long productId = 1L;

        Mockito.doThrow(new ProductNotFoundException(productId))
                .when(productService)
                .archive(productId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/archive", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар с id = 1 не найден"));

        Mockito.verify(productService)
                .archive(productId);
    }

    /**
     * <b>Проверяет восстановление товара из архива</b>
     * <p>Ожидается - тело ответа содержит сообщение о возвращении товара в открытый доступ</p>
     */
    @Test
    void testRestore() throws Exception {
        Mockito.doNothing()
                .when(productService)
                .restore(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/restore"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        "Ваш товар вернулся в открытый доступ. " +
                                "Теперь другие пользователи снова могут просматривать и покупать его"
                ));

        Mockito.verify(productService)
                .restore(1L);
    }

    /**
     * <b>Проверяется удаление из архива не архивированного товара</b>
     * <b>Ожидается - код 409 и сообщение: "Товар с id = 1 не находится в архиве"</b>
     */
    @Test
    void testRestoreNotArchivedProduct() throws Exception {
        Long productId = 1L;

        Mockito.doThrow(new ProductRestoreException(productId))
                .when(productService)
                .restore(productId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/restore", productId))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар c id = 1 уже доступен и не находится в архиве"));

        Mockito.verify(productService)
                .restore(productId);
    }

    /**
     * <b>Проверяется удаление из архива несуществующего товара</b>
     * <b>Ожидается - код 404 и сообщение: "Товар с id = 1 не найден"</b>
     */
    @Test
    void testRestoreNonExistingProduct() throws Exception {
        Long productId = 1L;

        Mockito.doThrow(new ProductNotFoundException(productId))
                .when(productService)
                .restore(productId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/restore", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар с id = 1 не найден"));

        Mockito.verify(productService)
                .restore(productId);
    }

    /**
     * <b>Проверяет определение продавца для товара</b>
     * <p>Ожидается - тело ответа содержит сообщение "Продавец 'seller-user' назначен на товар 'name'"</p>
     */
    @Test
    void testAssignSellerBySupplier() throws Exception {
        AssignSellerDTO assignSellerDTO = new AssignSellerDTO(2L);
        product.setSupplier(supplierUser);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);
        Mockito.when(userService.getUserById(2L))
                .thenReturn(sellerUser);
        Mockito.when(productService.findById(1L, supplierUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/assign-seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignSellerDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Продавец 'seller-user' назначен на товар 'name'"));
        Mockito.verify(productService)
                .assignSeller(product, sellerUser);
    }

    /**
     * <b>Проверяет определение продавца для чужого товара</b>
     * <p>Ожидается - код 403 и сообщение "Поставщик может назначать продавца только на свой товар"</p>
     */
    @Test
    void testAssignSellerByAnotherSupplier() throws Exception {
        User anotherSupplier = new User();
        anotherSupplier.setId(9L);
        anotherSupplier.setRole(Role.SUPPLIER);
        product.setSupplier(anotherSupplier);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);
        Mockito.when(userService.getUserById(2L))
                .thenReturn(sellerUser);
        Mockito.when(productService.findById(1L, supplierUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/assign-seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AssignSellerDTO(2L))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Поставщик может назначать продавца только на свой товар"));
        Mockito.verify(productService, Mockito.never())
                .assignSeller(Mockito.any(Product.class), Mockito.any(User.class));
    }


    /**
     * <b>Проверяет определение продавца для товара пользователем без роли {@link Role#SUPPLIER}</b>
     * <p>Ожидается - код 403 и сообщение: "Только поставщик может назначать продавца на товар"</p>
     */
    @Test
    void testAssignSellerByNotSupplier() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);
        Mockito.when(userService.getUserById(2L))
                .thenReturn(supplierUser);
        Mockito.when(productService.findById(1L, sellerUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/assign-seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AssignSellerDTO(2L))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Только поставщик может назначать продавца на товар"));
        Mockito.verify(productService, Mockito.never())
                .assignSeller(Mockito.any(Product.class), Mockito.any(User.class));
    }

    /**
     * <b>Проверяет назначение продавца пользователю без роли SELLER через контроллер</b>
     * <p>Ожидается - код 403 и исключение UserNotSuitableRoleException с сообщением:
     * "Продавцом можно назначить только пользователя с ролью SELLER"</p>
     */
    @Test
    void testAssignSellerWrongRole() throws Exception {

        AssignSellerDTO assignSellerDTO = new AssignSellerDTO(buyerUser.getId());
        product.setSupplier(supplierUser);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);
        Mockito.when(userService.getUserById(buyerUser.getId()))
                .thenReturn(buyerUser);
        Mockito.when(productService.findById(product.getId(), supplierUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/assign-seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignSellerDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Продавцом можно назначить только пользователя с ролью SELLER"));

        Mockito.verify(productService, Mockito.never())
                .assignSeller(Mockito.any(Product.class), Mockito.any(User.class));
    }


    /**
     * <b>Проверяет назначение несуществующего продавца на товар</b>
     * <p>Ожидается - код 404 и сообщение "Пользователь с id = 2 не найден"</p>
     */
    @Test
    void testAssignNonExistingSeller() throws Exception {
        Long productId = 1L;
        Long nonExistingUserId = 2L;
        product.setSupplier(supplierUser);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);
        Mockito.doThrow(new UserNotFoundException(nonExistingUserId))
                .when(userService)
                .getUserById(nonExistingUserId);

        Mockito.when(productService.findById(productId, supplierUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/assign-seller", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AssignSellerDTO(nonExistingUserId))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Пользователь с id = 2 не найден"));

        Mockito.verify(userService)
                .getUserById(nonExistingUserId);
        Mockito.verify(productService, Mockito.never())
                .assignSeller(Mockito.any(Product.class), Mockito.any(User.class));
    }

    /**
     * <b>Проверяет назначение продавца на несуществующий товар</b>
     * <p>Ожидается - код 404 и сообщение "Товар с id = 1 не найден"</p>
     */
    @Test
    void testAssignSellerOnNonExistingProduct() throws Exception{
        Long productId = 1L;
        Long sellerId = 2L;

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);
        Mockito.when(userService.getUserById(sellerId))
                .thenReturn(sellerUser);
        Mockito.doThrow(new ProductNotFoundException(productId))
                .when(productService)
                .findById(productId, supplierUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/assign-seller", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AssignSellerDTO(sellerId))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар с id = 1 не найден"));

        Mockito.verify(productService)
                .findById(productId, supplierUser);
        Mockito.verify(productService, Mockito.never())
                .assignSeller(Mockito.any(Product.class), Mockito.any(User.class));
    }

    /**
     * <b>Проверяет успешную покупку товара пользователем с ролью {@link Role#BUYER}</b>
     * <p>Ожидается - сообщение: "Покупатель 'buyer-user' купил товар 'name'"</p>
     */
    @Test
    void testBuyByBuyer() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(buyerUser);
        Mockito.when(productService.findById(1L, buyerUser))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/buy"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Покупатель 'buyer-user' купил товар 'name'"));

        Mockito.verify(productService)
                .buyProduct(1L, buyerUser);
    }

    /**
     * <b>Проверяет успешную покупку товара пользователем без роли {@link Role#BUYER}</b>
     * <p>Ожидается - код 403 и сообщение: "Покупать товары может только покупатель!"</p>
     */
    @Test
    void testBuyByNotBuyer() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/buy"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Покупать товары может только покупатель!"));

        Mockito.verify(productService, Mockito.never())
                .buyProduct(Mockito.anyLong(), Mockito.any(User.class));
    }

    /**
     * <b>Проверяет покупку архивированного товара</b>
     * <p>Ожидается - код 409 и сообщение: "Товар с id = 1 уже находится в архиве"</p>
     */
    @Test
    void testBuyArchivedProduct() throws Exception{
        Long productId = 1L;

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(buyerUser);
        Mockito.doThrow(new ProductArchiveException(productId))
                .when(productService)
                .buyProduct(productId, buyerUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/buy", productId))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар с id = 1 уже находится в архиве"));

        Mockito.verify(productService)
                .buyProduct(productId, buyerUser);
    }

    /**
     * <b>Проверяет покупку товара, продавец на который не назначен</b>
     * <p>Ожидается - код 409 и сообщение: "Товар с id = 1 не имеет назначенного продавца"</p>
     */
    @Test
    void testBuyProductWithNonAssignedSeller() throws Exception{
        Long productId = 1L;

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(buyerUser);

        Mockito.doThrow(new ProductWithoutSellerException(productId))
                .when(productService)
                .buyProduct(productId, buyerUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/buy", productId))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар с id = 1 не имеет назначенного продавца"));

        Mockito.verify(productService)
                .buyProduct(productId, buyerUser);
    }

    /**
     * <b>Проверяет покупку несуществующего товара</b>
     * <p>Ожидается - код 404 и сообщение: "Товар с id = 1 не найден"</p>
     */
    @Test
    void testBuyNonExistingProduct() throws Exception{
        Long productId = 1L;

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(buyerUser);
        Mockito.doThrow(new ProductNotFoundException(productId))
                .when(productService)
                .buyProduct(productId, buyerUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/buy", productId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар с id = 1 не найден"));

        Mockito.verify(productService)
                .buyProduct(productId, buyerUser);
    }

    /**
     * <b>Проверяет покупку купленного товара</b>
     * <p>Ожидается - код 409 и сообщение: "Товар с id = 1 уже куплен"</p>
     */
    @Test
    void testBuyAlreadyBoughtProduct() throws Exception{
        Long productId = 1L;

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(buyerUser);
        Mockito.doThrow(new ProductAlreadyBoughtException(productId))
                .when(productService)
                .buyProduct(productId, buyerUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/{id}/buy", productId))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар с id = 1 уже куплен"));

        Mockito.verify(productService)
                .buyProduct(productId, buyerUser);
    }
}
