package ru.project.buySellStore.service;

import ru.project.buySellStore.model.DateRange;

/**
 * Интерфейс для получения информации о периоде для отчета
 */
public interface PeriodService {

    /**
     * Получить краткое описание периода по его метке на русском языке
     */
    String getPeriodDescription(String period);

    /**
     * Получить диапазон дат по указанному периоду
     */
    DateRange getDateRange(String period);
}
