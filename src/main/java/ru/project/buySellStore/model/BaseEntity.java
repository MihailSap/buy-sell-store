package ru.project.buySellStore.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

/**
 * Базовый класс для сущностей
 */
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * <b>Идентификатор сущности</b>
     * <p>Определяется при создании</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Метод получения id пользователя
     */
    public long getId() {
        return id;
    }

    /**
     * Метод определения id пользователя
     */
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
