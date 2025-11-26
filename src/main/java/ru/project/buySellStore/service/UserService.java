package ru.project.buySellStore.service;

import ru.project.buySellStore.dto.RegisterDTO;
import ru.project.buySellStore.dto.UserDTO;
import ru.project.buySellStore.exception.userEx.UserAlreadyExistsException;
import ru.project.buySellStore.exception.userEx.UserNotFoundException;
import ru.project.buySellStore.model.User;

/**
 * Интерфейс для сервиса, предоставляющего CRUD методы для {@link User}
 * @author SapeginMihail
 */
public interface UserService {

    /**
     * Создание {@link User}
     */
    User create(RegisterDTO registerDTO) throws UserAlreadyExistsException;

    /**
     * Обновление профиля пользователя
     */
    User update(Long userId, UserDTO userDTO) throws UserNotFoundException;

    /**
     * Удаление {@link User} по id
     */
    void delete(Long userId) throws UserNotFoundException;

    /**
     * Получение {@link User} по id
     */
    User getUserById(Long userId) throws UserNotFoundException;
}
