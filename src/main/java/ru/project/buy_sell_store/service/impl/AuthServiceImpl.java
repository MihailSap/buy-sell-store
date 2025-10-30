package ru.project.buy_sell_store.service.impl;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.project.buy_sell_store.dto.LoginDTO;
import ru.project.buy_sell_store.dto.RegisterDTO;
import ru.project.buy_sell_store.service.AuthService;
import ru.project.buy_sell_store.service.UserService;

/**
 * Сервис, отвечающий за аутентификацию пользователей.
 * Реализует интерфейс {@link AuthService}.
 * Используется для регистрации, входа и выхода.
 * @author SapeginMihail
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Интерфейс для обработки аутентификации
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Интерфейс для работы с User.
     * Позволяет создавать аккаунт для нового пользователя
     */
    private final UserService userService;

    /**
     * Конструктор для внедрения нужных зависимостей и создания экземпляра класса AuthServiceImpl
     */
    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public void register(RegisterDTO registerDTO){
        userService.create(registerDTO);
    }

    @Override
    public void login(LoginDTO loginDTO, HttpSession session){
        UsernamePasswordAuthenticationToken authenticationInputToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getLogin(), loginDTO.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationInputToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    @Override
    public void logout(HttpSession session){
        session.invalidate();
        SecurityContextHolder.clearContext();
    }
}
