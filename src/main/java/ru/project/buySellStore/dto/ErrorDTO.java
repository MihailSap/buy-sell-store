package ru.project.buySellStore.dto;

/**
 * DTO ошибки
 */
public class ErrorDTO {

    /**
     * Сообщение об ошибке
     */
    private String message;

    /**
     * Создание с указанным сообщением об ошибке
     */
    public ErrorDTO(String message) {
        this.message = message;
    }

    /**
     * Получить сообщение об ошибке
     */
    public String getMessage() {
        return message;
    }
}
