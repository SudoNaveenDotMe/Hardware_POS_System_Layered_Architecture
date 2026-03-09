package lk.ijse.athukorala_hardware.dao.custom;

import lk.ijse.athukorala_hardware.dao.CrudDAO;
import lk.ijse.athukorala_hardware.dao.SuperDAO;
import lk.ijse.athukorala_hardware.entity.Driver;

import java.sql.SQLException;
import java.util.List;

public interface DriverDAO extends CrudDAO<Driver> {
    List<String> getAllDriverIds() throws SQLException, ClassNotFoundException;
}
