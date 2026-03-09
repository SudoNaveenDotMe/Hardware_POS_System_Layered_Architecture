package lk.ijse.athukorala_hardware.dao.custom.impl;

import lk.ijse.athukorala_hardware.dao.custom.OrderDAO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import lk.ijse.athukorala_hardware.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public int getLatestOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT order_id FROM `order` ORDER BY order_id DESC LIMIT 1");
        if (rs.next()) {
            return rs.getInt("order_id");
        }
        return -1;
    }

    @Override
    public List<Order> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Order entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO `order` (order_date, customer_id) VALUES (?,?)",
                entity.getOrderDate(), entity.getCustomerId());
    }

    @Override
    public boolean update(Order entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Order search(String id) throws SQLException, ClassNotFoundException {
        return null;
    }
}
