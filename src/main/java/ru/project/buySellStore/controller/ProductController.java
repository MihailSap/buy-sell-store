package ru.project.buySellStore.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.project.buySellStore.dto.AssignSellerDTO;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductUpdateDTO;
import ru.project.buySellStore.mapper.ProductMapper;
import ru.project.buySellStore.service.AuthService;
import ru.project.buySellStore.service.ProductService;
import ru.project.buySellStore.service.impl.UserServiceImpl;

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

    private final AuthService authService;

    private final UserServiceImpl userServiceImpl;

    /**
     * Создание контроллера с внедрением нужных зависимостей
     * @param productService - сервис Товара
     * @param productMapper - маппер Товара
     */
    public ProductController(ProductService productService, ProductMapper productMapper, AuthService authService, UserServiceImpl userServiceImpl) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.authService = authService;
        this.userServiceImpl = userServiceImpl;
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
    @PreAuthorize("hasRole('SUPPLIER')")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@Valid @RequestBody ProductDTO productDto) {
        productService.save(productDto);
        return String.format(
                "Поставщик '%s' создал товар '%s'",
                authService.getAuthenticatedUser().getLogin(), productDto.getName());
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
    @PreAuthorize("hasRole('SUPPLIER')")
    public String update(@PathVariable("id") Long id,
                                            @Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
        productService.update(id, productUpdateDTO);
        return "Продукт изменен!";
    }

    /**
     * Удаление товара по id
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
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

    @PostMapping("/{productId}/assign-seller")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String assignSeller(@PathVariable("productId") Long productId, @RequestBody AssignSellerDTO assignSellerDTO){
        Long sellerId = assignSellerDTO.getSellerId();
        productService.assignSeller(productId, sellerId);
        return String.format(
                "Продавец '%s' назначен на товар '%s'",
                userServiceImpl.getUserById(sellerId).getLogin(),
                productService.findById(productId).getName());
    }
}
