package lk.ijse.athukorala_hardware.dao.custom.impl;

import lk.ijse.athukorala_hardware.dao.custom.DriverDAO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import lk.ijse.athukorala_hardware.entity.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements DriverDAO {
    @Override
    public List<String> getAllDriverIds() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT driver_id FROM driver");
        List<String> driverIdList = new ArrayList<>();

        while (rs.next()) {
            driverIdList.add(rs.getString("driver_id"));
        }
        return driverIdList;
    }

    @Override
    public List<Driver> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM driver");
        List<Driver> driverList = new ArrayList<>();

        while (rs.next()) {
            driverList.add(new Driver(
                    rs.getInt("driver_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("address")
            ));
        }
        return driverList;
    }

    @Override
    public boolean save(Driver entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO driver (name, contact, address) VALUES (?, ?, ?)",
                entity.getName(), entity.getContact(), entity.getAddress()
        );
    }

    @Override
    public boolean update(Driver entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE driver SET name = ?, contact = ?, address = ? WHERE driver_id = ?",
                entity.getName(), entity.getContact(), entity.getAddress(), entity.getId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM driver WHERE driver_id = ?", id);
    }

    @Override
    public Driver search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM driver WHERE driver_id = ?", id);

        if (rs.next()) {
            return new Driver(
                    rs.getInt("driver_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("address")
            );
        }
        return null;
    }
}
