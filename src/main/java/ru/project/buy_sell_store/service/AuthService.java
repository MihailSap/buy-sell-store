package ru.project.buy_sell_store.service;

import jakarta.servlet.http.HttpSession;
import ru.project.buy_sell_store.dto.LoginDTO;
import ru.project.buy_sell_store.dto.RegisterDTO;

/**
 * Интерфейс для сервиса, предоставляющего методы для работы с аутентификацией
 * @author SapeginMihail
 */
public interface AuthService {

    /**
     * Регистрация пользователя
     */
    void register(RegisterDTO registerDTO);

    /**
     * Вход пользователя в аккаунт
     */
    void login(LoginDTO loginDTO, HttpSession session);

    /**
     * Выход пользователя из аккаунта
     */
    void logout(HttpSession session);
}
