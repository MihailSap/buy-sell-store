package ru.project.buySellStore.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.project.buySellStore.dto.AssignSellerDTO;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
import ru.project.buySellStore.mapper.ProductMapper;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
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

    private final UserServiceImpl userService;

    /**
     * Создание контроллера с внедрением нужных зависимостей
     * @param productService - сервис Товара
     * @param productMapper - маппер Товара
     */
    public ProductController(ProductService productService, ProductMapper productMapper, AuthService authService, UserServiceImpl userService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.authService = authService;
        this.userService = userService;
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
        User user = authService.getAuthenticatedUser();
        if(!user.getRole().equals(Role.SUPPLIER)) {
            throw new AccessDeniedException("Только поставщик может создавать товар!");
        }

        productService.save(productDto, user);
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
    public String update(@PathVariable("id") Long id,
                         @Valid @RequestBody ProductSellerUpdateDTO productUpdateSellerDTO) {
        User user = authService.getAuthenticatedUser();

        if (!user.getRole().equals(Role.SELLER)) {
            throw new AccessDeniedException("Только продавец может менять описание и цену!");
        }

        productService.update(id, productUpdateSellerDTO, user);

        return String.format(
                "Продавец '%s' изменил стоимость и описание товара '%s'!",
                user.getLogin(), productService.findById(id).getName()
        );
    }

    /**
     * Удаление товара по id
     */
    @DeleteMapping("/{productId}")
    public String delete(@PathVariable("productId") Long productId) {
        User user = authService.getAuthenticatedUser();
        if(!user.getRole().equals(Role.SUPPLIER)) {
            throw new AccessDeniedException("Только поставщик может удалять товар!");
        }

        Product product = productService.findById(productId);
        if(!user.equals(product.getSupplier())) {
            throw new AccessDeniedException("Поставщик может удалять только свой товар!");
        }

        productService.delete(productId);
        return String.format("Товар '%s' удален поставщиком '%s'",
                product.getName(),
                authService.getAuthenticatedUser().getLogin());
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

    /**
     * Определение продавца для товара
     */
    @PostMapping("/{productId}/assign-seller")
    public String assignSeller(
            @PathVariable("productId") Long productId,
            @RequestBody AssignSellerDTO assignSellerDTO){
        User user = authService.getAuthenticatedUser();
        if(!user.getRole().equals(Role.SUPPLIER)) {
            throw new AccessDeniedException(
                    "Только поставщик может назначать продавца на товар");
        }

        User seller = userService.getUserById(assignSellerDTO.getSellerId());
        Product product = productService.findById(productId);
        if(!user.equals(product.getSupplier())){
            throw new AccessDeniedException(
                    "Поставщик может назначать продавцов только на свои товары");
        }

        productService.assignSeller(product, seller);
        return String.format(
                "Продавец '%s' назначен на товар '%s'",
                seller.getLogin(),
                product.getName());
    }

    @PostMapping("/{id}/buy")
    public String buy(@PathVariable("id") Long id) {

        User buyer = authService.getAuthenticatedUser();

        if (!buyer.getRole().equals(Role.BUYER)) {
            throw new AccessDeniedException("Покупать товары может только покупатель!");
        }

        productService.buyProduct(id, buyer);

        return String.format(
                "Покупатель '%s' купил товар '%s'",
                buyer.getLogin(),
                productService.findById(id).getName()
        );
    }
}
