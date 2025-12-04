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
import ru.project.buySellStore.service.ProductService;
import ru.project.buySellStore.service.impl.ExpenseService;

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

    /**
     * Создание контроллера для просмотра расходов с внедрением нужных зависимостей
     */
    @Autowired
    public ExpenseController(
            ProductService productService,
            ExpenseService expenseService,
            AuthService authServiceImpl) {
        this.productService = productService;
        this.expenseService = expenseService;
        this.authServiceImpl = authServiceImpl;
    }

    /**
     * Получение расходов покупателя
     */
    @PostMapping
    public String getExpense(@RequestBody ReportDTO reportDTO){
        User buyer = authServiceImpl.getAuthenticatedUser();
        List<Product> products = productService.findByCategoryAndBuyer(reportDTO.getCategory(), buyer);
        int expenseSum = expenseService.getExpense(products);
        return String.format(
                "За все время вы потратили %d₽ на товары в категории %s",
                expenseSum, reportDTO.getCategory());
    }
}
