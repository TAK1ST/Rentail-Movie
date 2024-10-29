/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import base.Model;

/**
 *
 * @author trann
 */
public class Product extends Model {

    private String name;
    private String type;
    private String brand;
    private int quantity;
    private double price;
    private String description;
    private double rating;
    private int numberOfRating;
    private String sellerID;
    private String sellerName;
    private int selledAmount;

    public Product(
            String id,
            String name,
            String type,
            String brand,
            int quantity,
            double price,
            String description,
            int rating,
            int numberOfRating,
            String sellerID,
            String sellerName,
            int selledAmount) {
        super(id);
        this.name = name;
        this.type = type;
        this.brand = brand;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.numberOfRating = numberOfRating;
        this.sellerID = sellerID;
        this.sellerName = sellerName;
        this.selledAmount = selledAmount;
    }

    public Product(Product other) {
        super(other.getId());
        this.name = other.name;
        this.type = other.type;
        this.brand = other.brand;
        this.quantity = other.quantity;
        this.price = other.price;
        this.description = other.description;
        this.rating = other.rating;
        this.numberOfRating = other.numberOfRating;
        this.sellerID = other.sellerID;
        this.sellerName = other.sellerName;
        this.selledAmount = other.selledAmount;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %d, %.3f, %s, %.1f, %s, %s, %d",
                super.getId(),
                name,
                type,
                brand,
                quantity,
                price,
                description,
                rating,
                sellerID,
                sellerName,
                selledAmount);
    }

    public static String className() {
        return "Product";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getSelledAmount() {
        return selledAmount;
    }

    public void setSelledAmount(int selledAmount) {
        this.selledAmount = selledAmount;
    }

}
