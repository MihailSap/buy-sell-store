package ru.project.buy_sell_store.dto;

/**
 * DTO ошибки
 */
public class ErrorDTO {

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
