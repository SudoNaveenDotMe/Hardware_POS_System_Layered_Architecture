package lk.ijse.athukorala_hardware.entity;

import java.util.Date;

public class Order {
    private int orderId;
    private Date orderDate;
    private int customerId;

    public Order() {
    }

    public Order(Date orderDate, int customerId) {
        this.orderDate = orderDate;
        this.customerId = customerId;
    }

    public Order(int orderId, Date orderDate, int customerId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
