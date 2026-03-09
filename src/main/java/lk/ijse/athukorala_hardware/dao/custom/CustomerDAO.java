package lk.ijse.athukorala_hardware.dao.custom;

import lk.ijse.athukorala_hardware.dao.CrudDAO;
import lk.ijse.athukorala_hardware.dao.SuperDAO;
import lk.ijse.athukorala_hardware.entity.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer> {
    List<String> getAllCustomerIds() throws SQLException, ClassNotFoundException;
}
