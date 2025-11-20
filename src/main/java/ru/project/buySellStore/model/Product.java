package ru.project.buySellStore.model;

import jakarta.persistence.*;

import java.util.Objects;

/**
 *  Cущность товара
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String category;

    private Integer supplierCost;

    private Integer sellerCost;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private User supplier;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    private boolean archived = false;

    /**
     * Установить id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить id
     */
    public Long getId() {
        return id;
    }

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
     * Переопределение equals со всеми полями
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return archived == product.archived && Objects.equals(id, product.id)
                && Objects.equals(name, product.name) && Objects.equals(description, product.description)
                && Objects.equals(category, product.category) && Objects.equals(supplierCost, product.supplierCost)
                && Objects.equals(seller, product.seller) && Objects.equals(sellerCost, product.sellerCost);
    }

    /**
     * Переопределение hashCode со всеми полями
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, category, supplierCost, archived,
                seller, sellerCost);
    }
}
