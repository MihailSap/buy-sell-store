package ru.project.buy_sell_store.service.impl;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.project.buy_sell_store.dto.LoginDTO;
import ru.project.buy_sell_store.dto.UserDTO;
import ru.project.buy_sell_store.service.AuthService;
import ru.project.buy_sell_store.service.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public void register(UserDTO userDTO){
        userService.create(userDTO);
    }

    @Override
    public void login(LoginDTO loginDTO, HttpSession session){
        UsernamePasswordAuthenticationToken authenticationInputToken = new UsernamePasswordAuthenticationToken(
                loginDTO.login(), loginDTO.password()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationInputToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    @Override
    public void logout(HttpSession session){
        session.invalidate();
        SecurityContextHolder.clearContext();
    }
}
