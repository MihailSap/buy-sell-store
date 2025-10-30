package ru.project.buy_sell_store.service;

import ru.project.buy_sell_store.dto.RegisterDTO;
import ru.project.buy_sell_store.dto.UserDTO;
import ru.project.buy_sell_store.model.User;

/**
 * Интерфейс для сервиса, предоставляющего CRUD методы для {@link User}
 * @author SapeginMihail
 */
public interface UserService {

    /**
     * Создание {@link User}
     */
    User create(RegisterDTO registerDTO);

    /**
     * Обновление профиля пользователя
     */
    User update(Long userId, UserDTO userDTO);

    /**
     * Удаление {@link User} по id
     */
    void delete(Long userId);

    /**
     * Получение {@link User} по id
     */
    User getUserById(Long userId);
}
