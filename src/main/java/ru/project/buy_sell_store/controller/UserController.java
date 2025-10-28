package ru.project.buy_sell_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.project.buy_sell_store.dto.UserDTO;
import ru.project.buy_sell_store.model.User;
import ru.project.buy_sell_store.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserDTO get(@PathVariable("userId") Long userId){
        User user = userService.getUserById(userId);
        return userService.mapToDTO(user);
    }

    @PatchMapping("/{userId}")
    public String update(@PathVariable("userId") Long userId, @RequestBody UserDTO userDTO){
        userService.update(userId, userDTO);
        return "Ваш профиль изменен!";
    }

    @DeleteMapping("/{userId}")
    public String delete(@PathVariable("userId") Long userId){
        userService.delete(userId);
        return "Профиль пользователя удален!";
    }
}
