package ru.project.buy_sell_store.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.project.buy_sell_store.dto.LoginDTO;
import ru.project.buy_sell_store.dto.RegisterUserDTO;
import ru.project.buy_sell_store.service.impl.AuthServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody RegisterUserDTO registerUserDTO) {
        authService.register(registerUserDTO);
        return String.format("Пользователь %s зарегистрирован!", registerUserDTO.login());
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO, HttpSession session){
        authService.login(loginDTO, session);
        return String.format("С возвращением, %s!", loginDTO.login());
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        authService.logout(session);
    }
}
