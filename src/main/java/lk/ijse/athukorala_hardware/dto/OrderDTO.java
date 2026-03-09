package lk.ijse.athukorala_hardware.dto;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    private List<OrderItemDTO> orderItems;
    private int orderId;
    private int customerId;
    private Date OrderDate;

    public OrderDTO() {
    }

    public OrderDTO(List<OrderItemDTO> orderItems, int customerId, Date orderDate) {
        this.orderItems = orderItems;
        this.customerId = customerId;
        OrderDate = orderDate;
    }

    public OrderDTO(List<OrderItemDTO> orderItems, int orderId, int customerId, Date orderDate) {
        this.orderItems = orderItems;
        this.orderId = orderId;
        this.customerId = customerId;
        OrderDate = orderDate;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderItems=" + orderItems +
                ", orderId=" + orderId +
                ", customerId=" + customerId +
                ", OrderDate=" + OrderDate +
                '}';
    }
}
