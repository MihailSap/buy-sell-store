package ru.project.buy_sell_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buy_sell_store.dto.UserDTO;
import ru.project.buy_sell_store.dto.RegisterDTO;
import ru.project.buy_sell_store.model.User;
import ru.project.buy_sell_store.repository.UserRepository;
import ru.project.buy_sell_store.service.UserService;

/**
 * Реализация интерфейса {@link UserService}
 * @author SapeginMihail
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Репозиторий для работы с сущностью {@link User}
     */
    private final UserRepository userRepository;

    /**
     * Экземпляр {@link PasswordEncoder}, используемый для шифрования паролей.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для внедрения нужных зависимостей и создания экземпляра класса {@link UserServiceImpl}.
     * @param userRepository репозиторий для работы с сущностью {@link User}
     * @param passwordEncoder экземпляр для шифрования паролей
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User create(RegisterDTO registerDTO) {
        User user = new User();
        user.setLogin(registerDTO.login());
        user.setEmail(registerDTO.email());
        user.setPassword(passwordEncoder.encode(registerDTO.password()));
        user.setRole(registerDTO.role());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(Long userId, UserDTO userDTO) {
        User user = getUserById(userId);
        user.setEmail(userDTO.email());
        user.setLogin(userDTO.login());
        user.setBirthDate(userDTO.birthDate());
        user.setCity(userDTO.city());
        user.setDescription(userDTO.description());
        return userRepository.save(user);
    }

    @Override
    public void delete(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь с таким id не найден"));
    }
}
