package ru.project.buySellStore.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductUpdateDTO;
import ru.project.buySellStore.exception.productEx.ProductArchiveException;
import ru.project.buySellStore.exception.productEx.ProductNotFoundException;
import ru.project.buySellStore.exception.productEx.ProductRestoreException;
import ru.project.buySellStore.mapper.ProductMapper;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления товаром
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    /**
     * Создание контроллера с внедрением нужных зависимостей
     * @param productService - сервис Товара
     * @param productMapper - маппер Товара
     */
    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    /**
     * Получение всех товаров, не считая архивных
     */
    @GetMapping
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productService.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Создание нового товара
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public String create(@Valid @RequestBody ProductDTO productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setCost(productDto.getCost());
        productService.save(product);
        return "Продукт создан!";
    }

    /**
     * Получение товара по id
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ProductDTO findById(@PathVariable("id") Long id) throws ProductNotFoundException {
        return productMapper.toDto(productService.findById(id));
    }

    /**
     * Обновление товара по id
     */
    @PatchMapping("/{id}")
    @Transactional
    public String update(
            @PathVariable("id") Long id, @Valid @RequestBody ProductUpdateDTO productUpdateDTO)
            throws ProductNotFoundException {
        Product product = productService.findById(id);
        product.setName(productUpdateDTO.getName());
        product.setDescription(productUpdateDTO.getDescription());
        product.setCost(productUpdateDTO.getCost());
        productService.save(product);
        return "Продукт изменен!";
    }

    /**
     * Удаление товара по id
     */
    @DeleteMapping("/{id}")
    @Transactional
    public String delete(@PathVariable("id") Long id)
            throws ProductNotFoundException {
        productService.delete(id);
        return "Продукт удален!";
    }

    /**
     * Архивирование товара по id
     */
    @PostMapping("/{id}/archive")
    @Transactional
    public String archive(@PathVariable("id") Long id)
            throws ProductArchiveException, ProductNotFoundException {
        productService.archive(id);
        return "Товар добавлен в архив";
    }

    /**
     * Восстановление товара из архива по id
     */
    @PostMapping("/{id}/restore")
    @Transactional
    public String restore(@PathVariable("id") Long id)
            throws ProductNotFoundException, ProductRestoreException {
        productService.restore(id);
        return "Ваш товар вернулся в открытый доступ. " +
                "Теперь другие пользователи снова могут просматривать и покупать его";
    }
}
