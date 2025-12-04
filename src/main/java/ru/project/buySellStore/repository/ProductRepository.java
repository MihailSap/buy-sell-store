package ru.project.buySellStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.User;

import java.util.List;

/**
 *  Репозиторий для управления товаром
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Получение товаров определенной категории, купленных покупателем
     */
    List<Product> findByCategoryAndBuyer(String category, User buyer);

    /**
     * Получение товаров, купленных покупателем
     */
    List<Product> findByBuyer(User buyer);
}
