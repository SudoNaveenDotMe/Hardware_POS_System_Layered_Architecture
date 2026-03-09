package lk.ijse.athukorala_hardware.dao.custom.impl;

import lk.ijse.athukorala_hardware.dao.custom.CustomerDAO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public List<Customer> getAll() throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer";
        PreparedStatement ptsm = conn.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();

        List<Customer> customerList = new ArrayList<>();
        while (resultSet.next()) {
            customerList.add(new Customer(
                    resultSet.getInt("cus_id"), resultSet.getString("name"),
                    resultSet.getString("contact"), resultSet.getString("address"),
                    resultSet.getString("email")
            ));
        }
        return customerList;
    }

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO customer (name,contact,address,email) VALUES(?,?,?,?)",
                entity.getName(), entity.getContact(), entity.getAddress(), entity.getEmail()
        );
    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE customer SET name =? , contact=?, address = ?, email=? WHERE cus_id LIKE ?",
                entity.getName(), entity.getContact(), entity.getAddress(), entity.getEmail(), entity.getId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM customer WHERE cus_id LIKE ?", id);
    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM customer WHERE cus_id LIKE ?", id);
        if (result.next()) {
            return new Customer(
                    result.getInt("cus_id"), result.getString("name"),
                    result.getString("contact"), result.getString("address"),
                    result.getString("email")
            );
        }
        return null;
    }

    @Override
    public List<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT cus_id FROM customer");
        List<String> customerIdList = new ArrayList<>();
        while (rs.next()) {
            customerIdList.add(rs.getString("cus_id"));
        }
        return customerIdList;
    }
}
