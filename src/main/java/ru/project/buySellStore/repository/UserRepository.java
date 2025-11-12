package ru.project.buySellStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.buySellStore.model.User;

import java.util.Optional;

/**
 * <b>JPA-репозиторий для работы с сущностью {@link User}.</b>
 * <p>Позволяет эффективно выполнять операции в БД над сущностью User</p>
 * @author SapeginMihail
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Проверка, существует ли пользователь с таким логином
     * @param login логин, по которому выполняется поиск пользователя
     * @return {@code true}, если пользователь с таким логином существует
     */
    boolean existsByLogin(String login);

    /**
     * Получение {@link User} из БД по его email (если пользователь существует)
     */
    Optional<User> findByEmail(String email);
}
