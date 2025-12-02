package ru.project.buySellStore.dto;

import jakarta.validation.constraints.Email;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.validation.annotations.Login;
import ru.project.buySellStore.validation.annotations.Password;

/**
 * DTO, отправляемое пользователем при регистрации
 * @author SapeginMihail
 */
public class RegisterDTO {

    /**
     * <b>Логин, под которым пользователь регистрируется</b>
     * <ul>
     *     <li>Должен состоять только из букв и цифр</li>
     *     <li>Не должен быть пустым</li>
     *     <li>Не должен быть длиннее 30 символов</li>
     * </ul>
     */
    @Login
    private String login;

    /**
     * Адрес электронной почты пользователя
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
     * Роль пользователя.
     * Определяется только при регистрации.
     * Может принимать только следующие значения: SUPPLIER, SELLER, BUYER
     */
    private Role role;

    /**
     * Конструктор
     * @param login логин, под которым пользователь регистрируется
     * @param email адрес электронной почты пользователя
     * @param password пароль пользователя
     * @param role роль пользователя
     */
    public RegisterDTO(String login, String email, String password, Role role) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Пустой конструктор
     */
    public RegisterDTO() {
    }

    /**
     * Метод для получения логина
     * @return логин
     */
    public String getLogin() {
        return login;
    }

    /**
     * Метод для получения адреса электронной почты
     * @return адрес электронной почты
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

    /**
     * Метод для получения роли
     * @return роль пользователя
     */
    public Role getRole() {
        return role;
    }
}
