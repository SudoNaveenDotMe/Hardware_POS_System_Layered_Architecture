package lk.ijse.athukorala_hardware.dao.custom;

import lk.ijse.athukorala_hardware.dao.CrudDAO;
import lk.ijse.athukorala_hardware.entity.Order;

import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Order> {
    int getLatestOrderId() throws SQLException, ClassNotFoundException;
}
