package ru.project.buySellStore.dto;

import jakarta.validation.constraints.Email;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.validation.annotations.BirthDate;
import ru.project.buySellStore.validation.annotations.Login;

import java.util.Date;

/**
 * DTO пользователя. Используется для передачи данных о пользователе
 */
public class UserDTO {

    /**
     * Пользователя id
     */
    private Long id;

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
     * Адрес электронной почты пользователя.
     * Должен соответствовать шаблону адреса электронной почты
     */
    @Email(message = "Адрес электронной почты должен быть корректным, например: user@gmail.com")
    private String email;

    /**
     * Дата рождения пользователя.
     * Должна быть введена в формате yyyy-mm-dd.
     * Должна быть в прошедшем времени
     */
    @BirthDate
    private Date birthDate;

    /**
     * Город проживания пользователя
     */
    private String city;

    /**
     * Описание аккаунта пользователя
     */
    private String description;

    /**
     * Конструктор для создания пользователя
     * @param login логин
     * @param email адрес электронной почты
     * @param birthDate дата рождения
     * @param city город проживания
     * @param description описание аккаунта
     */
    public UserDTO(String login, String email, Date birthDate, String city, String description) {
        this.login = login;
        this.email = email;
        this.birthDate = birthDate;
        this.city = city;
        this.description = description;
    }

    /**
     * Конструктор с id и всем полями для получения информации
     * о пользователе
     */
    public UserDTO(Long id, String login, String email, Date birthDate, String city, String description) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.birthDate = birthDate;
        this.city = city;
        this.description = description;
    }

    /**
     * Пустой конструктор
     */
    public UserDTO() {
    }

    /**
     * Установить id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить id
     */
    public Long getId() {
        return id;
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
     * Метод для получения даты рождения
     * @return дата рождения
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Метод для получения города проживания
     * @return город проживания
     */
    public String getCity() {
        return city;
    }

    /**
     * Метод для получения описания аккаунта
     * @return описание аккаунта
     */
    public String getDescription() {
        return description;
    }
}
