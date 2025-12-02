package ru.project.buySellStore.service;

import ru.project.buySellStore.exception.userEx.UserNotFoundException;
import ru.project.buySellStore.model.User;

/**
 * Интерфейс для сервиса, предоставляющего CRUD методы для {@link User}
 * @author SapeginMihail
 */
public interface UserService {

    /**
     * Сохранение {@link User}
     */
    User save(User user);

    /**
     * Удаление {@link User} по id
     * @throws UserNotFoundException если пользователь с указанным id не существует
     */
    void delete(Long userId) throws UserNotFoundException;

    /**
     * Получение {@link User} по id
     * @throws UserNotFoundException если пользователь с указанным id не существует
     */
    User getUserById(Long userId) throws UserNotFoundException;

    /**
     * Проверка на существование {@link User} с таким login
     */
    boolean isExistsByLogin(String login);
}
