package ru.project.buySellStore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.project.buySellStore.exception.userEx.UserNotFoundException;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.repository.UserRepository;
import ru.project.buySellStore.service.impl.UserServiceImpl;

import java.util.Optional;

/**
 * Класс с тестами для методов класса {@link UserServiceImpl}
 * @author SapeginMihail
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * <b>Тест на сохранение пользователя</b>
     */
    @Test
    void testSave(){
        User user = new User();
        Mockito.when(userRepository.save(user))
                .thenReturn(user);
        Assertions.assertEquals(user, userService.save(user));
    }

    /**
     * Тест на удаление пользователя с несуществующим id
     */
    @Test
    void testDeleteUser(){
        Mockito.when(userRepository.findById(1000L))
                .thenReturn(Optional.empty());
        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.delete(1000L));
        Assertions.assertEquals("Пользователь с id = 1000 не найден",
                ex.getMessage());
        Mockito.verify(userRepository, Mockito.never()).delete(Mockito.any());
    }

    /**
     * <b>Тест на получение пользователя по несуществующему id</b>
     */
    @Test
    void testGetUserByNonExistingId(){
        Mockito.when(userRepository.findById(1000L))
                .thenReturn(Optional.empty());
        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(1000L));

        Assertions.assertEquals("Пользователь с id = 1000 не найден", ex.getMessage());
    }

    /**
     * <b>Тест на получение пользователя по существующему id</b>
     */
    @Test
    void testGetUserByExistingId() throws UserNotFoundException {
        Long existingId = 1L;
        User user = new User();
        user.setId(existingId);

        Mockito.when(userRepository.findById(existingId))
                .thenReturn(Optional.of(user));

        Assertions.assertEquals(user, userService.getUserById(existingId));
    }

    /**
     * <b>Тест на проверку существования пользователя с таким {@code login}</b>
     */
    @Test
    void testIsExistsByLogin(){
        String login = "user";
        Mockito.when(userRepository.existsByLogin(login))
                .thenReturn(true);
        Assertions.assertTrue(userService.isExistsByLogin(login));
    }
}