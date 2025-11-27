package ru.project.buySellStore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.project.buySellStore.dto.LoginDTO;
import ru.project.buySellStore.dto.RegisterDTO;
import ru.project.buySellStore.exception.userEx.UserAlreadyExistsException;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.AuthService;
import ru.project.buySellStore.service.UserService;
import ru.project.buySellStore.service.impl.AuthServiceImpl;
import ru.project.buySellStore.service.impl.UserServiceImpl;

/**
 * Контроллер с эндпоинтами для регистрации, входа и выхода пользователя
 * @author SapeginMihail
 */
@Transactional
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор контроллера для внедрения нужных зависимостей и создания экземпляра класса {@link AuthController}
     */
    @Autowired
    public AuthController(AuthServiceImpl authService, UserServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Эндпоинт для регистрации нового пользователя
     * <p>Возвращает статус 201 CREATED в случае успешной регистрации и создания аккаунта пользователя</p>
     * @param registerDTO DTO с полями, необходимыми для регистрации пользователя
     * @return строка с сообщением об успешной регистрации с упоминанием логина пользователя
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Validated @RequestBody RegisterDTO registerDTO)
            throws UserAlreadyExistsException {
        String login = registerDTO.getLogin();
        if(userService.isExistsByLogin(login)) {
            throw new UserAlreadyExistsException(login);
        }
        User user = new User();
        user.setLogin(registerDTO.getLogin());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(registerDTO.getRole());
        userService.save(user);
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
        UsernamePasswordAuthenticationToken authenticationInputToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword()
        );
        authService.login(authenticationInputToken, session);
        User user = authService.getAuthenticatedUser();
        return String.format("С возвращением, %s!", user.getLogin());
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
