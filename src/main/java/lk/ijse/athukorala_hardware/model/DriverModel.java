package lk.ijse.athukorala_hardware.model;

import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.dto.DriverDTO;
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

public class DriverModel {

    public boolean saveDriver(DriverDTO driverDTO) throws SQLException {
        return CrudUtil.execute(
                "INSERT INTO driver (name, contact, address) VALUES (?, ?, ?)",
                driverDTO.getName(),
                driverDTO.getContact(),
                driverDTO.getAddress()
        );
    }

    public boolean updateDriver(DriverDTO driverDTO) throws SQLException {
        return CrudUtil.execute(
                "UPDATE driver SET name = ?, contact = ?, address = ? WHERE driver_id = ?",
                driverDTO.getName(),
                driverDTO.getContact(),
                driverDTO.getAddress(),
                driverDTO.getId()
        );
    }

    public boolean deleteDriver(String id) throws SQLException {
        return CrudUtil.execute(
                "DELETE FROM driver WHERE driver_id = ?",
                id
        );
    }

    public DriverDTO searchDriver(String id) throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT * FROM driver WHERE driver_id = ?",
                id
        );

        if (rs.next()) {
            return new DriverDTO(
                    rs.getInt("driver_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("address")
            );
        }
        return null;
    }

    public List<DriverDTO> getDrivers() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM driver";

        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<DriverDTO> driverList = new ArrayList<>();

        while (rs.next()) {
            DriverDTO driverDTO = new DriverDTO(
                    rs.getInt("driver_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("address")
            );
            driverList.add(driverDTO);
        }
        return driverList;
    }

    public List<String> getAllDriverIds() throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT driver_id FROM driver"
        );

        List<String> driverIdList = new ArrayList<>();

        while (rs.next()) {
            driverIdList.add(rs.getString("driver_id"));
        }

        return driverIdList;
    }

    public void printDriverReport() throws SQLException, JRException {
        Connection conn = DBConnection.getInstance().getConnection();
        InputStream reportObject = getClass().getResourceAsStream("/lk/ijse/athukorala_hardware/reports/Hardware_Driver.jrxml");


        JasperReport jr = JasperCompileManager.compileReport(reportObject);

        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);

        JasperViewer.viewReport(jp,false);

    }
}
