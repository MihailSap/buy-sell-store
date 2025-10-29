package ru.project.buy_sell_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.project.buy_sell_store.model.User;
import ru.project.buy_sell_store.repository.UserRepository;
import ru.project.buy_sell_store.security.UserDetailsImpl;

/**
 * Реализация интерфейса {@link UserDetailsService}.
 * Используется Spring Security для получения информации о пользователе в виде объекта {@link UserDetails}
 * @author SapeginMihail
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Репозиторий для работы с сущностью {@link User}
     */
    private final UserRepository userRepository;

    /**
     * Конструктор для внедрения нужных зависимостей и создания экземпляра класса {@link UserDetailsServiceImpl}.
     * @param userRepository репозиторий для работы с сущностью {@link User}
     */
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получение из БД информации о пользователе в виде объекта {@link UserDetails}
     * @param login логин пользователя
     * @return объект {@link UserDetails} для найденного {@link User}
     * @throws UsernameNotFoundException появляется, когда пользователь с таким логином не найден
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));
        return new UserDetailsImpl(user);
    }
}
