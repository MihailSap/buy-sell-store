package ru.project.buy_sell_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.buy_sell_store.dto.UserDTO;
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
    public void create(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.login());
        user.setEmail(userDTO.email());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setBirthday(userDTO.birthday());
        userRepository.save(user);
    }
}
