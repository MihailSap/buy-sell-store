package ru.project.buy_sell_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.project.buy_sell_store.dto.UserDTO;
import ru.project.buy_sell_store.mapper.UserMapper;
import ru.project.buy_sell_store.model.User;
import ru.project.buy_sell_store.service.UserService;
import ru.project.buy_sell_store.service.impl.UserServiceImpl;

/**
 * Контроллер для работы с профилем пользователя {@link User}
 * Методы контроллера возвращают строку или {@link UserDTO}
 * @author SapeginMihail
 */
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
     * Конструктор контроллера для внедрения нужных зависимостей и создания экземпляра класса {@link UserController}
     * @param userService реализация интерфейса для работы с {@link User}
     * @param userMapper
     */
    @Autowired
    public UserController(UserServiceImpl userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
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
    public String update(@PathVariable("userId") Long userId, @RequestBody UserDTO userDTO){
        userService.update(userId, userDTO);
        return "Ваш профиль изменен!";
    }

    /**
     * Эндпоинт для удаления аккаунта пользователя по id.
     * @param userId id пользователя, аккаунт которого нужно удалить
     * @return строка, сообщающая об успешном удалении пользователя
     */
    @DeleteMapping("/{userId}")
    public String delete(@PathVariable("userId") Long userId){
        userService.delete(userId);
        return "Профиль пользователя удален!";
    }
}