package ru.project.buySellStore.model;

import jakarta.persistence.*;

/**
 *  Cущность товара
 */
@Entity
@Table(name = "product")
public class Product  extends BaseEntity{

    private String name;

    private String description;

    private String category;

    private Integer cost;

    private boolean archived = false;

    /**
     * Получить имя
     */
    public String getName() {
        return name;
    }

    /**
     * Установить имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить описание
     */
    public String getDescription() {
        return description;
    }

    /**
     * Установить описание
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Получить категорию
     */
    public String getCategory() {
        return category;
    }

    /**
     * Установить категорию
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Получить стоимость
     */
    public Integer getCost() {
        return cost;
    }

    /**
     * Установить стоимость
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    /**
     * Узнать находится ли товар в архиве
     */
    public boolean isArchived() {
        return archived;
    }

    /**
     * Установить товар как архивный
     */
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
