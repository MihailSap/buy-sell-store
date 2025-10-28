package ru.project.buy_sell_store.dto;

import ru.project.buy_sell_store.enums.Role;

public record RegisterUserDTO(String login, String email, String password, Role role) {
}
