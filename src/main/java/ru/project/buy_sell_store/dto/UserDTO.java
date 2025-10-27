package ru.project.buy_sell_store.dto;

import java.util.Date;

public record UserDTO(String login, String email, String password, Date birthday) {
}
