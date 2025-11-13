package ru.project.buySellStore.service;

import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.project.buySellStore.dto.LoginDTO;
import ru.project.buySellStore.dto.RegisterDTO;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.security.UserDetailsImpl;
import ru.project.buySellStore.service.impl.AuthServiceImpl;
import ru.project.buySellStore.service.impl.UserServiceImpl;

/**
 * Тесты для методов класса {@link AuthServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private HttpSession session;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    /**
     * Очистка контекста после каждого теста
     */
    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Проверка корректности регистрации.
     * Ожидается, что будет вызван метод {@code create()} из класса {@link UserServiceImpl}
     */
    @Test
    void registerTest() {
        RegisterDTO registerDTO = new RegisterDTO(
                "user", "user@mail.com", "password", Role.SELLER);
        authService.register(registerDTO);
        Mockito.verify(userService, Mockito.times(1)).create(registerDTO);
    }

    /**
     * Проверка корректности входа в аккаунт
     */
    @Test
    void login(){
        LoginDTO loginDTO = new LoginDTO();
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        authService.login(loginDTO, session);
        Mockito.verify(authenticationManager, Mockito.times(1))
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

        SecurityContext context = SecurityContextHolder.getContext();
        Assertions.assertThat(context.getAuthentication()).isEqualTo(authentication);
        Mockito.verify(session, Mockito.times(1))
                .setAttribute(Mockito.eq("SPRING_SECURITY_CONTEXT"), Mockito.eq(context));
    }

    /**
     * Проверка корректности выхода из аккаунта
     * Ожидается, что при выходе будет вызван метод {@code invalidate()}
     */
    @Test
    void logoutTest(){
        authService.logout(session);
        Mockito.verify(session, Mockito.times(1)).invalidate();
    }

    /**
     * Проверка корректности получения текущего авторизованного пользователя
     */
    @Test
    void getAuthenticatedUserTest() {
        User user = new User();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User authenticatedUser = authService.getAuthenticatedUser();
        Assertions.assertThat(authenticatedUser).isEqualTo(user);
    }
}