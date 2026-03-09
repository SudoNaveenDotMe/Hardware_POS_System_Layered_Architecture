package lk.ijse.athukorala_hardware.bo.custom;

import lk.ijse.athukorala_hardware.bo.SuperBO;
import lk.ijse.athukorala_hardware.dto.ItemDTO;
import net.sf.jasperreports.engine.JRException;

import java.sql.SQLException;
import java.util.List;

public interface ItemBO extends SuperBO {
    List<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;
    boolean saveItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;
    boolean updateItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;
    boolean deleteItem(String id) throws SQLException, ClassNotFoundException;
    ItemDTO searchItem(String id) throws SQLException, ClassNotFoundException;
    List<String> getAllItemIds() throws SQLException, ClassNotFoundException;
    boolean decreaseItemQty(int itemId, int qty) throws SQLException, ClassNotFoundException;
    void printItemReport() throws SQLException, JRException;
}
