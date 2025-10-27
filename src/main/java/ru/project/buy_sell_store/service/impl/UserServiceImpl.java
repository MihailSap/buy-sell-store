package ru.project.buy_sell_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.buy_sell_store.dto.UserDTO;
import ru.project.buy_sell_store.model.User;
import ru.project.buy_sell_store.repository.UserRepository;
import ru.project.buy_sell_store.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.login());
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());
        user.setBirthday(userDTO.birthday());
        System.out.println(user);
        userRepository.save(user);
    }
}
