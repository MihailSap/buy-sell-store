package ru.project.buy_sell_store.service;

import ru.project.buy_sell_store.dto.UserDTO;
import ru.project.buy_sell_store.dto.RegisterUserDTO;
import ru.project.buy_sell_store.model.User;

public interface UserService {

    void create(RegisterUserDTO registerUserDTO);

    void update(Long userId, UserDTO userDTO);

    void delete(Long userId);

    User getUserById(Long userId);

    UserDTO mapToDTO(User user);
}
