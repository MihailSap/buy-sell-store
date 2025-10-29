package ru.project.buy_sell_store.dto;

import ru.project.buy_sell_store.enums.Role;

/**
 * DTO, отправляемое пользователем при регистрации
 * @param login логин, под которым пользователь регистрируется
 * @param email адрес электронной почты пользователя
 * @param password пароль пользователя. Определяется только при регистрации
 * @param role роль пользователя. Определяется только при регистрации
 * @author SapeginMihail
 */
public record RegisterDTO(String login, String email, String password, Role role) {
}
