package ru.project.buySellStore.service.impl;

import org.springframework.stereotype.Service;
import ru.project.buySellStore.model.Product;

import java.util.List;

@Service
public class ExpenseService {

    public int getExpense(List<Product> products) {
        int expenseSum = 0;
        for (Product product : products) {
            expenseSum += product.getSellerCost();
        }
        return expenseSum;

    }
}
