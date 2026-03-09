package lk.ijse.athukorala_hardware.model;

import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.dto.ItemDTO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    public boolean saveItem(ItemDTO itemDTO) throws SQLException {
        boolean result = CrudUtil.execute(
                "INSERT INTO item (name,price,stock_qty) VALUES(?,?,?)",
                itemDTO.getName(),
                itemDTO.getPrice(),
                itemDTO.getQty()
        );

        return result;
    }

    public boolean updateItem(ItemDTO itemDTO) throws SQLException {
        boolean result = CrudUtil.execute(
                "UPDATE item SET name =? , price=?, stock_qty=? WHERE item_id LIKE ?",
                itemDTO.getName(),
                itemDTO.getPrice(),
                itemDTO.getQty(),
                itemDTO.getId()
        );

        return result;
    }

    public boolean deleteItem(String id) throws SQLException {
        boolean result = CrudUtil.execute(
                "DELETE FROM item WHERE item_id LIKE ?",
                id
        );

        return result;

    }

    public ItemDTO searchItem(String id) throws SQLException {
        ResultSet result = CrudUtil.execute("SELECT * FROM item WHERE item_id LIKE ?", id);

        if (result.next()) {
            int itemId = result.getInt("item_id");
            String name = result.getString("name");
            double price = result.getDouble("price");
            int qty = result.getInt("stock_qty");

            return new ItemDTO(itemId, name, price, qty);
        }
        return null;
    }

    public List<ItemDTO> getItem() throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM item";

        PreparedStatement ptsm = conn.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();

        List<ItemDTO> itemDTOList = new ArrayList<>();

        while (resultSet.next()) {
            ItemDTO itemDTO = new ItemDTO(
                    resultSet.getInt("item_id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("stock_qty")

            );
            itemDTOList.add(itemDTO);
        }
        return itemDTOList;
    }

    public List<String> getAllItemIds() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT item_id FROM item");
        List<String> itemIdList = new ArrayList<>();

        while (rs.next()) {
            itemIdList.add(rs.getString("item_id"));
        }

        return itemIdList;
    }

    public boolean decreaseItemQty(int itemId, int qty) throws SQLException {
        return CrudUtil.execute("UPDATE item SET stock_qty = stock_qty - ? WHERE item_id =?",
                qty,
                itemId
        );
    }

    public void printItemReport() throws SQLException, JRException {
        Connection conn = DBConnection.getInstance().getConnection();
        InputStream reportObject = getClass().getResourceAsStream("/lk/ijse/athukorala_hardware/reports/Hardware_items.jrxml");


        JasperReport jr = JasperCompileManager.compileReport(reportObject);

        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);

        JasperViewer.viewReport(jp,false);
    }

}
