/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author trann
 */
public class Order extends CartItem {

    public enum Status {
        NONE,
        NOT_CONFIRM,
        CONFIRMED,
        PACKAGING,
        DELIVERING,
        DELIVERED,
        CANCELED;
    }

    public enum Condition {
        NONE,
        GOOD,
        NORMAL,
        BAD,
        BROKEN,
        UNCORRECT,
        MISSING;
    }

    public enum Payment {
        NONE,
        APP,
        BANKING,
        SHIP_COD;
    }

    private Status status;
    private LocalDate deliveryDate;
    private Condition condition;
    private Payment typeOfPayment;

    public Order(String id, String customerID, String customerUsername, String productID, String productName,
            double productPrice, String sellerID, String sellerUsername, int quantity, LocalDate addDate,
            double totalMoney, Status status, LocalDate deliveryDate, Condition condition, Payment typeOfPayment) {
        super(id, customerID, customerUsername, productID, productName, productPrice, sellerID, sellerUsername,
                quantity, addDate, totalMoney);
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.condition = condition;
        this.typeOfPayment = typeOfPayment;
    }

    public Order(CartItem other, Status status, LocalDate deliveryDate, Condition condition, Payment typeOfPayment) {
        super(other);
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.condition = condition;
        this.typeOfPayment = typeOfPayment;
    }

    public Order(Order other) {
        super(other.getId(), other.getCustomerID(), other.getCustomerUsername(), other.getProductID(),
                other.getProductName(),
                other.getProductPrice(), other.getCustomerID(), other.getCustomerUsername(), other.getQuantity(),
                other.getAddDate(), other.getTotalMoney());
        this.status = other.status;
        this.deliveryDate = other.deliveryDate;
        this.condition = other.condition;
        this.typeOfPayment = other.typeOfPayment;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", super.toString(), status, deliveryDate, condition);
    }

    public static String className() {
        return "Order";
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Payment getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(Payment typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

}
