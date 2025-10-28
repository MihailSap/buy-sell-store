package ru.project.buy_sell_store.model;

import jakarta.persistence.*;
import ru.project.buy_sell_store.enums.Role;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String login;

    private String email;

    private String password;

    private Date birthday;

    private String city;

    private String description;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(long id, String login, String email, String password, Date birthday, String city, String description, Role role) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
        this.birthday = birthday;
        this.city = city;
        this.description = description;
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthDate) {
        this.birthday = birthDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(login, user.login) && Objects.equals(email, user.email)
                && Objects.equals(password, user.password) && Objects.equals(birthday, user.birthday)
                && Objects.equals(city, user.city) && Objects.equals(description, user.description) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, email, password, birthday, city, description, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", role=" + role +
                '}';
    }
}
