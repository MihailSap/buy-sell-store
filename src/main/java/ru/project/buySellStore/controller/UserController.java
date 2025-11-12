package ru.project.buySellStore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.project.buySellStore.dto.UserDTO;
import ru.project.buySellStore.mapper.UserMapper;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.service.AuthService;
import ru.project.buySellStore.service.UserService;
import ru.project.buySellStore.service.impl.AuthServiceImpl;
import ru.project.buySellStore.service.impl.UserServiceImpl;

/**
 * Контроллер для работы с профилем пользователя {@link User}
 * Методы контроллера возвращают строку или {@link UserDTO}
 * @author SapeginMihail
 */
@Transactional
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Интерфейс для работы с {@link User}.
     * Позволяет получать, редактировать и удалять профиль пользователя
     */
    private final UserService userService;

    /**
     * Класс, содержащий методы для маппинга {@link User}
     * Используется для маппинга {@link User} в {@link UserDTO}
     */
    private final UserMapper userMapper;

    /**
     * Интерфейс, предоставляющий метод для выхода из аккаунта
     */
    private final AuthService authService;

    /**
     * Конструктор контроллера для внедрения нужных зависимостей и создания экземпляра класса {@link UserController}
     * @param userService реализация интерфейса для работы с {@link User}
     * @param userMapper
     */
    @Autowired
    public UserController(UserServiceImpl userService, UserMapper userMapper, AuthServiceImpl authService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authService = authService;
    }

    /**
     * Эндпоинт для получения DTO профиля по его id
     * @param userId id пользователя, информацию о котором нужно получить
     * @return DTO с информацией о пользователе
     */
    @GetMapping("/{userId}")
    public UserDTO get(@PathVariable("userId") Long userId){
        User user = userService.getUserById(userId);
        return userMapper.mapToUserDTO(user);
    }

    /**
     * Эндпоинт для изменения профиля пользователя по его id
     * @param userId id пользователя, профиль которого нужно изменить
     * @param userDTO DTO с информацией, которую нужно отразить в профиле пользователя
     * @return строка, сообщающая об успешном изменении профиля
     */
    @PatchMapping("/{userId}")
    public String update(@PathVariable("userId") Long userId, @Validated @RequestBody UserDTO userDTO){
        userService.update(userId, userDTO);
        return "Ваш профиль изменен!";
    }

    /**
     * Эндпоинт для удаления аккаунта пользователя по id.
     * @param userId id пользователя, аккаунт которого нужно удалить
     * @return строка, сообщающая об успешном удалении пользователя
     */
    @DeleteMapping("/{userId}")
    public String delete(@PathVariable("userId") Long userId, HttpSession session){
        authService.logout(session);
        userService.delete(userId);
        return "Профиль пользователя удален!";
    }
}