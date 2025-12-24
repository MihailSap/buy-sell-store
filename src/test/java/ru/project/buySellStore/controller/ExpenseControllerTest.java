package ru.project.buySellStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.project.buySellStore.dto.ReportDTO;
import ru.project.buySellStore.mapper.PeriodMapper;
import ru.project.buySellStore.model.*;
import ru.project.buySellStore.service.AuthService;
import ru.project.buySellStore.service.ExpenseService;
import ru.project.buySellStore.service.ProductService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Тесты для {@link ExpenseController}
 */
@WebMvcTest(ExpenseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ExpenseService expenseService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private PeriodMapper periodMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Product> products;

    private ReportDTO reportDTO;

    private DateRange dateRange;

    /**
     * Создание объектов перед тестом
     */
    @BeforeEach
    void setUp(){
        products = new ArrayList<>();
        reportDTO = new ReportDTO("FOOD", Period.LAST_MONTH);
        dateRange = new DateRange(LocalDate.now().minusMonths(1), LocalDate.now());
    }

    /**
     * Проверка получения расходов покупателя
     */
    @Test
    void testGetExpense() throws Exception {
        User user = new User();
        user.setRole(Role.BUYER);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(user);
        Mockito.when(periodMapper.mapPeriodToDateRange(Period.LAST_MONTH))
                .thenReturn(dateRange);
        Mockito.when(productService.findByBuyerAndCategoryAndBoughtDateBetween("FOOD", user, dateRange))
                .thenReturn(products);
        Mockito.when(expenseService.getExpense(products))
                .thenReturn(1500);
        Mockito.when(periodMapper.getPeriodDescription(Period.LAST_MONTH))
                .thenReturn("За последний месяц");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("За последний месяц вы потратили 1500 на товарах в категории FOOD"));

        Mockito.verify(productService)
                .findByBuyerAndCategoryAndBoughtDateBetween(
                        Mockito.eq("FOOD"), Mockito.eq(user), Mockito.eq(dateRange));
        Mockito.verify(expenseService)
                .getExpense(products);
        Mockito.verify(periodMapper)
                .getPeriodDescription(Period.LAST_MONTH);
    }
}
