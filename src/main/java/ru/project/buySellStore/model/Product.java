package ru.project.buySellStore.model;

import jakarta.persistence.*;

/**
 *  Cущность товара
 */
@Entity
@Table(name = "product")
public class Product extends BaseEntity{

    /**
     * Название
     */
    private String name;

    /**
     * Описание
     */
    private String description;

    /**
     * Категория
     */
    private String category;

    /**
     * Цена поставщика
     */
    private Integer supplierCost;

    /**
     * Цена продавца
     */
    private Integer sellerCost;

    /**
     * Продавец
     */
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    /**
     * Поставщик
     */
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private User supplier;

    /**
     * Покупатель
     */
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    /**
     * Флаг, указывающий, что товар находится в архиве
     * Если true — товар считается архивным и не должен отображаться в списках
     * Если false — товар активен и доступен для операций
     *
     * По умолчанию false
     */
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
     * Получить стоимость поставщика
     */
    public Integer getSupplierCost() {
        return supplierCost;
    }

    /**
     * Установить стоимость поставщика
     */
    public void setSupplierCost(Integer cost) {
        this.supplierCost = cost;
    }

    /**
     * Получить продавца данного товара
     */
    public User getSeller() {
        return seller;
    }

    /**
     * Установить продавца данного товара
     */
    public void setSeller(User seller) {
        this.seller = seller;
    }

    /**
     * Получить поставщика
     */
    public User getSupplier() {
        return supplier;
    }

    /**
     * Установить поставщика
     */
    public void setSupplier(User supplier) {
        this.supplier = supplier;
    }

    /**
     * Получить покупателя
     */
    public User getBuyer() {
        return buyer;
    }

    /**
     * Установить покупателя
     * @param buyer
     */
    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    /**
     * Получить стоимость продавца
     */
    public Integer getSellerCost() {
        return sellerCost;
    }

    /**
     * Установить стоимость продавца
     */
    public void setSellerCost(Integer sellerCost) {
        this.sellerCost = sellerCost;
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
