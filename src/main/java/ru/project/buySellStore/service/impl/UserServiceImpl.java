package ru.project.buySellStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.buySellStore.exception.userEx.UserNotFoundException;
import ru.project.buySellStore.model.User;
import ru.project.buySellStore.repository.UserRepository;
import ru.project.buySellStore.service.UserService;

/**
 * Реализация интерфейса {@link UserService}
 * @author SapeginMihail
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Конструктор для внедрения нужных зависимостей и создания экземпляра класса {@link UserServiceImpl}.
     * @param userRepository репозиторий для работы с сущностью {@link User}
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long userId) throws UserNotFoundException {
        try{
            User user = getUserById(userId);
            userRepository.delete(user);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public User getUserById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public boolean isExistsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
}
