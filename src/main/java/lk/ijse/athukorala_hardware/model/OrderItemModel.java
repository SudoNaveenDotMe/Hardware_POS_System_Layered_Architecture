package lk.ijse.athukorala_hardware.model;

import lk.ijse.athukorala_hardware.dto.OrderItemDTO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;

import java.util.List;

public class OrderItemModel {
    private final ItemModel itemModel = new ItemModel();

    public void saveOrderItems(int orderId, List<OrderItemDTO> orderItemList) throws Exception {
        for (OrderItemDTO orderItemDTO : orderItemList){
            if (CrudUtil.execute(
                    "INSERT INTO orderDetail (order_id, item_id, qty,total_amount) VALUES (?,?,?,?)",
                    orderId,
                    orderItemDTO.getItemId(),
                    orderItemDTO.getQty(),
                    orderItemDTO.getPrice()
            )){
                if (!itemModel.decreaseItemQty(
                        orderItemDTO.getItemId(),
                        orderItemDTO.getQty()
                )){
                    throw new Exception("Something went wrong when decreasing qty!!");
                }

            }else {
                throw new Exception("Something went wrong when inserting data into order Items!!");
            }
        }
    }
}
