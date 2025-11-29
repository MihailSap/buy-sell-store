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
import ru.project.buySellStore.exception.productEx.ProductNotFoundException;
import ru.project.buySellStore.mapper.ProductMapper;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.AuthService;
import ru.project.buySellStore.service.ProductService;
import ru.project.buySellStore.service.impl.UserServiceImpl;

import java.util.List;

/**
 * Тесты для ProductController
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

    /**
     * Явное создание сущностей для тестов
     */
    @BeforeEach
    void setUp(){
        supplierUser = new User();
        supplierUser.setLogin("supplier-user");
        supplierUser.setRole(Role.SUPPLIER);

        sellerUser = new User();
        sellerUser.setLogin("seller-user");
        sellerUser.setRole(Role.SELLER);

        buyerUser = new User();
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

        ProductDTO productDTO1 = new ProductDTO(1L,"name1",
                "description1",
                "CLOTHES",
                1000);
        ProductDTO productDTO2 = new ProductDTO(2L,"name2",
                "description2",
                "ELECTRONICS"
                , 2000);

        Mockito.when(productService.findAll())
                .thenReturn(List.of(product1, product2));
        Mockito.when(productMapper.toDto(product1))
                .thenReturn(productDTO1);
        Mockito.when(productMapper.toDto(product2))
                .thenReturn(productDTO2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(productDTO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(productDTO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(productDTO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(productDTO1.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cost").value(productDTO1.getCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(productDTO2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].supplierCost").value(productDTO1.getSupplierCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(productDTO2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(productDTO2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].category").value(productDTO2.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].supplierCost").value(productDTO2.getSupplierCost()));

        Mockito.verify(productService, Mockito.times(1)).findAll();
    }

    /**
     * <b>Проверяет получение товара по существующему {@code id}</b>
     * <p>Ожидается - корректное отображение свойств одного продукта в JSON-ответе</p>
     */
    @Test
    void testFindById() throws Exception {
        Mockito.when(productService.findById(1L)).thenReturn(product);
        Mockito.when(productMapper.toDto(product)).thenReturn(productDTO);
        Mockito.when(productService.findById(1L))
                .thenReturn(product);
        Mockito.when(productMapper.toDto(product))
                .thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(productDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(productDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(productDTO.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.supplierCost").value(productDTO.getSupplierCost()));

        Mockito.verify(productService, Mockito.times(1))
                .findById(1L);
    }

    /**
     * Проверяет создание нового товара пользователем с ролью SUPPLIER
     * Ожидается - тело ответа содержит сообщение "Поставщик 'supplier-user' создал товар 'name'"
     */
    @Test
    void testCreateBySupplier() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Поставщик 'supplier-user' создал товар 'name'"));
        Mockito.verify(productService, Mockito.times(1))
                .save(Mockito.any(ProductDTO.class), Mockito.any(User.class));
    }

    /**
     * Проверяет появление исключения
     * при попытке пользователя с ролью SELLER создать новый продукт
     */
    @Test
    void testCreateBySeller() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Только поставщик может создавать товар!"));
        Mockito.verify(productService, Mockito.never())
                .save(Mockito.any(ProductDTO.class), Mockito.any(User.class));
    }

    /**
     * Проверяет удаление товара по id пользователем с ролью SUPPLIER
     * Ожидается - тело ответа содержит сообщение "Товар 'name' удален поставщиком 'supplier-user'"
     */
    @Test
    void testDeleteBySupplier() throws Exception {
        product.setSupplier(supplierUser);
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);

        Mockito.when(productService.findById(1L))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Товар 'name' удален поставщиком 'supplier-user'"));

        Mockito.verify(productService, Mockito.times(1))
                .delete(1L);
    }

    /**
     * Проверяет удаление товара по id пользователем с ролью SELLER
     * Ожидается - тело ответа содержит сообщение "Только поставщик может удалять товар!"
     */
    @Test
    void testDeleteBySeller() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);
        Mockito.when(productService.findById(1L))
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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/archive"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Товар добавлен в архив"));

        Mockito.verify(productService, Mockito.times(1))
                .archive(1L);
    }

    /**
     * <b>Проверяет восстановление товара из архива</b>
     * <p>Ожидается - тело ответа содержит сообщение о возвращении товара в открытый доступ</p>
     */
    @Test
    void testRestore() throws Exception {
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
     * Проверяет определение продавца для товара.
     * Ожидается - тело ответа содержит сообщение "Продавец 'seller-user' назначен на товар 'name'"
     */
    @Test
    void testAssignSellerBySupplier() throws Exception {
        AssignSellerDTO assignSellerDTO = new AssignSellerDTO(2L);

        product.setSupplier(supplierUser);
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);
        Mockito.when(userService.getUserById(2L))
                .thenReturn(sellerUser);
        Mockito.when(productService.findById(1L))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/assign-seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(assignSellerDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Продавец 'seller-user' назначен на товар 'name'"));
        Mockito.verify(productService, Mockito.times(1))
                .assignSeller(product, sellerUser);
    }

    /**
     * Проверяет определение продавца для товара
     * Ожидается - тело ответа содержит сообщение "Только поставщик может назначать продавца на товар"
     */
    @Test
    void testAssignSellerBySeller() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);
        Mockito.when(userService.getUserById(2L))
                .thenReturn(supplierUser);
        Mockito.when(productService.findById(1L))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/assign-seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new AssignSellerDTO(2L))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Только поставщик может назначать продавца на товар"));
        Mockito.verify(productService, Mockito.never())
                .assignSeller(Mockito.any(Product.class), Mockito.any(User.class));
    }

    /**
     * Проверяет успешное обновление товара продавцом
     */
    @Test
    void testUpdateBySeller() throws Exception {
        ProductSellerUpdateDTO updateDTO =
                new ProductSellerUpdateDTO();

        updateDTO.setDescription("description");
        updateDTO.setSellerCost(1000);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);

        Mockito.when(productService.findById(1L))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Продавец 'seller-user' изменил стоимость и описание товара 'name'!"));

        Mockito.verify(productService, Mockito.times(1))
                .updateBySeller(Mockito.eq(1L), Mockito.any(ProductSellerUpdateDTO.class), Mockito.eq(sellerUser));
    }

    /**
     * Проверяет обновление товара НЕ продавцом
     */
    @Test
    void testUpdateByNonSeller() throws Exception {
        ProductSellerUpdateDTO updateDTO =
                new ProductSellerUpdateDTO();

        updateDTO.setDescription("description");
        updateDTO.setSellerCost(1000);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplierUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Только продавец может менять описание и цену!"));

        Mockito.verify(productService, Mockito.never())
                .updateBySeller(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    /**
     * Проверяет успешную покупку товара покупателем
     */
    @Test
    void testBuyByBuyer() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(buyerUser);

        Mockito.when(productService.findById(1L))
                .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/buy"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Покупатель 'buyer-user' купил товар 'name'"));

        Mockito.verify(productService, Mockito.times(1))
                .buyProduct(1L, buyerUser);
    }

    /**
     * Проверяет покупку товара НЕ покупателем
     */
    @Test
    void testBuyByNonBuyer() throws Exception {
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(sellerUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/buy"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Покупать товары может только покупатель!"));

        Mockito.verify(productService, Mockito.never())
                .buyProduct(Mockito.anyLong(), Mockito.any());
    }
}
