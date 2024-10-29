/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import base.Model;

import java.time.LocalDateTime;

/**
 *
 * @author trann
 */
public class Review extends Model {

    private int rating;
    private String comment;
    private String customerID;
    private String customerUsername;
    private String productID;
    private String productName;
    private String sellerID;
    private String sellerUsername;
    private LocalDateTime createDateTime;

    public Review(String id, int rating, String comment, String customerID, String customerUsername, String productID,
            String productName, String sellerID, String sellerUsername, LocalDateTime createDateTime) {
        super(id);
        this.rating = rating;
        this.comment = comment;
        this.customerID = customerID;
        this.customerUsername = customerUsername;
        this.productID = productID;
        this.productName = productName;
        this.sellerID = sellerID;
        this.sellerUsername = sellerUsername;
        this.createDateTime = createDateTime;
    }

    public Review(Review other) {
        super(other.getId());
        this.rating = other.rating;
        this.comment = other.comment;
        this.customerID = other.customerID;
        this.customerUsername = other.customerUsername;
        this.productID = other.productID;
        this.productName = other.productName;
        this.sellerID = other.sellerID;
        this.sellerUsername = other.sellerUsername;
        this.createDateTime = other.createDateTime;
    }

    @Override
    public String toString() {
        return String.format("%s, %d, %s, %s, %s, %s, %s, %s, %s, %s",
                super.getId(),
                rating,
                comment,
                customerID,
                customerUsername,
                productID,
                productName,
                sellerID,
                sellerUsername,
                createDateTime);
    }

    public static String className() {
        return "Review";
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public LocalDateTime getCreateDate() {
        return createDateTime;
    }

    public void setCreateDate(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

}
