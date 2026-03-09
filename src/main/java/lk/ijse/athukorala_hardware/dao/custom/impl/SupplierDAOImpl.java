package lk.ijse.athukorala_hardware.dao.custom.impl;

import lk.ijse.athukorala_hardware.dao.custom.SupplierDAO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import lk.ijse.athukorala_hardware.entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {
    @Override
    public List<String> getAllSupplierIds() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT supplier_id FROM supplier");
        List<String> supplierIdList = new ArrayList<>();

        while (rs.next()) {
            supplierIdList.add(rs.getString("supplier_id"));
        }
        return supplierIdList;
    }

    @Override
    public List<Supplier> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM supplier");
        List<Supplier> supplierList = new ArrayList<>();

        while (rs.next()) {
            supplierList.add(new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("email"),
                    rs.getString("address")
            ));
        }
        return supplierList;
    }

    @Override
    public boolean save(Supplier entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO supplier (name, contact, address, email) VALUES (?, ?, ?, ?)",
                entity.getName(), entity.getContact(), entity.getAddress(), entity.getEmail()
        );
    }

    @Override
    public boolean update(Supplier entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE supplier SET name = ?, contact = ?, address = ?, email = ? WHERE supplier_id = ?",
                entity.getName(), entity.getContact(), entity.getAddress(), entity.getEmail(), entity.getId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM supplier WHERE supplier_id = ?", id);
    }

    @Override
    public Supplier search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM supplier WHERE supplier_id = ?", id);

        if (rs.next()) {
            return new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("email"),
                    rs.getString("address")
            );
        }
        return null;
    }
}
