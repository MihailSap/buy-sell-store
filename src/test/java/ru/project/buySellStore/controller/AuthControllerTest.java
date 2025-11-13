package ru.project.buySellStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.project.buySellStore.dto.LoginDTO;
import ru.project.buySellStore.dto.RegisterDTO;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.impl.AuthServiceImpl;

/**
 * Тесты для методов класса {@link AuthController}
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthServiceImpl authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Проверяет корректность ответа при успешной регистрации пользователя
     */
    @Test
    void registerTest() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(
                "user", "user@mail.com", "12345", Role.SELLER);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Пользователь user зарегистрирован!"));

        Mockito.verify(authService, Mockito.times(1))
                .register(Mockito.any(RegisterDTO.class));
    }

    /**
     * Проверяет корректность ответа при успешном входе
     */
    @Test
    void loginTest() throws Exception {
        LoginDTO loginDTO = new LoginDTO("user@mail.com", "password");

        User user = new User();
        user.setLogin("user");

        Mockito.doNothing()
                .when(authService)
                .login(Mockito.any(LoginDTO.class), Mockito.any(HttpSession.class));
        Mockito.when(authService.getAuthenticatedUser()).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("С возвращением, user!"));

        Mockito.verify(authService, Mockito.times(1))
                .login(Mockito.any(LoginDTO.class), Mockito.any(HttpSession.class));
        Mockito.verify(authService, Mockito.times(1)).getAuthenticatedUser();
    }

    /**
     * Проверяет корректность ответа при успешном выходе
     */
    @Test
    void logoutTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/logout"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(authService, Mockito.times(1))
                .logout(Mockito.any(HttpSession.class));
    }

}