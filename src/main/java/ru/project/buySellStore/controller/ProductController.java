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
import ru.project.buySellStore.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управление Товара
 */
@Transactional
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
    public String create(@Valid @RequestBody ProductDTO productDto) {
        productService.save(productDto);
        return "Продукт создан!";
    }

    /**
     * Получение товара по id
     */
    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable("id") Long id) throws ProductNotFoundException {
        return productMapper.toDto(productService.findById(id));
    }

    /**
     * Обновление товара по id
     */
    @PatchMapping("/{id}")
    public String update(
            @PathVariable("id") Long id, @Valid @RequestBody ProductUpdateDTO productUpdateDTO)
            throws ProductNotFoundException {
        productService.update(id, productUpdateDTO);
        return "Продукт изменен!";
    }

    /**
     * Удаление товара по id
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id)
            throws ProductNotFoundException {
        productService.delete(id);
        return "Продукт удален!";
    }

    /**
     * Архивирование товара по id
     */
    @PostMapping("/{id}/archive")
    public String archive(@PathVariable("id") Long id)
            throws ProductArchiveException, ProductNotFoundException {
        productService.archive(id);
        return "Товар добавлен в архив";
    }

    /**
     * Восстановление товара из архива по id
     */
    @PostMapping("/{id}/restore")
    public String restore(@PathVariable("id") Long id)
            throws ProductNotFoundException, ProductRestoreException {
        productService.restore(id);
        return "Ваш товар вернулся в открытый доступ. " +
                "Теперь другие пользователи снова могут просматривать и покупать его";
    }
}
