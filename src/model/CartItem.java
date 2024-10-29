/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import base.Model;

import java.time.LocalDate;

/**
 * @author trann
 */
public class CartItem extends Model {

    private String customerID;
    private String customerUsername;
    private String productID;
    private String productName;
    private double productPrice;
    private String sellerID;
    private String sellerUsername;
    private int quantity;
    private LocalDate addDate;
    private double totalMoney;

    public CartItem(String id, String customerID, String customerUsername, String productID, String productName,
                    double productPrice, String sellerID, String sellerUsername, int quantity, LocalDate addDate,
                    double totalMoney) {
        super(id);
        this.customerID = customerID;
        this.customerUsername = customerUsername;
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.sellerID = sellerID;
        this.sellerUsername = sellerUsername;
        this.quantity = quantity;
        this.addDate = addDate;
        this.totalMoney = totalMoney;
    }

    public CartItem(CartItem other) {
        super(other.getId());
        this.customerID = other.customerID;
        this.customerUsername = other.customerUsername;
        this.productID = other.productID;
        this.productName = other.productName;
        this.productPrice = other.productPrice;
        this.sellerID = other.sellerID;
        this.sellerUsername = other.sellerUsername;
        this.quantity = other.quantity;
        this.addDate = other.addDate;
        this.totalMoney = other.totalMoney;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %.3f, %s, %s, %d, %s, %.3f",
                super.getId(),
                customerID,
                customerUsername,
                productID,
                productName,
                productPrice,
                sellerID,
                sellerUsername,
                quantity,
                addDate,
                totalMoney);
    }

    public static String className() {
        return "Cart's item";
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getAddDate() {
        return addDate;
    }

    public void setAddDate(LocalDate addDate) {
        this.addDate = addDate;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
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

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        super.getId(),
                        customerID,
                        customerUsername,
                        productID,
                        productName,
                        productPrice,
                        sellerID,
                        sellerUsername,
                        quantity,
                        addDate,
                        totalMoney
                };
    }
}
