package ru.project.buySellStore.model;

import java.time.LocalDate;

/**
 * Диапазон дат, включающий в себя начальную и конечную дату
 */
public class DateRange {

    /**
     * Дата начала диапазона
     */
    private LocalDate startDate;

    /**
     * Дата окончания диапазона
     */
    private LocalDate endDate;

    /**
     * Конструктор для создания диапазона дат
     */
    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Получить дату начала диапазона
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Установить дату начала диапазона
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Получить дату окончания диапазона
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Установить дату окончания диапазона
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
