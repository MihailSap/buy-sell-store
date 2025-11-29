package ru.project.buySellStore.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * <b>Сущность пользователя</b>
 * @author SapeginMihail
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * <b>id пользователя.</b>
     * <p>Определяется при создании сущности</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * <b>Логин</b>
     * <p>Именно по нему пользователь выполняет вход</p>
     */
    private String login;

    /**
     * <b>Адрес электронной почты</b>
     * <p>Определяется пользователем при регистрации</p>
     */
    private String email;

    /**
     * <b>Пароль</b>
     * <p>Определяется пользователем при регистрации. Хранится в зашифрованном виде.</p>
     */
    private String password;

    /**
     * <b>Дата рождения</b>
     * <p>Определяется пользователем при обновлении профиля.</p>
     */
    private Date birthDate;

    /**
     * <b>Город проживания</b>
     * <p>Определяется пользователем при обновлении профиля.</p>
     */
    private String city;

    /**
     * <b>Описание аккаунта</b>
     * <p>Определяется пользователем при обновлении профиля.</p>
     */
    private String description;

    /**
     * <b>Роль</b>
     * <p>Определяется пользователем при регистрации. Сохраняется в БД как строка.</p>
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * <b>Товары поставщика</b>
     * <p>Список товаров, которые поставщик создал</p>
     */
    @OneToMany(mappedBy = "supplier")
    private Set<Product> suppliedProducts = new HashSet<>();

    /**
     * <b>Товары продавца</b>
     * <p>Список товаров, которые продавец продает</p>
     */
    @OneToMany(mappedBy = "seller")
    private Set<Product> sellingProducts = new HashSet<>();

    /**
     * <b>Товары покупателя</b>
     * <p>Список товаров, которые покупатель купил</p>
     */
    @OneToMany(mappedBy = "buyer")
    private Set<Product> boughtProducts = new HashSet<>();

    /**
     * Конструктор
     */
    public User(long id, String login, String email, String password, Date birthDate, String city, String description, Role role) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
        this.birthDate = birthDate;
        this.city = city;
        this.description = description;
    }

    /**
     * Пустой конструктор
     * Необходим для сущности
     */
    public User() {

    }

    /**
     * Метод получения id пользователя
     */
    public long getId() {
        return id;
    }

    /**
     * Метод определения id пользователя
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Метод получения логина пользователя
     */
    public String getLogin() {
        return login;
    }

    /**
     * Метод определения логина пользователя
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Метод получения адреса электронной почты пользователя
     */
    public String getEmail() {
        return email;
    }

    /**
     * Метод определения адреса электронной почты пользователя
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Метод получения пароля пользователя
     */
    public String getPassword() {
        return password;
    }

    /**
     * Метод определения пароля пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Метод получения роли пользователя
     */
    public Role getRole() {
        return role;
    }

    /**
     * Метод определения роли пользователя
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Метод получения даты рождения пользователя
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Метод определения даты рождения пользователя
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Метод получения города проживания пользователя
     */
    public String getCity() {
        return city;
    }

    /**
     * Метод определения города проживания пользователя
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Метод получения описания пользователя
     */
    public String getDescription() {
        return description;
    }

    /**
     * Метод определения описания пользователя
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthDate +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", role=" + role +
                '}';
    }
}
