package lk.ijse.athukorala_hardware.dao.custom;

import lk.ijse.athukorala_hardware.dao.CrudDAO;
import lk.ijse.athukorala_hardware.entity.User;

import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User> {
    User searchByEmail(String email) throws SQLException, ClassNotFoundException;
}
