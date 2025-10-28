package ru.project.buy_sell_store.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buy_sell_store.dto.ProductDTO;
import ru.project.buy_sell_store.entity.Product;
import ru.project.buy_sell_store.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private ProductDTO productDto;

    @BeforeEach
    void setUp() {
        productDto = new ProductDTO();
        productDto.setName("Футболка Nike");
        productDto.setDescription("Отличная");
        productDto.setCategory("CLOTHES");
        productDto.setCost(2499);
    }

    @Test
    void saveTest() {
        ProductDTO savedProductDto = productService.save(productDto);
        Assertions.assertEquals(productDto.getName(), savedProductDto.getName());
    }

    @Test
    void findByIdTest() {
        ProductDTO savedProductDto = productService.save(productDto);

        ProductDTO foundProductDto = productService.findById(savedProductDto.getId());
        Assertions.assertEquals(savedProductDto.getId(), foundProductDto.getId());
    }

    @Test
    void findAllTest() {
        ProductDTO newProductDto = new ProductDTO();
        newProductDto.setName("Футболка с длинным рукавом");
        newProductDto.setDescription("Удобная");
        newProductDto.setCategory("CLOTHES");
        newProductDto.setCost(3000);

        productService.save(productDto);
        productService.save(newProductDto);

        List<ProductDTO> productDtoList = productService.findAll();

        Assertions.assertTrue(productDtoList.size() >= 2);
    }

    @Test
    void updateTest() {
        ProductDTO savedProductDto = productService.save(productDto);

        ProductDTO updatedProductDto = new ProductDTO();
        updatedProductDto.setName("Футболка Adidas");
        updatedProductDto.setDescription("Отличная, удобная");
        updatedProductDto.setCost(2799);

        productService.update(savedProductDto.getId(), updatedProductDto);

        ProductDTO foundProductDto = productService.findById(savedProductDto.getId());

        Assertions.assertEquals(savedProductDto.getId(), foundProductDto.getId());
        Assertions.assertEquals("Футболка Adidas", foundProductDto.getName());
    }

    @Test
    void deleteTest() {
        ProductDTO savedProductDto = productService.save(productDto);
        productService.delete(savedProductDto.getId());
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
            productService.findById(savedProductDto.getId());
        });
        Assertions.assertEquals("Товар не найден", ex.getMessage());
    }

    @Test
    void archiveTest() {
        ProductDTO savedProductDto = productService.save(productDto);
        productService.archive(savedProductDto.getId());

        Optional<Product> foundProduct = productRepository.findById(savedProductDto.getId());
        Assertions.assertTrue(foundProduct.get().isArchived());
    }

    @Test
    void restoreTest() {
        ProductDTO savedProductDto = productService.save(productDto);
        productService.archive(savedProductDto.getId());
        productService.restore(savedProductDto.getId());

        Optional<Product> foundProduct = productRepository.findById(savedProductDto.getId());
        Assertions.assertFalse(foundProduct.get().isArchived());
    }
}