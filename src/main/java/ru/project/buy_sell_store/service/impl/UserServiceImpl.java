package ru.project.buy_sell_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buy_sell_store.dto.UserDTO;
import ru.project.buy_sell_store.dto.RegisterUserDTO;
import ru.project.buy_sell_store.model.User;
import ru.project.buy_sell_store.repository.UserRepository;
import ru.project.buy_sell_store.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void create(RegisterUserDTO registerUserDTO) {
        User user = new User();
        user.setLogin(registerUserDTO.login());
        user.setEmail(registerUserDTO.email());
        user.setPassword(passwordEncoder.encode(registerUserDTO.password()));
        user.setRole(registerUserDTO.role());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void update(Long userId, UserDTO userDTO) {
        User user = getUserById(userId);
        user.setEmail(userDTO.email());
        user.setLogin(userDTO.login());
        user.setBirthday(userDTO.birthDate());
        user.setCity(userDTO.city());
        user.setDescription(userDTO.description());
        userRepository.save(user);
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

    @Override
    public UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getCity(),
                user.getDescription()
        );
    }
}
