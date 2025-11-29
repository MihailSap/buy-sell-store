package ru.project.buySellStore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.project.buySellStore.dto.LoginDTO;
import ru.project.buySellStore.dto.RegisterDTO;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.impl.AuthServiceImpl;
import ru.project.buySellStore.service.impl.UserServiceImpl;

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

    @MockitoBean
    private UserServiceImpl userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * <b>Регистрация пользователя с указанием {@code login},
     * который уже существует в БД</b>
     * <p>Ожидается, что будет выброшено соответствующее исключение с соответствующим кодом ошибки</p>
     */
    @Test
    void testRegisterWithExistingLogin() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(
                "user", "user@mail.com", "12345", Role.SELLER);

        Mockito.when(userService.isExistsByLogin("user"))
                        .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Пользователь с логином 'user' уже существует"));
        Mockito.verify(userService, Mockito.times(1))
                .isExistsByLogin("user");
        Mockito.verify(userService, Mockito.never())
                .save(Mockito.any(User.class));
    }

    /**
     * <b>Регистрация пользователя</b>
     */
    @Test
    void testRegisterWithNonExistingLogin() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(
                "user", "user@mail.com", "12345", Role.SELLER);

        Mockito.when(userService.isExistsByLogin("user"))
                .thenReturn(false);
        Mockito.when(userService.save(Mockito.any(User.class)))
                .thenReturn(new User());
        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn("");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Пользователь user зарегистрирован!"));

        Mockito.verify(userService)
                .isExistsByLogin("user");
        Mockito.verify(userService)
                .save(Mockito.any(User.class));
        Mockito.verify(passwordEncoder)
                .encode(registerDTO.getPassword());
    }

    /**
     * <b>Проверяет корректность ответа при успешном входе</b>
     */
    @Test
    void testLogin() throws Exception {
        LoginDTO loginDTO = new LoginDTO("user@mail.com", "password");

        User user = new User();
        user.setLogin("user");

        Mockito.doNothing()
                .when(authService)
                .login(Mockito.any(UsernamePasswordAuthenticationToken.class), Mockito.any(HttpSession.class));
        Mockito.when(authService.getAuthenticatedUser())
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("С возвращением, user!"));

        Mockito.verify(authService, Mockito.times(1))
                .login(Mockito.any(UsernamePasswordAuthenticationToken.class), Mockito.any(HttpSession.class));
        Mockito.verify(authService)
                .getAuthenticatedUser();
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