package ru.project.buy_sell_store.dto;

import jakarta.validation.constraints.Email;
import ru.project.buy_sell_store.validation.annotations.Password;

/**
 * DTO, отправляемое пользователем при входе
 */
public class LoginDTO {

    /**
     * Адрес электронной почты, под которым зарегистрирован пользователь
     */
    @Email(message = "Адрес электронной почты должен быть корректным, например: user@gmail.com")
    private String email;

    /**
     * Пароль пользователя.
     * Определяется только при регистрации.
     * Не должен быть длиннее 30 символов
     */
    @Password
    private String password;

    /**
     * Конструктор
     * @param email адрес электронной почты
     * @param password пароль
     */
    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Пустой конструктор
     */
    public LoginDTO() {
    }

    /**
     * Метод для получения адреса электронной почты
     * @return логин
     */
    public String getEmail() {
        return email;
    }

    /**
     * Метод для получения пароля
     * @return пароль
     */
    public String getPassword() {
        return password;
    }
}
