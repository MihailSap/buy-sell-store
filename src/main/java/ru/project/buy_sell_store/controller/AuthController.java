package ru.project.buy_sell_store.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.project.buy_sell_store.dto.LoginDTO;
import ru.project.buy_sell_store.dto.RegisterDTO;
import ru.project.buy_sell_store.service.AuthService;
import ru.project.buy_sell_store.service.impl.AuthServiceImpl;

/**
 * Контроллер с эндпоинтами для регистрации, входа и выхода пользователя
 * @author SapeginMihail
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Интерфейс, предоставляющий методы для работы с аутентификацией
     */
    private final AuthService authService;

    /**
     * Конструктор контроллера для внедрения нужных зависимостей и создания экземпляра класса {@link AuthController}
     */
    @Autowired
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    /**
     * Эндпоинт для регистрации нового пользователя
     * <p>Возвращает статус 201 CREATED в случае успешной регистрации и создания аккаунта пользователя</p>
     * @param registerDTO DTO с полями, необходимыми для регистрации пользователя
     * @return строка с сообщением об успешной регистрации с упоминанием логина пользователя
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Validated @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return String.format("Пользователь %s зарегистрирован!", registerDTO.getLogin());
    }

    /**
     * Эндпоинт для входа пользователя в аккаунт
     * @param loginDTO DTO с полями, необходимыми для входа в аккаунт
     * @param session объект, в котором сохраняется информация об авторизованном пользователе
     * @return строка с приветствием по логину
     */
    @PostMapping("/login")
    public String login(@Validated @RequestBody LoginDTO loginDTO, HttpSession session){
        authService.login(loginDTO, session);
        return String.format("С возвращением, %s!", loginDTO.getLogin());
    }

    /**
     * Эндпоинт для выхода пользователя из аккаунта.
     * @param session объект, из которого удаляется информация об авторизованном пользователе после его выхода
     */
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        authService.logout(session);
    }
}
