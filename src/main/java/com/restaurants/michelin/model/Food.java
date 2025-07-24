package com.restaurants.michelin.model;

import javax.persistence.*;

@Entity
@Table(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFood;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "ENUM('Món_chính', 'Đồ_tráng_miệng', 'Món_phụ', 'Đồ_uống')")
    private FoodType type;
    @Column(name = "nameFood", nullable = false)
    private String nameFood;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "image")
    private String image;

    public Food(Integer idFood, FoodType type, String nameFood, int price, int quantity) {
        this.idFood = idFood;
        this.type = type;
        this.nameFood = nameFood;
        this.price = price;
        this.quantity = quantity;
    }
    public Food(FoodType type, String nameFood, int price, int quantity) {
        this.type = type;
        this.nameFood = nameFood;
        this.price = price;
        this.quantity = quantity;
    }

    public Food() {

    }

    public Integer getIdFood() {
        return idFood;
    }

    public void setIdFood(Integer idFood) {
        this.idFood = idFood;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
