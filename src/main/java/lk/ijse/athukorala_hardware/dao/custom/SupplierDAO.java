package lk.ijse.athukorala_hardware.dao.custom;

import lk.ijse.athukorala_hardware.dao.CrudDAO;
import lk.ijse.athukorala_hardware.dao.SuperDAO;
import lk.ijse.athukorala_hardware.entity.Supplier;

import java.sql.SQLException;
import java.util.List;

public interface SupplierDAO extends CrudDAO<Supplier> {
    List<String> getAllSupplierIds() throws SQLException, ClassNotFoundException;
}
