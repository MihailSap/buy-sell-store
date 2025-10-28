package ru.project.buy_sell_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.buy_sell_store.entity.Product;

/**
 *  Репозиторий для управления Товара
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
