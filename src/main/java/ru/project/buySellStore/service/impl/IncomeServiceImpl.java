package ru.project.buySellStore.service.impl;

import org.springframework.stereotype.Service;
import ru.project.buySellStore.dto.ReportDTO;
import ru.project.buySellStore.model.Product;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.IncomeService;

import java.util.List;

@Service
public class IncomeServiceImpl implements IncomeService {

    @Override
    public int calculateIncomeSeller(List<Product> products) {
        int income = 0;
        for (Product product : products) {
            if (product.getBuyer() != null) {
                int difference = product.getSellerCost() - product.getSupplierCost();
                income += difference;
            }
        }
        return income;
    }

    @Override
    public int calculateIncomeSupplier(List<Product> products) {
        int income = 0;
        for (Product product : products) {
            if (product.getBuyer() != null) {
                income += product.getSupplierCost();
            }
        }
        return income;
    }
}
