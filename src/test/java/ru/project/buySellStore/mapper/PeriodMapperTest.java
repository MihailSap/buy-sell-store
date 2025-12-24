package ru.project.buySellStore.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.buySellStore.model.DateRange;
import ru.project.buySellStore.model.Period;

import java.time.LocalDate;

/**
 * Тесты для {@link PeriodMapper}
 */
public class PeriodMapperTest {

    private PeriodMapper periodMapper;

    /**
     * Настройка тестовых данных перед каждым тестом
     */
    @BeforeEach
    void setUp(){
        periodMapper = new PeriodMapper();
    }

    /**
     * Проверяется корректность преобразования периода в его описание на русском языке для всех случаев
     */
    @Test
    void testGetPeriodDescription(){
        Assertions.assertEquals("За сегодня", periodMapper.getPeriodDescription(Period.TODAY));
        Assertions.assertEquals("За последнюю неделю", periodMapper.getPeriodDescription(Period.LAST_WEEK));
        Assertions.assertEquals("За последний месяц", periodMapper.getPeriodDescription(Period.LAST_MONTH));
        Assertions.assertEquals("За последний год", periodMapper.getPeriodDescription(Period.LAST_YEAR));
        Assertions.assertEquals("За все время", periodMapper.getPeriodDescription(Period.ALL));
    }

    /**
     * Проверяется корректность преобразования периода {@link Period#TODAY} в диапазон дат
     */
    @Test
    void testMapTodayPeriodToDateRange() {
        LocalDate now = LocalDate.now();
        DateRange result = periodMapper.mapPeriodToDateRange(Period.TODAY);
        Assertions.assertEquals(now, result.getStartDate());
        Assertions.assertEquals(now, result.getEndDate());
    }

    /**
     * Проверяется корректность преобразования периода {@link Period#LAST_WEEK} в диапазон дат
     */
    @Test
    void testMapLastWeekPeriodToDateRange() {
        LocalDate now = LocalDate.now();
        DateRange result = periodMapper.mapPeriodToDateRange(Period.LAST_WEEK);
        Assertions.assertEquals(now.minusWeeks(1), result.getStartDate());
        Assertions.assertEquals(now, result.getEndDate());
    }

    /**
     * Проверяется корректность преобразования периода {@link Period#LAST_MONTH} в диапазон дат
     */
    @Test
    void testMapLastMonthPeriodToDateRange() {
        LocalDate now = LocalDate.now();
        DateRange result = periodMapper.mapPeriodToDateRange(Period.LAST_MONTH);
        Assertions.assertEquals(now.minusMonths(1), result.getStartDate());
        Assertions.assertEquals(now, result.getEndDate());
    }

    /**
     * Проверяется корректность преобразования периода {@link Period#LAST_YEAR} в диапазон дат
     */
    @Test
    void testMapLastYearPeriodToDateRange() {
        LocalDate now = LocalDate.now();
        DateRange result = periodMapper.mapPeriodToDateRange(Period.LAST_YEAR);
        Assertions.assertEquals(now.minusYears(1), result.getStartDate());
        Assertions.assertEquals(now, result.getEndDate());
    }

    /**
     * Проверяется корректность преобразования периода {@link Period#ALL} в диапазон дат
     */
    @Test
    void testMapAllPeriodToDateRange() {
        LocalDate now = LocalDate.now();
        DateRange result = periodMapper.mapPeriodToDateRange(Period.ALL);
        Assertions.assertEquals(LocalDate.of(1970, 1, 1), result.getStartDate());
        Assertions.assertEquals(now, result.getEndDate());
    }
}
