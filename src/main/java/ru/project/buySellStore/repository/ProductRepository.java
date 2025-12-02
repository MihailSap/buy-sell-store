package ru.project.buySellStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.buySellStore.model.Product;

/**
 *  Репозиторий для управления Товара
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
