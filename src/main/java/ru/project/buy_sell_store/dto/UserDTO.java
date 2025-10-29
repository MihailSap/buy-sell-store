package ru.project.buy_sell_store.dto;

import java.util.Date;

/**
 * DTO пользователя. Используется для передачи данных о пользователе
 * @param login логин пользователя
 * @param email адрес электронной почты пользователя
 * @param birthDate дата рождения пользователя
 * @param city город проживания пользователя
 * @param description описание аккаунта пользователя
 * @author SapeginMihail
 */
public record UserDTO(String login, String email, Date birthDate, String city, String description) {
}
