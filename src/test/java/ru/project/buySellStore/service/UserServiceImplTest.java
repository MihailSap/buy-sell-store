package ru.project.buySellStore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buySellStore.dto.RegisterDTO;
import ru.project.buySellStore.dto.UserDTO;
import ru.project.buySellStore.model.Role;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.repository.UserRepository;
import ru.project.buySellStore.service.impl.UserServiceImpl;

import java.util.Calendar;
import java.util.Date;

/**
 * Класс с тестами для методов класса {@link UserServiceImpl}
 * @author SapeginMihail
 */
@SpringBootTest
@Transactional
class UserServiceImplTest {

    /**
     * Интерфейс для сервиса, предоставляющего CRUD методы для {@link User}
     */
    @Autowired
    private UserService userService;

    /**
     * Репозиторий для работы с сущностью {@link User}
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Объект для шифрования и проверки паролей
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * DTO, отправляемое пользователем при регистрации
     */
    private RegisterDTO registerDTO;

    /**
     * Подготовка данных для тестов
     */
    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO(
                "user",
                "user@yandex.ru",
                "12345678",
                Role.SELLER
        );
    }

    /**
     * Тест на создание {@link User}
     * Проверяется создание пользователя с созданным id и зашифрованным паролем
     */
    @Test
    void createUserTest() {
        User user = userService.create(registerDTO);
        Assertions.assertNotEquals(0L, user.getId());
        Assertions.assertEquals(registerDTO.getLogin(), user.getLogin());
        Assertions.assertEquals(registerDTO.getEmail(), user.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(registerDTO.getPassword(), user.getPassword()));
    }

    /**
     * Тест на получение {@link User} по id.
     * Проверяется получение существующего пользователя по id
     */
    @Test
    void getUserByExistingIdTest() {
        User savedUser = userService.create(registerDTO);
        User foundUser = userService.getUserById(savedUser.getId());
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(savedUser, foundUser);
    }

    /**
     * Тест на получение {@link User} по id.
     * Проверяет выбрасывание исключения при попытке получить несуществующего {@link User}
     */
    @Test
    void getUserByNotExistingIdTest() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () ->
                userService.getUserById(9999L)
        );

        Assertions.assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    /**
     * Тест на удаление {@link User}.
     * Проверяется корректность удаления существующего пользователя
     */
    @Test
    void deleteUserTest() {
        User savedUser = userService.create(registerDTO);
        userService.delete(savedUser.getId());
        Assertions.assertTrue(userRepository.findById(savedUser.getId()).isEmpty());
    }

    /**
     * Тест на обновление {@link User}.
     * Проверяется корректность обновления профиля пользователя
     */
    @Test
    void updateUserTest() {
        User savedUser = userService.create(registerDTO);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -15);
        Date pastDate = calendar.getTime();

        UserDTO updateDTO = new UserDTO(
                "updated",
                "updated@yandex.com",
                pastDate,
                "Екатеринбург",
                "Больше ничего не продаю"
        );

        User updatedUser = userService.update(savedUser.getId(), updateDTO);
        Assertions.assertEquals(savedUser.getId(), updatedUser.getId());
        Assertions.assertEquals("updated", updatedUser.getLogin());
        Assertions.assertEquals("updated@yandex.com", updatedUser.getEmail());
        Assertions.assertEquals(pastDate, updatedUser.getBirthDate());
        Assertions.assertEquals("Екатеринбург", updatedUser.getCity());
        Assertions.assertEquals("Больше ничего не продаю", updatedUser.getDescription());
    }
}