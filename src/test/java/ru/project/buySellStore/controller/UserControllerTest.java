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

    /**
     * Проверяет получение пользователя по id
     * Ожидается - корректное отображение пользователя
     */
    @Test
    void getUserByIdTest() throws Exception {
        User user = new User();
        Date birthDate = new Date();
        user.setId(1L);
        user.setLogin("user");
        user.setEmail("user@email.com");
        user.setBirthDate(birthDate);
        user.setCity("Ekaterinburg");
        user.setDescription("Небольшое описание");
        UserDTO userDTO = new UserDTO(
                "user",
                "user@email.com",
                birthDate,
                "Ekaterinburg",
                "Небольшое описание");

        Mockito.when(userService.getUserById(1L)).thenReturn(user);
        Mockito.when(userMapper.mapToUserDTO(user)).thenReturn(userDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(userDTO.getLogin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(userDTO.getCity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(userDTO.getDescription()));

        Mockito.verify(userService, Mockito.times(1)).getUserById(1L);
    }

    /**
     * Проверяет корректность ответа при обновлении существующего пользователя
     * Ожидается, что тело ответа содержит 'Ваш профиль изменен!'
     */
    @Test
    void updateTest() throws Exception {
        UserDTO userDTO = new UserDTO(
                "user",
                "user@mail.com",
                new Date(),
                "Ekaterinburg",
                "Небольшое описание");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Ваш профиль изменен!"));

        Mockito.verify(userService, Mockito.times(1))
                .update(Mockito.eq(1L), Mockito.any(UserDTO.class));
    }

    /**
     * Проверяет корректность ответа при удалении существующего пользователя
     * Ожидается, что тело ответа содержит 'Профиль пользователя удален!'
     * Также, должны быть вызваны нужные методы
     */
    @Test
    void deleteTest() throws Exception {
        Long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Профиль пользователя удален!"));

        Mockito.verify(authService, Mockito.times(1)).logout(Mockito.any(HttpSession.class));
        Mockito.verify(userService, Mockito.times(1)).delete(userId);
    }
}