package lk.ijse.athukorala_hardware.bo.custom.impl;

import lk.ijse.athukorala_hardware.bo.custom.DriverBO;
import lk.ijse.athukorala_hardware.dao.DAOFactory;
import lk.ijse.athukorala_hardware.dao.custom.DriverDAO;
import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.dto.DriverDTO;
import lk.ijse.athukorala_hardware.entity.Driver;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverBOImpl implements DriverBO {
    DriverDAO driverDAO = (DriverDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.DRIVER);

    @Override
    public boolean saveDriver(DriverDTO driverDTO) throws SQLException, ClassNotFoundException {
        return driverDAO.save(new Driver(
                driverDTO.getName(), driverDTO.getContact(), driverDTO.getAddress()
        ));
    }

    @Override
    public boolean updateDriver(DriverDTO driverDTO) throws SQLException, ClassNotFoundException {
        return driverDAO.update(new Driver(
                driverDTO.getId(), driverDTO.getName(), driverDTO.getContact(), driverDTO.getAddress()
        ));
    }

    @Override
    public boolean deleteDriver(String id) throws SQLException, ClassNotFoundException {
        return driverDAO.delete(id);
    }

    @Override
    public DriverDTO searchDriver(String id) throws SQLException, ClassNotFoundException {
        Driver driver = driverDAO.search(id);
        if (driver != null) {
            return new DriverDTO(
                    driver.getId(), driver.getName(), driver.getContact(), driver.getAddress()
            );
        }
        return null;
    }

    @Override
    public List<DriverDTO> getDrivers() throws SQLException, ClassNotFoundException {
        List<Driver> drivers = driverDAO.getAll();
        List<DriverDTO> driverDTOs = new ArrayList<>();

        for (Driver driver : drivers) {
            driverDTOs.add(new DriverDTO(
                    driver.getId(), driver.getName(), driver.getContact(), driver.getAddress()
            ));
        }
        return driverDTOs;
    }

    @Override
    public List<String> getAllDriverIds() throws SQLException, ClassNotFoundException {
        return driverDAO.getAllDriverIds();
    }

    @Override
    public void printDriverReport() throws SQLException, JRException {
        Connection conn = DBConnection.getInstance().getConnection();
        InputStream reportObject = getClass().getResourceAsStream("/lk/ijse/athukorala_hardware/reports/Hardware_Driver.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObject);
        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
        JasperViewer.viewReport(jp, false);
    }
}
