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
import ru.project.buySellStore.dto.UserDTO;
import ru.project.buySellStore.exception.userEx.UserNotFoundException;
import ru.project.buySellStore.mapper.UserMapper;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.impl.AuthServiceImpl;
import ru.project.buySellStore.service.impl.UserServiceImpl;

import java.util.Date;

/**
 * Тест для методов класса {@link UserController}
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private AuthServiceImpl authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * <b>Проверяет получение пользователя по существующему id.</b>
     * <p>Ожидается - корректное отображение пользователя</p>
     */
    @Test
    void testGetUserByExistingId() throws Exception {
        User user = new User();
        Date birthDate = new Date();
        user.setLogin("user");
        user.setEmail("user@email.com");
        user.setCity("Ekaterinburg");
        user.setDescription("Небольшое описание");
        UserDTO userDTO = new UserDTO(
                "user",
                "user@email.com",
                birthDate,
                "Ekaterinburg",
                "Небольшое описание");

        Mockito.when(userService.getUserById(1L))
                .thenReturn(user);
        Mockito.when(userMapper.mapToUserDTO(user))
                .thenReturn(userDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(userDTO.getLogin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(userDTO.getCity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(userDTO.getDescription()));

        Mockito.verify(userService)
                .getUserById(1L);
    }

    /**
     * <b>Проверяет получение пользователя по несуществующему id.</b>
     * <p>Ожидается - возвращение соответствующей ошибки</p>
     */
    @Test
    void testGetUserByNonExistingId() throws Exception {
        Long nonExistingId = 1L;
        Mockito.when(userService.getUserById(nonExistingId))
                .thenThrow(new UserNotFoundException(nonExistingId));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Пользователь с id = 1 не найден"));

        Mockito.verify(userService)
                .getUserById(nonExistingId);
    }

    /**
     * <b>Проверяет корректность ответа при обновлении существующего пользователя</b>
     * <p>Ожидается, что тело ответа содержит 'Ваш профиль изменен!'</p>
     */
    @Test
    void testUpdate() throws Exception {
        UserDTO userDTO = new UserDTO(
                "user",
                "user@mail.com",
                new Date(),
                "Ekaterinburg",
                "Небольшое описание");

        Mockito.when(userService.getUserById(1L))
                        .thenReturn(new User());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Ваш профиль изменен!"));

        Mockito.verify(userService, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    /**
     * <b>Проверяет корректность ответа при удалении существующего пользователя</b>
     * <p>Ожидается, что тело ответа содержит 'Профиль пользователя удален!'
     * Также, должны быть вызваны нужные методы</p>
     */
    @Test
    void deleteTest() throws Exception {
        Long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Профиль пользователя удален!"));

        Mockito.verify(authService)
                .logout(Mockito.any(HttpSession.class));
        Mockito.verify(userService)
                .delete(userId);
    }
}