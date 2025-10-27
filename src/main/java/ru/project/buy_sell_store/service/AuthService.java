package ru.project.buy_sell_store.service;

import jakarta.servlet.http.HttpSession;
import ru.project.buy_sell_store.dto.LoginDTO;
import ru.project.buy_sell_store.dto.UserDTO;

public interface AuthService {

    void register(UserDTO userDTO);

    void login(LoginDTO loginDTO, HttpSession session);

    void logout(HttpSession session);
}
