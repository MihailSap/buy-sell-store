package ru.project.buySellStore.service.impl;

import org.springframework.stereotype.Service;
import ru.project.buySellStore.service.PeriodService;

/**
 * Сервис для получения строки с информацией о периоде.
 * Используется для отчета
 */
@Service
public class PeriodServiceImpl implements PeriodService {

    @Override
    public String getPeriod(String period){
        return switch (period) {
            case "TODAY" -> "За сегодня";
            case "LAST_WEEK" -> "За последнюю неделю";
            case "LAST_MONTH" -> "За последний месяц";
            default -> "За все время";
        };
    }
}
