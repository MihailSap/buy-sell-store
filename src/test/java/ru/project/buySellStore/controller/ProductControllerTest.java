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

    /**
     * Проверяет получение списка всех товаров
     *
     * Ожидается - корректное отображение данных всех продуктов в JSON-ответе
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

        ProductDTO productDTO1 = new ProductDTO("name1",
                "description1",
                "CLOTHES",
                1000);
        ProductDTO productDTO2 = new ProductDTO("name2",
                "description2",
                "ELECTRONICS"
                , 2000);

        Mockito.when(productService.findAll()).thenReturn(List.of(product1, product2));
        Mockito.when(productMapper.toDto(product1)).thenReturn(productDTO1);
        Mockito.when(productMapper.toDto(product2)).thenReturn(productDTO2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(productDTO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(productDTO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(productDTO1.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cost").value(productDTO1.getCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(productDTO2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(productDTO2.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].category").value(productDTO2.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].cost").value(productDTO2.getCost()));

        Mockito.verify(productService, Mockito.times(1)).findAll();
    }

    /**
     * Проверяет получение товара по его id
     *
     * Ожидается - корректное отображение свойств одного продукта в JSON-ответе
     */
    @Test
    void testFindById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("description");
        product.setCategory("CLOTHES");
        product.setCost(1000);

        ProductDTO productDTO = new ProductDTO("name",
                "description",
                "CLOTHES",
                1000);

        Mockito.when(productService.findById(1L)).thenReturn(product);
        Mockito.when(productMapper.toDto(product)).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(productDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(productDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(productDTO.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(productDTO.getCost()));

        Mockito.verify(productService, Mockito.times(1)).findById(1L);
    }

    /**
     * Проверяет создание нового товара
     *
     * Ожидается - тело ответа содержит сообщение "Продукт создан!"
     */
    @Test
    void testCreate() throws Exception {
        ProductDTO dto = new ProductDTO("Test product",
                "desc",
                "CLOTHES",
                1000);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Продукт создан!"));

        Mockito.verify(productService, Mockito.times(1))
                .save(Mockito.any(ProductDTO.class));
    }

    /**
     * Проверяет обновление существующего товара
     *
     * Ожидается - тело ответа содержит сообщение "Продукт изменен!"
     */
    @Test
    void testUpdate() throws Exception {
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setName("Updated name");
        updateDTO.setDescription("Updated desc");
        updateDTO.setCost(1500);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Продукт изменен!"));

        Mockito.verify(productService, Mockito.times(1))
                .update(Mockito.eq(1L), Mockito.any(ProductUpdateDTO.class));
    }

    /**
     * Проверяет удаление товара по id
     *
     * Ожидается - тело ответа содержит сообщение "Продукт удален!"
     */
    @Test
    void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Продукт удален!"));

        Mockito.verify(productService, Mockito.times(1)).delete(1L);
    }

    /**
     * Проверяет архивирование товара
     *
     * Ожидается - тело ответа содержит сообщение "Товар добавлен в архив"
     */
    @Test
    void testArchive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/archive"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Товар добавлен в архив"));

        Mockito.verify(productService, Mockito.times(1)).archive(1L);
    }

    /**
     * Проверяет восстановление товара из архива
     *
     * Ожидается - тело ответа содержит сообщение о возвращении товара в открытый доступ
     */
    @Test
    void testRestore() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/restore"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        "Ваш товар вернулся в открытый доступ. " +
                                "Теперь другие пользователи снова могут просматривать и покупать его"
                ));

        Mockito.verify(productService, Mockito.times(1)).restore(1L);
    }
}
