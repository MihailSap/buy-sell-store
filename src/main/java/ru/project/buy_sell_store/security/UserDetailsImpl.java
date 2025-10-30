package ru.project.buy_sell_store.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.project.buy_sell_store.model.User;

import java.util.Collection;
import java.util.List;

/**
 * <b>Реализация интерфейса {@link UserDetails}.</b>
 * Отвечает за представление аутентифицированного пользователя в Spring Security.
 * Содержит имя пользователя, пароль, роли и другие данные об аутентифицированном пользователе
 * @author SapeginMihail
 */
public class UserDetailsImpl implements UserDetails {

    /**
     * Объект {@link User} представляющий аутентифицированного пользователя
     */
    private final User user;

    /**
     * Конструктор для создания экземпляра класса {@link UserDetailsImpl}
     * @param user объект {@link User} представляющий аутентифицированного пользователя
     */
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    /**
     * Получение полномочий пользователя.
     * @return коллекция полномочий
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Получение пароля пользователя
     * @return пароль пользователя
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Получает username пользователя.
     * В данной реализации, вместо username получает логин пользователя.
     * @return username, то есть login пользователя
     */
    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    /**
     * Проверяет, истёк ли срок действия аккаунта пользователя.
     * Всегда возвращает {@code true}, так как в данной реализации аккаунт пользователя не имеет срока действия.
     * @return {@code true}, если срок действия аккаунта не истек
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт пользователя.
     * Всегда возвращает {@code true}, так как в данной реализации отсутствует блокировка аккаунта.
     * @return {@code true}, если аккаунт не заблокирован
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных.
     * Всегда возвращает {@code true}, так как в данной реализации учетные данные пользователя не имеют срока действия.
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет доступен ли аккаунт.
     * Всегда возвращает {@code true}, так как в данной реализации аккаунт всегда доступен.
     * @return {@code true}, если аккаунт доступен
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Получение {@link User}
     * @return объект {@link User}
     */
    public User getUser() {
        return user;
    }
}
