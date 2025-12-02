package ru.project.buySellStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductUpdateDTO;
import ru.project.buySellStore.exception.productEx.ProductNotFoundException;
import ru.project.buySellStore.mapper.ProductMapper;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.service.ProductService;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        product1.setCost(1000);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("name2");
        product2.setDescription("description2");
        product2.setCategory("ELECTRONICS");
        product2.setCost(2000);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(productDTO2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(productDTO2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].category").value(productDTO2.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].cost").value(productDTO2.getCost()));

        Mockito.verify(productService, Mockito.times(1)).findAll();
    }

    /**
     * <b>Проверяет получение товара по существующему {@code id}</b>
     * <p>Ожидается - корректное отображение свойств одного продукта в JSON-ответе</p>
     */
    @Test
    void testFindExistingProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("description");
        product.setCategory("CLOTHES");
        product.setCost(1000);

        ProductDTO productDTO = new ProductDTO(1L,"name",
                "description",
                "CLOTHES",
                1000);

        Mockito.when(productService.findById(1L))
                .thenReturn(product);
        Mockito.when(productMapper.toDto(product))
                .thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(productDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(productDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(productDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(productDTO.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(productDTO.getCost()));

        Mockito.verify(productService)
                .findById(1L);
    }

    /**
     * <b>Проверяет получение товара по несуществующему {@code id}</b>
     * <p>Ожидается - корректное отображение свойств одного продукта в JSON-ответе</p>
     */
    @Test
    void testFindNonExistingProductById() throws Exception {
        Long nonExistingId = 1L;
        Mockito.when(productService.findById(nonExistingId))
                .thenThrow(new ProductNotFoundException(nonExistingId));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Товар с id = 1 не найден"));

        Mockito.verify(productService)
                .findById(nonExistingId);
    }

    /**
     * <b>Проверяет создание нового товара</b>
     * <p>Ожидается - тело ответа содержит сообщение "Продукт создан!"</p>
     */
    @Test
    void testCreate() throws Exception {
        ProductDTO dto = new ProductDTO("Test product",
                "desc",
                "CLOTHES",
                1000);

        Mockito.when(productService.save(Mockito.any(Product.class)))
                        .thenReturn(new Product());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Продукт создан!"));

        Mockito.verify(productService)
                .save(Mockito.any(Product.class));
    }

    /**
     * <b>Проверяет обновление существующего товара</b>
     * <p>Ожидается - тело ответа содержит сообщение "Продукт изменен!"</p>
     */
    @Test
    void testUpdate() throws Exception {
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setName("Updated name");
        updateDTO.setDescription("Updated desc");
        updateDTO.setCost(1500);

        Product product = new Product();
        Mockito.when(productService.findById(1L))
                        .thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Продукт изменен!"));

        Mockito.verify(productService)
                .save(product);
    }

    /**
     * <b>Проверяет удаление товара по id</b>
     * <p>Ожидается - тело ответа содержит сообщение "Продукт удален!"</p>
     */
    @Test
    void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Продукт удален!"));

        Mockito.verify(productService)
                .delete(1L);
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

        Mockito.verify(productService)
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
}
