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
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.security.UserDetailsImpl;
import ru.project.buySellStore.service.impl.AuthServiceImpl;

/**
 * Тесты для методов класса {@link AuthServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

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
     * <b>Проверка корректности входа в аккаунт</b>
     */
    @Test
    void testLogin(){
        LoginDTO loginDTO = new LoginDTO();
        UsernamePasswordAuthenticationToken authenticationInputToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword()
        );

        Mockito.when(authenticationManager.authenticate(authenticationInputToken))
                .thenReturn(authentication);

        authService.login(authenticationInputToken, session);
        SecurityContext context = SecurityContextHolder.getContext();
        Assertions.assertThat(context.getAuthentication()).isEqualTo(authentication);

        Mockito.verify(session)
                .setAttribute(Mockito.eq("SPRING_SECURITY_CONTEXT"), Mockito.eq(context));
        Mockito.verify(authenticationManager)
                .authenticate(authenticationInputToken);
    }

    /**
     * <b>Проверка корректности выхода из аккаунта</b>
     * <p>Ожидается, что при выходе будет вызван метод {@code invalidate()}</p>
     */
    @Test
    void testLogout(){
        authService.logout(session);
        Mockito.verify(session)
                .invalidate();
    }

    /**
     * <b>Проверка корректности получения текущего авторизованного пользователя</b>
     */
    @Test
    void testGetAuthenticatedUser() {
        User user = new User();
        Mockito.when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(user));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User authenticatedUser = authService.getAuthenticatedUser();
        Assertions.assertThat(authenticatedUser)
                .isEqualTo(user);
    }
}