package ru.project.buySellStore.service.impl;

import org.springframework.stereotype.Service;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.service.ExpenseService;

import java.util.List;

/**
 * Cервис для расчета расхода
 */
@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Override
    public int getExpense(List<Product> products) {
        int expenseSum = 0;
        for (Product product : products) {
            expenseSum += product.getSellerCost();
        }
        return expenseSum;

    }
}
