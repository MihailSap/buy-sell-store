package ru.project.buySellStore.service;

import jakarta.servlet.http.HttpSession;
import ru.project.buySellStore.dto.LoginDTO;
import ru.project.buySellStore.dto.RegisterDTO;
import ru.project.buySellStore.exception.userEx.UserAlreadyExistsException;
import ru.project.buySellStore.model.User;

/**
 * Интерфейс для сервиса, предоставляющего методы для работы с аутентификацией
 * @author SapeginMihail
 */
public interface AuthService {

    /**
     * Регистрация пользователя
     */
    void register(RegisterDTO registerDTO) throws UserAlreadyExistsException;

    /**
     * Вход пользователя в аккаунт
     */
    void login(LoginDTO loginDTO, HttpSession session);

    /**
     * Выход пользователя из аккаунта
     */
    void logout(HttpSession session);

    /**
     * Получение авторизованного пользователя
     * @return авторизованный пользователь
     */
    User getAuthenticatedUser();
}
