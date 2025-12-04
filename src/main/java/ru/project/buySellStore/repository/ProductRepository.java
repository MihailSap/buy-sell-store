package ru.project.buySellStore.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.User;

import java.time.LocalDate;
import java.util.List;

/**
 *  Репозиторий для управления Товара
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Получить товары по продавцу и временному промежутку
     */
    List<Product> findBySellerAndBoughtDateBetween(User seller, LocalDate startDate, LocalDate endDate);

    /**
     * Получить товары по продавцу, категории и временному промежутку
     */
    List<Product> findBySellerAndCategoryAndBoughtDateBetween(User seller,
                                                              String category,
                                                              LocalDate startDate,
                                                              LocalDate endDate);

    /**
     * Получить товары по поставщику и временному промежутку
     */
    List<Product> findBySupplierAndBoughtDateBetween(User supplier, LocalDate startDate, LocalDate endDate);

    /**
     * Получить товары по поставщику, категории и временному промежутку
     */
    List<Product> findBySupplierAndCategoryAndBoughtDateBetween(User supplier,
                                                                String category,
                                                                LocalDate startDate,
                                                                LocalDate endDate);
}
