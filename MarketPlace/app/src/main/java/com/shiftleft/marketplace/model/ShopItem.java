package com.shiftleft.marketplace.model;

import java.io.Serializable;

/**
 * Created by rsousa on 05/11/2016.
 */

public class ShopItem implements Serializable {
    private String productId;
    private String category;
    private String subCategory;
    private String section;
    private String description;
    private double price;
    private double quantity;
    private String brand;
    private short status;
    private ShopItem alternative;

    public ShopItem clone() {
        ShopItem item = new ShopItem();
        item.setProductId(productId);
        item.setCategory(category);
        item.setSubCategory(subCategory);
        item.setSection(section);
        item.setDescription(description);
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setBrand(brand);
        item.setStatus(status);
        item.setAlternative(alternative);
        return item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public ShopItem getAlternative() {
        return alternative;
    }

    public void setAlternative(ShopItem alternative) {
        this.alternative = alternative;
    }
}
