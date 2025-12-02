package ru.project.buySellStore.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.project.buySellStore.model.User;

import java.util.Collection;
import java.util.List;

/**
 * <b>Реализация интерфейса {@link UserDetails}.</b>
 * Отвечает за представление аутентифицированного пользователя в Spring Security.
 * Содержит имя пользователя, пароль, роли и другие данные об аутентифицированном пользователе
 * @author SapeginMihail
 */
public class UserDetailsImpl implements UserDetails {

    private final User user;

    /**
     * Конструктор для создания экземпляра класса {@link UserDetailsImpl}
     * @param user объект {@link User} представляющий аутентифицированного пользователя
     */
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

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
