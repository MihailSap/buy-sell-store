package ru.project.buy_sell_store.dto;

/**
 * DTO, отправляемое пользователем при входе
 * @param login логин, под которым зарегистрирован пользователь
 * @param password пароль пользователя
 * @author SapeginMihail
 */
public record LoginDTO(String login, String password) {
}
