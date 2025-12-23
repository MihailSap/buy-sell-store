package ru.project.buySellStore.service.impl;

import org.springframework.stereotype.Service;
import ru.project.buySellStore.model.DateRange;
import ru.project.buySellStore.service.PeriodService;

import java.time.LocalDate;

/**
 * Сервис для получения информации о периоде.
 * Используется для отчета
 */
@Service
public class PeriodServiceImpl implements PeriodService {

    @Override
    public String getPeriodDescription(String period){
        return switch (period) {
            case "TODAY" -> "За сегодня";
            case "LAST_WEEK" -> "За последнюю неделю";
            case "LAST_MONTH" -> "За последний месяц";
            default -> "За все время";
        };
    }

    @Override
    public DateRange getDateRange(String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (period) {
            case "TODAY" -> endDate;
            case "LAST_WEEK" -> endDate.minusWeeks(1);
            case "LAST_MONTH" -> endDate.minusMonths(1);
            case "ALL" -> LocalDate.of(1970, 1, 1);
            default -> throw new IllegalArgumentException("Неправильный формат периода: " + period);
        };
        return new DateRange(startDate, endDate);
    }
}
