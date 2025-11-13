package ru.project.buySellStore.mapper;

import org.springframework.stereotype.Component;
import ru.project.buySellStore.dto.UserDTO;
import ru.project.buySellStore.model.User;

/**
 * Класс для маппинга сущности {@link User}
 * @author SapeginMihail
 */
@Component
public class UserMapper {

    /**
     * Маппинг сущности {@link User} в {@link UserDTO} для передачи клиенту
     * @param user сущность для маппинга
     * @return DTO для передачи клиенту
     */
    public UserDTO mapToUserDTO(User user) {
        return new UserDTO(
                user.getLogin(),
                user.getEmail(),
                user.getBirthDate(),
                user.getCity(),
                user.getDescription()
        );
    }
}
