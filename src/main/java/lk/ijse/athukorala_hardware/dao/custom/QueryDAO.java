package lk.ijse.athukorala_hardware.dao.custom;

import lk.ijse.athukorala_hardware.dao.SuperDAO;
import lk.ijse.athukorala_hardware.entity.CustomOrderEntity;

import java.sql.SQLException;
import java.util.List;

public interface QueryDAO extends SuperDAO {
    List<CustomOrderEntity> getOrdersWithTotals() throws SQLException, ClassNotFoundException;
}
