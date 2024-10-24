package com.techelevator.model;

import java.math.BigDecimal;

public class Cart {

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    private int productId;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    private String productName;

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    private String productSku;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private BigDecimal price;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int quantity;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    private String imageName;

    public Cart() {}

    public Cart(int userId, int productId, String productName,
                String productSku, BigDecimal price, int quantity, String username) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productSku = productSku;
        this.price = price;
        this.quantity = quantity;
        this.username = username;
    }
}
