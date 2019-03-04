package com.example.inventorymanagement;

public class Product {
    private String product_name;
    private String card_no;
    private String model_no;
    private double price_buy;
    private double price_sell;
    private String place;
    private String description;
    private int quantitiy;

    public Product(){
        product_name = "product_name";
        card_no = "card_no";
        model_no = "model__no";
        price_buy = 1;
        price_sell = 1;
        place = "place";
        description = "description";
        quantitiy = 0;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModel_no(String model_no) {
        this.model_no = model_no;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setPrice_buy(double price_buy) {
        this.price_buy = price_buy;
    }

    public void setPrice_sell(double price_sell) {
        this.price_sell = price_sell;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setQuantitiy(int quantitiy) {
        this.quantitiy = quantitiy;
    }

    public double getPrice_sell() {
        return price_sell;
    }

    public double getPrice_buy() {
        return price_buy;
    }

    public int getQuantitiy() {
        return quantitiy;
    }

    public String getCard_no() {
        return card_no;
    }

    public String getDescription() {
        return description;
    }

    public String getModel_no() {
        return model_no;
    }

    public String getPlace() {
        return place;
    }

    public String getProduct_name() {
        return product_name;
    }
}
