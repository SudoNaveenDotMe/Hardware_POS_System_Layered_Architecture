package lk.ijse.athukorala_hardware.bo.custom;

import lk.ijse.athukorala_hardware.bo.SuperBO;
import lk.ijse.athukorala_hardware.dto.DriverDTO;
import net.sf.jasperreports.engine.JRException;

import java.sql.SQLException;
import java.util.List;

public interface DriverBO extends SuperBO {
    boolean saveDriver(DriverDTO driverDTO) throws SQLException, ClassNotFoundException;
    boolean updateDriver(DriverDTO driverDTO) throws SQLException, ClassNotFoundException;
    boolean deleteDriver(String id) throws SQLException, ClassNotFoundException;
    DriverDTO searchDriver(String id) throws SQLException, ClassNotFoundException;
    List<DriverDTO> getDrivers() throws SQLException, ClassNotFoundException;
    List<String> getAllDriverIds() throws SQLException, ClassNotFoundException;
    void printDriverReport() throws SQLException, JRException;
}
