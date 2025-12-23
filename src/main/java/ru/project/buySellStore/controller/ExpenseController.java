package ru.project.buySellStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.project.buySellStore.dto.ReportDTO;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.AuthService;
import ru.project.buySellStore.service.ExpenseService;
import ru.project.buySellStore.service.PeriodService;
import ru.project.buySellStore.service.ProductService;

import java.util.List;

/**
 * Контроллер для просмотра расходов
 */
@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ProductService productService;

    private final ExpenseService expenseService;

    private final AuthService authServiceImpl;

    private final PeriodService periodService;

    /**
     * Создание контроллера для просмотра расходов с внедрением нужных зависимостей
     */
    @Autowired
    public ExpenseController(
            ProductService productService,
            ExpenseService expenseService,
            AuthService authServiceImpl, PeriodService periodService) {
        this.productService = productService;
        this.expenseService = expenseService;
        this.authServiceImpl = authServiceImpl;
        this.periodService = periodService;
    }

    /**
     * Получение расходов покупателя
     */
    @PostMapping
    public String getExpense(@RequestBody ReportDTO reportDTO){
        User user = authServiceImpl.getAuthenticatedUser();
        List<Product> products = productService.
                findByBuyerAndCategoryAndBoughtDateBetween(
                        reportDTO.getCategory(), user, reportDTO.getPeriod());
        int income = expenseService.getExpense(products);
        String period = periodService.getPeriod(reportDTO.getPeriod());
        return String.format("%s вы потратили %s на товарах в категории %s",
                period, income, reportDTO.getCategory());
    }
}
