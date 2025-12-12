package ru.project.buySellStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.project.buySellStore.dto.ReportDTO;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.AuthService;
import ru.project.buySellStore.service.IncomeService;
import ru.project.buySellStore.service.ProductService;
import ru.project.buySellStore.service.impl.PeriodService;

import java.util.List;

/**
 * Контроллер для получения информации о доходе
 */
@RestController
@RequestMapping("/api/income")
public class IncomeController {

    private final IncomeService incomeService;

    private final AuthService authService;

    private final ProductService productService;
    private final PeriodService periodService;

    /**
     * Создание контроллера с нужными зависимостями
     * @param incomeService сервис подсчета дохода
     */
    @Autowired
    public IncomeController(
            IncomeService incomeService, AuthService authService, ProductService productService, PeriodService periodService) {
        this.incomeService = incomeService;
        this.authService = authService;
        this.productService = productService;
        this.periodService = periodService;
    }

    /**
     * Получить отчет о доходе исходя от параметров в ReportDTO
     */
    @PostMapping
    public String getIncome(@RequestBody ReportDTO reportDTO) {
        User user = authService.getAuthenticatedUser();
        int income = 0;
        if(user.getRole() == Role.SELLER) {
            List<Product> products = productService.
                    findBySellerAndCategoryAndBoughtDateBetween(
                            reportDTO.getCategory(), user, reportDTO.getPeriod());
            income = incomeService.calculateIncomeSeller(products);
        }

        if (user.getRole() == Role.SUPPLIER) {
            List<Product> products = productService.
                    findBySupplierAndCategoryAndBoughtDateBetween(
                            reportDTO.getCategory(), user, reportDTO.getPeriod());
            income = incomeService.calculateIncomeSupplier(products);
        }

        String period = periodService.getPeriod(reportDTO.getPeriod());
        return String.format("%s вы заработали %s на товарах в категории %s",
                period, income, reportDTO.getCategory());
    }
}
