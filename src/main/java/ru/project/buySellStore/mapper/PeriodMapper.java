package ru.project.buySellStore.mapper;

import org.springframework.stereotype.Component;
import ru.project.buySellStore.model.DateRange;
import ru.project.buySellStore.model.Period;

import java.time.LocalDate;

/**
 * Маппер для преобразования периода в нужный формат
 */
@Component
public class PeriodMapper {

    public String getPeriodDescription(Period period){
        return switch (period) {
            case Period.TODAY -> "За сегодня";
            case Period.LAST_WEEK -> "За последнюю неделю";
            case Period.LAST_MONTH -> "За последний месяц";
            case Period.ALL -> "За все время";
        };
    }

    public DateRange mapPeriodToDateRange(Period period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch (period) {
            case Period.TODAY -> endDate;
            case Period.LAST_WEEK -> endDate.minusWeeks(1);
            case Period.LAST_MONTH -> endDate.minusMonths(1);
            case Period.ALL -> LocalDate.of(1970, 1, 1);
        };
        return new DateRange(startDate, endDate);
    }
}
