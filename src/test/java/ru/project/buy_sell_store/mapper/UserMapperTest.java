package ru.project.buy_sell_store.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.buy_sell_store.dto.UserDTO;
import ru.project.buy_sell_store.enums.RoleEnum;
import ru.project.buy_sell_store.model.User;

import java.util.Calendar;
import java.util.Date;

/**
 * Класс с тестом для метода класса {@link UserMapper}
 * @author SapeginMihail
 */
public class UserMapperTest {

    /**
     * Класс для маппинга {@link User}
     */
    private UserMapper userMapper;

    /**
     * Подготовка данных для тестов
     */
    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    /**
     * Тест для метода {@link UserMapper#mapToUserDTO(User)}.
     * Проверяет корректность маппинга - все ли поля правильно заполняются
     */
    @Test
    void mapToUserDTOTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);
        Date birthDate = calendar.getTime();

        User user = new User(
                1L,
                "user",
                "user@yandex.ru",
                "12345678",
                birthDate,
                "Екатеринбург",
                "Самый честный продавец",
                RoleEnum.SELLER
        );

        UserDTO dto = userMapper.mapToUserDTO(user);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(user.getLogin(), dto.getLogin());
        Assertions.assertEquals(user.getEmail(), dto.getEmail());
        Assertions.assertEquals(user.getBirthDate(), dto.getBirthDate());
        Assertions.assertEquals(user.getCity(), dto.getCity());
        Assertions.assertEquals(user.getDescription(), dto.getDescription());
    }
}
