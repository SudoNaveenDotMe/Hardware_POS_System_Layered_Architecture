package lk.ijse.athukorala_hardware.bo.custom.impl;

import lk.ijse.athukorala_hardware.bo.custom.ItemBO;
import lk.ijse.athukorala_hardware.dao.DAOFactory;
import lk.ijse.athukorala_hardware.dao.custom.ItemDAO;
import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.dto.ItemDTO;
import lk.ijse.athukorala_hardware.entity.Item;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ITEM);

    @Override
    public List<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        List<Item> items = itemDAO.getAll();
        List<ItemDTO> itemDTOs = new ArrayList<>();
        for (Item item : items) {
            itemDTOs.add(new ItemDTO(
                    item.getId(), item.getName(), item.getPrice(), item.getQty()
            ));
        }
        return itemDTOs;
    }

    @Override
    public boolean saveItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return itemDAO.save(new Item(
                itemDTO.getName(), itemDTO.getPrice(), itemDTO.getQty()
        ));
    }

    @Override
    public boolean updateItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        return itemDAO.update(new Item(
                itemDTO.getId(), itemDTO.getName(), itemDTO.getPrice(), itemDTO.getQty()
        ));
    }

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(id);
    }

    @Override
    public ItemDTO searchItem(String id) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(id);
        if (item != null) {
            return new ItemDTO(item.getId(), item.getName(), item.getPrice(), item.getQty());
        }
        return null;
    }

    @Override
    public List<String> getAllItemIds() throws SQLException, ClassNotFoundException {
        return itemDAO.getIds();
    }

    @Override
    public boolean decreaseItemQty(int itemId, int qty) throws SQLException, ClassNotFoundException {
        return itemDAO.updateQty(itemId, qty);
    }

    @Override
    public void printItemReport() throws SQLException, JRException {
        Connection conn = DBConnection.getInstance().getConnection();
        InputStream reportObject = getClass().getResourceAsStream("/lk/ijse/athukorala_hardware/reports/Hardware_items.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObject);
        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
        JasperViewer.viewReport(jp, false);
    }
}
