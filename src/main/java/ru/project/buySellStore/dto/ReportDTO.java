package ru.project.buySellStore.dto;

/**
 * DTO для передачи данных отчета о доходах или расходах
 */
public class ReportDTO {
    private String category;

    private String period;

    /**
     * Конструктор для создания DTO с указанными категорией и периодом
     */
    public ReportDTO(String category, String period) {
        this.category = category;
        this.period = period;
    }

    /**
     * Получить категорию
     * @return
     */
    public String getCategory() {
        return category;
    }

    /**
     * Получить период
     */
    public String getPeriod() {
        return period;
    }
}
