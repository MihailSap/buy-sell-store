package ru.project.buySellStore.model;

/**
 * Период, по которому пользователь может получить отчет
 */
public enum Period {

    /**
     * Текущий день
     */
    TODAY,

    /**
     * Последняя неделя
     */
    LAST_WEEK,

    /**
     * Последний месяц
     */
    LAST_MONTH,

    /**
     * Последний год
     */
    LAST_YEAR,

    /**
     * Все время
     */
    ALL
}
