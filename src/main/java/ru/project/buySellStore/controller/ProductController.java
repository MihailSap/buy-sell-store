package ru.project.buySellStore.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.project.buySellStore.dto.AssignSellerDTO;
import ru.project.buySellStore.dto.ProductDTO;
import ru.project.buySellStore.dto.ProductSellerUpdateDTO;
import ru.project.buySellStore.dto.ProductSupplierUpdateDTO;
import ru.project.buySellStore.dto.productView.ProductViewDTO;
import ru.project.buySellStore.exception.productEx.*;
import ru.project.buySellStore.exception.userEx.UserNotFoundException;
import ru.project.buySellStore.exception.userEx.UserNotSuitableRoleException;
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
 * Контроллер для управления товаром
 */
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
     * <p>Пользователь получает только свой товар<p>
     */
    @GetMapping
    @Transactional(readOnly = true)
    public List<ProductViewDTO> findAll() {
        User user = authService.getAuthenticatedUser();
        List<Product> products = productService.findAll(user);
        return products.stream()
                .map(p -> productMapper.toDtoByRole(p, user.getRole()))
                .collect(Collectors.toList());
    }

    /**
     * Получение товара по id
     * <p>Пользователь получает только свой товар<p>
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ProductViewDTO findById(@PathVariable("id") Long id) throws ProductNotFoundException {
        User user = authService.getAuthenticatedUser();
        Product product = productService.findById(id, user);
        return productMapper.toDtoByRole(product, user.getRole());
    }

    /**
     * Создание нового товара
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public String create(@Valid @RequestBody ProductDTO productDto) {
        User user = authService.getAuthenticatedUser();
        if(!user.getRole().equals(Role.SUPPLIER)) {
            throw new AccessDeniedException("Только поставщик может создавать товар!");
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setSupplierCost(productDto.getSupplierCost());
        product.setSupplier(user);
        productService.save(product);
        return String.format(
                "Поставщик '%s' создал товар '%s'",
                authService.getAuthenticatedUser().getLogin(), productDto.getName());
    }

    /**
     * <b>Обновление товара по {@code id}</b>
     * <p>Для пользователя с ролью {@link Role#SELLER}</p>
     */
    @PatchMapping("/{id}/seller")
    @Transactional
    public String updateBySeller(@PathVariable("id") Long id,
                         @Valid @RequestBody ProductSellerUpdateDTO productUpdateSellerDTO)
            throws ProductNotFoundException {
        User user = authService.getAuthenticatedUser();
        if (!user.getRole().equals(Role.SELLER)) {
            throw new AccessDeniedException("Только продавец может менять описание и цену!");
        }

        Product product = productService.findById(id);
        if (product.getSeller() == null || !product.getSeller().equals(user)) {
            throw new AccessDeniedException("Этот товар не назначен вам!");
        }
        product.setDescription(productUpdateSellerDTO.getDescription());
        product.setSellerCost(productUpdateSellerDTO.getSellerCost());
        productService.save(product);
        return String.format(
                "Продавец '%s' изменил стоимость и описание товара '%s'!",
                user.getLogin(), productService.findById(id).getName()
        );
    }

    /**
     * <b>Обновление товара по id </b>
     * <p>Для пользователя с ролью {@link Role#SUPPLIER}</p>
     */
    @PatchMapping("/{id}/supplier")
    @Transactional
    public String updateBySupplier(
            @PathVariable Long id,
            @Valid @RequestBody ProductSupplierUpdateDTO productSupplierUpdateDTO)
            throws ProductNotFoundException {

        User supplier = authService.getAuthenticatedUser();
        if (!supplier.getRole().equals(Role.SUPPLIER)) {
            throw new AccessDeniedException(
                    "Только поставщик может редактировать название, описание и изначальную цену товара!");
        }

        Product product = productService.findById(id);

        if (!product.getSupplier().equals(supplier)) {
            throw new AccessDeniedException("Поставщик может изменять только свои товары!");
        }

        if (product.getSeller() != null) {
            throw new AccessDeniedException(
                    "Поставщик не может редактировать товар после назначения продавца!"
            );
        }

        product.setName(productSupplierUpdateDTO.getName());
        product.setDescription(productSupplierUpdateDTO.getDescription());
        product.setSupplierCost(productSupplierUpdateDTO.getSupplierCost());

        productService.save(product);
        return String.format(
                "Поставщик '%s' изменил товар '%s'",
                supplier.getLogin(),
                productSupplierUpdateDTO.getName()
        );
    }

    /**
     * Удаление товара по id
     */
    @DeleteMapping("/{productId}")
    @Transactional
    public String delete(@PathVariable("productId") Long productId) throws ProductNotFoundException {
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

    /**
     * Определение продавца для товара
     */
    @PostMapping("/{productId}/assign-seller")
    @Transactional
    public String assignSeller(
            @PathVariable("productId") Long productId,
            @RequestBody AssignSellerDTO assignSellerDTO) throws UserNotFoundException,
            ProductNotFoundException, UserNotSuitableRoleException {
        User user = authService.getAuthenticatedUser();
        if(!user.getRole().equals(Role.SUPPLIER)) {
            throw new AccessDeniedException(
                    "Только поставщик может назначать продавца на товар");
        }

        User seller = userService.getUserById(assignSellerDTO.getSellerId());
        Product product = productService.findById(productId);
        if(!user.equals(product.getSupplier())){
            throw new AccessDeniedException(
                    "Поставщик может назначать продавца только на свой товар");
        }

        productService.assignSeller(product, seller);
        return String.format(
                "Продавец '%s' назначен на товар '%s'",
                seller.getLogin(),
                product.getName());
    }

    @PostMapping("/{id}/buy")
    @Transactional
    public String buy(@PathVariable("id") Long id) 
      throws ProductNotFoundException, ProductWithoutSellerException, ProductArchiveException, ProductAlreadyBoughtException {

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
