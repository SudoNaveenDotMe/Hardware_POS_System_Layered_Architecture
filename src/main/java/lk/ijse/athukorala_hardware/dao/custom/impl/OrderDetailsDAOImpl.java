package lk.ijse.athukorala_hardware.dao.custom.impl;

import lk.ijse.athukorala_hardware.dao.custom.OrderDetailsDAO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import lk.ijse.athukorala_hardware.entity.OrderDetails;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    @Override
    public boolean save(OrderDetails entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO orderDetail (order_id, item_id, qty, total_amount) VALUES (?,?,?,?)",
                entity.getOrderId(), entity.getItemId(), entity.getQty(), entity.getTotalAmount()
        );
    }

    @Override
    public List<OrderDetails> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }


    @Override
    public boolean update(OrderDetails entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetails search(String id) throws SQLException, ClassNotFoundException {
        return null;
    }
}
