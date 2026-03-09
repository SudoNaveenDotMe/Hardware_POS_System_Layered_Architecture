package lk.ijse.athukorala_hardware.dao.custom.impl;

import lk.ijse.athukorala_hardware.dao.custom.ItemDAO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import lk.ijse.athukorala_hardware.entity.Item;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {


    @Override
    public List<Item> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item");

        List<Item> itemList = new ArrayList<>();
        while (resultSet.next()) {
            itemList.add(new Item(
                    resultSet.getInt("item_id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("qty")
            ));
        }
        return itemList;
    }

    @Override
    public boolean save(Item entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO item (name, price, stock_qty) VALUES(?,?,?)",
                entity.getName(), entity.getPrice(), entity.getQty()
        );
    }

    @Override
    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE item SET name = ?, price = ?, stock_qty = ? WHERE item_id = ?",
                entity.getName(), entity.getPrice(), entity.getQty(), entity.getId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM item WHERE item_id = ?", id);    }

    @Override
    public Item search(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM item WHERE item_id = ?", id);
        if (result.next()) {
            return new Item(
                    result.getInt("item_id"),
                    result.getString("name"),
                    result.getDouble("price"),
                    result.getInt("stock_qty")
            );
        }
        return null;
    }

    @Override
    public List<String> getIds() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT item_id FROM item");
        List<String> itemIdList = new ArrayList<>();
        while (rs.next()) {
            itemIdList.add(rs.getString("item_id"));
        }
        return itemIdList;
    }

    @Override
    public boolean updateQty(int itemId, int qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE item SET stock_qty = stock_qty - ? WHERE item_id = ?", qty, itemId);
    }
}
