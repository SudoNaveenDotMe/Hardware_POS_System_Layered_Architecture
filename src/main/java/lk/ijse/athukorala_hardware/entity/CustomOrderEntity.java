package lk.ijse.athukorala_hardware.entity;

import java.util.Date;

public class CustomOrderEntity {
    private final int orderId;
    private final int customerId;
    private final Date orderDate;
    private final double orderTotal;

    public CustomOrderEntity(int orderId, int customerId, Date orderDate, double orderTotal) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.orderTotal = orderTotal;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public double getOrderTotal() {
        return orderTotal;
    }
}
