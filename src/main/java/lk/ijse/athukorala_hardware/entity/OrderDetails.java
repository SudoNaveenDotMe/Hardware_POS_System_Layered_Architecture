package lk.ijse.athukorala_hardware.entity;

public class OrderDetails {
    private int orderId;
    private int itemId;
    private int qty;
    private double totalAmount;

    public OrderDetails() {
    }

    public OrderDetails(int orderId, int itemId, int qty, double totalAmount) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.qty = qty;
        this.totalAmount = totalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
