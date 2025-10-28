package ru.project.buy_sell_store.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.buy_sell_store.dto.ProductDTO;
import ru.project.buy_sell_store.service.ProductService;

import java.util.List;

/**
 * Контроллер для управление Товара
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Получение всех товаров
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    /**
     * Создание нового товара
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@Valid @RequestBody ProductDTO productDto) {
        productService.save(productDto);
        return "Продукт создан!";
    }

    /**
     * Получение товара по id
     */
    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable("id") Long id) {
        return productService.findById(id);
    }

    /**
     * Обновление товара по id
     */
    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                                            @Valid @RequestBody ProductDTO productDto) {
        productService.update(id, productDto);
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
