package lk.ijse.athukorala_hardware.dto.tm;

import javafx.scene.control.Button;

public class OrderItemTM {
    private int itemId;
    private String itemName;
    private double unitPrice;
    private int orderQty;
    private double itemTotal;
    private Button btn;

    public OrderItemTM() {
    }

    public OrderItemTM(int itemId, String itemName, double unitPrice, int orderQty, double itemTotal, Button btn) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.orderQty = orderQty;
        this.itemTotal = itemTotal;
        this.btn = btn;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "OrderItemTM{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", unitPrice=" + unitPrice +
                ", orderQty=" + orderQty +
                ", itemTotal=" + itemTotal +
                ", btn=" + btn +
                '}';
    }
}
