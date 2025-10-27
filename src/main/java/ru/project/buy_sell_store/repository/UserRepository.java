package ru.project.buy_sell_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.buy_sell_store.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
