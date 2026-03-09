package lk.ijse.athukorala_hardware.dao.custom;

import lk.ijse.athukorala_hardware.dao.CrudDAO;
import lk.ijse.athukorala_hardware.entity.Item;


import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item> {
    List<String> getIds() throws SQLException, ClassNotFoundException;
    boolean updateQty(int id, int qty) throws SQLException, ClassNotFoundException;
}
