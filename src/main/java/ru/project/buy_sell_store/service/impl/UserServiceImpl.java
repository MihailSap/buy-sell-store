package ru.project.buy_sell_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buy_sell_store.dto.RegisterDTO;
import ru.project.buy_sell_store.dto.UserDTO;
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
        if(userRepository.existsByLogin(registerDTO.getLogin())){
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setLogin(registerDTO.getLogin());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(registerDTO.getRole());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(Long userId, UserDTO userDTO) {
        User user = getUserById(userId);
        user.setEmail(userDTO.getEmail());
        user.setLogin(userDTO.getLogin());
        user.setBirthDate(userDTO.getBirthDate());
        user.setCity(userDTO.getCity());
        user.setDescription(userDTO.getDescription());
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
