package ru.project.buySellStore.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.project.buySellStore.dto.RegisterDTO;
import ru.project.buySellStore.dto.UserDTO;
import ru.project.buySellStore.exception.userEx.UserAlreadyExistsException;
import ru.project.buySellStore.exception.userEx.UserNotFoundException;
import ru.project.buySellStore.model.Role;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Тест на получение пользователя с несуществующим id
     */
    @Test
    void testGetUserById(){
        Mockito.when(userRepository.findById(1000L)).thenReturn(Optional.empty());
        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(1000L));

        Assertions.assertEquals("Пользователь с id = 1000 не найден", ex.getMessage());
    }

    /**
     * Тест на обновление пользователя с несуществующим id
     */
    @Test
    void testUpdateUser(){
        UserDTO userDTO = new UserDTO();
        Mockito.when(userRepository.findById(1000L))
                .thenReturn(Optional.empty());
        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.update(1000L, userDTO));
        Assertions.assertEquals("Пользователь с id = 1000 не найден",
                ex.getMessage());
        Mockito.verify(userRepository, Mockito.never()).delete(Mockito.any());
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
     * Тест на создание двух пользователей с одинаковыми логинами
     */
    @Test
    void createUsersWithSameLoginsTest(){
        RegisterDTO registerDTO = new RegisterDTO(
                "user",
                "user@yandex.ru",
                "12345678",
                Role.SELLER
        );

        Mockito.when(userRepository.existsByLogin(registerDTO.getLogin()))
                .thenReturn(false)
                .thenReturn(true);
        Mockito.when(passwordEncoder.encode(registerDTO.getPassword()))
                .thenReturn("encodedPassword");
        userService.create(registerDTO);
        UserAlreadyExistsException exception = Assertions.assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.create(registerDTO));
        Assertions.assertEquals("Пользователь с логином 'user' уже существует",
                exception.getMessage());
        Mockito.verify(userRepository,
                Mockito.times(1)).save(Mockito.any(User.class));
    }
}