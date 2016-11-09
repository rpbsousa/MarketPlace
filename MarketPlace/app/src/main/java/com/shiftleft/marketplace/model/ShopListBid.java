package com.shiftleft.marketplace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsousa on 06/11/2016.
 */

public class ShopListBid implements Serializable {
    private String brand;
    private List<ShopItem> items;
    /*private List<ShopItem> available;
    private List<ShopItem> unavailable;
    private List<ShopItem> alternative;
    private List<ShopItem> promotion;*/
    private double price;
    private int status; //ok 100%; ok com alternativa; faltam items

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<ShopItem> getItems() {
        return items;
    }

    public void setItems(List<ShopItem> items) {
        this.items = items;
    }

    /*
        public List<ShopItem> getAvailable() {
            return available;
        }

        public void setAvailable(List<ShopItem> available) {
            this.available = available;
        }

        public List<ShopItem> getUnavailable() {
            return unavailable;
        }

        public void setUnavailable(List<ShopItem> unavailable) {
            this.unavailable = unavailable;
        }

        public List<ShopItem> getAlternative() {
            return alternative;
        }

        public void setAlternative(List<ShopItem> alternative) {
            this.alternative = alternative;
        }

        public List<ShopItem> getPromotion() {
            return promotion;
        }

        public void setPromotions(List<ShopItem> promotion) {
            this.promotion = promotion;
        }
    */
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
