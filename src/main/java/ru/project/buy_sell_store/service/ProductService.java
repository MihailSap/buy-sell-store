package ru.project.buy_sell_store.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buy_sell_store.dto.ProductDTO;
import ru.project.buy_sell_store.entity.Product;
import ru.project.buy_sell_store.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Севрис для управление Товара
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Сохранить товар в базу данных
     */
    @Transactional
    public ProductDTO save(ProductDTO productDto) {
        Product savedProduct = productRepository.save(toEntity(productDto));

        return toDto(savedProduct);
    }

    /**
     * Получить все товары из базы данных
     */
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Получить товар из базы данных по id
     */
    public ProductDTO findById(Long id) {
       Product product = productRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Товар не найден"));
       return toDto(product);
    }

    /**
     * Обновить товар из базы данных по id
     */
    @Transactional
    public ProductDTO update(Long id, ProductDTO productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCost(productDto.getCost());

        Product savedProduct = productRepository.save(product);
        return toDto(savedProduct);
    }

    /**
     * Удалить товар по id
     */
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Архивировать товар по id
     */
    @Transactional
    public ProductDTO archive(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if(product.isArchived()) {
            throw new RuntimeException("Товар находится уже в архиве");
        }

        product.setArchived(true);
        Product savedProduct = productRepository.save(product);
        return toDto(savedProduct);
    }

    /**
     * Восстановить из архива по id
     */
    @Transactional
    public ProductDTO restore(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (!product.isArchived()) {
            throw new RuntimeException("Товар уже доступен и не находится в архиве");
        }

        product.setArchived(false);
        Product savedProduct = productRepository.save(product);
        return toDto(savedProduct);
    }

    /**
     * Преобразование товара из DTO в Entity
     */
    private Product toEntity(ProductDTO productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setCost(productDto.getCost());
        return product;
    }

    /**
     * Преобразование товара из Entity в DTO
     */
    private ProductDTO toDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(product.getCategory());
        productDTO.setCost(product.getCost());
        return productDTO;
    }
}
