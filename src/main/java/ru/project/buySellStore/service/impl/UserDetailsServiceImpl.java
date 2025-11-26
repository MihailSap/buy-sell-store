package ru.project.buySellStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.repository.UserRepository;
import ru.project.buySellStore.security.UserDetailsImpl;

/**
 * Реализация интерфейса {@link UserDetailsService}.
 * Используется Spring Security для получения информации о пользователе в виде объекта {@link UserDetails}
 * @author SapeginMihail
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Конструктор для внедрения нужных зависимостей и создания экземпляра класса {@link UserDetailsServiceImpl}.
     * @param userRepository репозиторий для работы с сущностью {@link User}
     */
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким email не найден!"));
        return new UserDetailsImpl(user);
    }
}
