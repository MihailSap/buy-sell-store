package ru.project.buySellStore.service.impl;

import org.springframework.stereotype.Service;

/**
 * Сервис для получения строки с информацией о периоде.
 * Используется для отчета
 */
@Service
public class PeriodService {

    /**
     * Получение информации о периоде
     */
    public String getPeriod(String period){
        return switch (period) {
            case "TODAY" -> "За сегодня";
            case "LAST_WEEK" -> "За последнюю неделю";
            case "LAST_MONTH" -> "За последний месяц";
            default -> "За все время";
        };
    }
}
