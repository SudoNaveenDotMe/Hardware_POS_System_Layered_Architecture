package lk.ijse.athukorala_hardware.dto;

public class OrderItemDTO {
    private int orderItemId;
    private int itemId;
    private int qty;
    private double price;

    public OrderItemDTO() {
    }

    public OrderItemDTO(int itemId, int qty, double price) {
        this.itemId = itemId;
        this.qty = qty;
        this.price = price;
    }

    public OrderItemDTO(int orderItemId, int itemId, int qty, double price) {
        this.orderItemId = orderItemId;
        this.itemId = itemId;
        this.qty = qty;
        this.price = price;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "orderItemId=" + orderItemId +
                ", itemId=" + itemId +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
