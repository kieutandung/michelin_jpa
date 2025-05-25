package com.restaurants.michelin.model;

import javax.persistence.*;

@Entity @Table(name = "food")
public class Food {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Món_chính', 'Đồ_tráng_miệng', 'món_phụ', 'Đồ_uống')")
    private FoodType type;
    private String nameFood, image;
    private int price , quantity;

    public Food(Integer id, FoodType type, String nameFood, String image, int price, int quantity) {
        this.id = id;
        this.type = type;
        this.nameFood = nameFood;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public Food() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
