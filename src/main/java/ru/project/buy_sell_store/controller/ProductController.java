package ru.project.buy_sell_store.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.project.buy_sell_store.dto.ProductDTO;
import ru.project.buy_sell_store.mapper.ProductMapper;
import ru.project.buy_sell_store.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управление Товара
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    /**
     * Получение всех товаров
     */
    @GetMapping
    public List<ProductDTO> findAll() {
        return productService.findAll().stream().map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Создание нового товара
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@Valid @RequestBody ProductDTO productDto) {
        productService.save(productMapper.toEntity(productDto));
        return "Продукт создан!";
    }

    /**
     * Получение товара по id
     */
    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable("id") Long id) {
        return productMapper.toDto(productService.findById(id));
    }

    /**
     * Обновление товара по id
     */
    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                                            @Valid @RequestBody ProductDTO productDto) {
        productService.update(id, productMapper.toEntity(productDto));
        return "Продукт изменен!";
    }

    /**
     * Удаление товара по id
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return "Продукт удален!";
    }

    /**
     * Архивирование товара по id
     */
    @PostMapping("/{id}/archive")
    public String archive(@PathVariable("id") Long id) {
        productService.archive(id);
        return "Товар добавлен в архив";
    }

    /**
     * Восстановление товара из архива по id
     */
    @PostMapping("/{id}/restore")
    public String restore(@PathVariable("id") Long id) {
        productService.restore(id);
        return "Ваш товар вернулся в открытый доступ. " +
                "Теперь другие пользователи снова могут просматривать и покупать его";
    }
}
