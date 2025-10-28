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
    public ResponseEntity<String> create(@Valid @RequestBody ProductDTO productDto) {
        productService.save(productDto);
        return new ResponseEntity<>("Продукт создан!", HttpStatus.CREATED);
    }

    /**
     * Получение товара по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    /**
     * Обновление товара по id
     */
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id,
                                            @Valid @RequestBody ProductDTO productDto) {
        productService.update(id, productDto);
        return new ResponseEntity<>("Продукт изменен!", HttpStatus.OK);
    }

    /**
     * Удаление товара по id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return new ResponseEntity<>("Продукт удален!", HttpStatus.OK);
    }

    /**
     * Архивирование товара по id
     */
    @PostMapping("/{id}/archive")
    public ResponseEntity<String> archive(@PathVariable("id") Long id) {
        productService.archive(id);
        return new ResponseEntity<>("Товар добавлен в архив", HttpStatus.OK);
    }

    /**
     * Восстановление товара из архива по id
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<String> restore(@PathVariable("id") Long id) {
        productService.restore(id);
        return new ResponseEntity<>("Ваш товар вернулся в открытый доступ. " +
                "Теперь другие пользователи снова могут просматривать и покупать его",
                HttpStatus.OK);
    }
}
