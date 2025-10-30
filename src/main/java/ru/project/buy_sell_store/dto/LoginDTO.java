package ru.project.buy_sell_store.dto;

import ru.project.buy_sell_store.validation.annotations.Login;
import ru.project.buy_sell_store.validation.annotations.Password;

/**
 * DTO, отправляемое пользователем при входе
 */
public class LoginDTO {

    /**
     * <b>Логин, под которым зарегистрирован пользователь</b>
     * <ul>
     *     <li>Должен состоять только из букв и цифр</li>
     *     <li>Не должен быть пустым</li>
     *     <li>Не должен быть длиннее 30 символов</li>
     * </ul>
     */
    @Login
    private String login;

    /**
     * Пароль пользователя.
     * Определяется только при регистрации.
     * Не должен быть длиннее 30 символов
     */
    @Password
    private String password;

    /**
     * Конструктор
     * @param login логин
     * @param password пароль
     */
    public LoginDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Пустой конструктор
     */
    public LoginDTO() {
    }

    /**
     * Метод для получения логина
     * @return логин
     */
    public String getLogin() {
        return login;
    }

    /**
     * Метод для получения пароля
     * @return пароль
     */
    public String getPassword() {
        return password;
    }
}
