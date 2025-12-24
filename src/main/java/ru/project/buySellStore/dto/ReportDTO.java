package ru.project.buySellStore.dto;

import ru.project.buySellStore.model.Period;

/**
 * DTO для передачи данных отчета о доходах или расходах
 */
public class ReportDTO {

    /**
     * Категория товаров
     */
    private String category;

    private Period period;

    /**
     * Конструктор для создания DTO с указанными категорией и периодом
     */
    public ReportDTO(String category, Period period) {
        this.category = category;
        this.period = period;
    }

    /**
     * Получить категорию
     */
    public String getCategory() {
        return category;
    }

    /**
     * Получить период
     */
    public Period getPeriod() {
        return period;
    }
}
