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
import ru.project.buySellStore.service.IncomeService;
import ru.project.buySellStore.service.ProductService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Тесты для {@link IncomeController}
 */
@WebMvcTest(IncomeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class IncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IncomeService incomeService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private PeriodMapper periodMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ReportDTO reportDTO;

    private List<Product> products;

    private DateRange dateRange;

    /**
     * Создание объектов и определение моков, которые необходимы для всех тестов
     */
    @BeforeEach
    void setUp(){
        reportDTO = new ReportDTO( "CLOTHES", Period.LAST_WEEK);
        products = new ArrayList<>();
        dateRange = new DateRange(LocalDate.now().minusWeeks(1), LocalDate.now());

        Mockito.when(periodMapper.mapPeriodToDateRange(Period.LAST_WEEK))
                .thenReturn(dateRange);
        Mockito.when(periodMapper.getPeriodDescription(Period.LAST_WEEK))
                .thenReturn("За последнюю неделю");
    }

    /**
     * Проверка получения отчета о доходе для продавца
     */
    @Test
    void testGetIncomeSeller() throws Exception {
        User seller = new User();
        seller.setRole(Role.SELLER);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(seller);
        Mockito.when(incomeService.calculateIncomeSeller(products))
                .thenReturn(10000);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        "За последнюю неделю вы заработали 10000 на товарах в категории CLOTHES"
                ));

        Mockito.verify(productService)
                .findBySellerAndCategoryAndBoughtDateBetween(
                        Mockito.eq("CLOTHES"), Mockito.eq(seller), Mockito.eq(dateRange));
        Mockito.verify(incomeService)
                .calculateIncomeSeller(products);
        Mockito.verify(periodMapper)
                .mapPeriodToDateRange(Period.LAST_WEEK);
        Mockito.verify(productService, Mockito.never())
                .findBySupplierAndCategoryAndBoughtDateBetween(
                        Mockito.any(String.class), Mockito.any(User.class), Mockito.any(DateRange.class));
    }

    /**
     * Проверка получения отчета о доходе для поставщика
     */
    @Test
    void testGetIncomeSupplier() throws Exception {
        User supplier = new User();
        supplier.setRole(Role.SUPPLIER);

        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(supplier);
        Mockito.when(incomeService.calculateIncomeSupplier(products))
                .thenReturn(10000);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        "За последнюю неделю вы заработали 10000 на товарах в категории CLOTHES"
                ));

        Mockito.verify(productService)
                .findBySupplierAndCategoryAndBoughtDateBetween(
                        Mockito.eq("CLOTHES"), Mockito.eq(supplier), Mockito.eq(dateRange));
        Mockito.verify(incomeService)
                .calculateIncomeSupplier(products);
        Mockito.verify(periodMapper)
                .mapPeriodToDateRange(Period.LAST_WEEK);
        Mockito.verify(productService, Mockito.never())
                .findBySellerAndCategoryAndBoughtDateBetween(
                        Mockito.any(String.class), Mockito.any(User.class), Mockito.any(DateRange.class));
    }
}
