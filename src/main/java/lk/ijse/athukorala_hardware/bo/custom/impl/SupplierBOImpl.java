package lk.ijse.athukorala_hardware.bo.custom.impl;

import lk.ijse.athukorala_hardware.bo.custom.SupplierBO;
import lk.ijse.athukorala_hardware.dao.DAOFactory;
import lk.ijse.athukorala_hardware.dao.custom.ItemDAO;
import lk.ijse.athukorala_hardware.dao.custom.SupplierDAO;
import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.dto.SupplierDTO;
import lk.ijse.athukorala_hardware.entity.Supplier;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierBOImpl implements SupplierBO {
    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SUPPLIER);

    @Override
    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException {
        return supplierDAO.save(new Supplier(
                supplierDTO.getName(), supplierDTO.getContact(), supplierDTO.getEmail(), supplierDTO.getAddress()
        ));
    }

    @Override
    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException {
        return supplierDAO.update(new Supplier(
                supplierDTO.getId(), supplierDTO.getName(), supplierDTO.getContact(), supplierDTO.getEmail(), supplierDTO.getAddress()
        ));
    }

    @Override
    public boolean deleteSupplier(String id) throws SQLException, ClassNotFoundException {
        return supplierDAO.delete(id);
    }

    @Override
    public SupplierDTO searchSupplier(String id) throws SQLException, ClassNotFoundException {
        Supplier supplier = supplierDAO.search(id);
        if (supplier != null) {
            return new SupplierDTO(
                    supplier.getId(), supplier.getName(), supplier.getContact(), supplier.getEmail(), supplier.getAddress()
            );
        }
        return null;
    }

    @Override
    public List<SupplierDTO> getSuppliers() throws SQLException, ClassNotFoundException {
        List<Supplier> suppliers = supplierDAO.getAll();
        List<SupplierDTO> supplierDTOs = new ArrayList<>();

        for (Supplier supplier : suppliers) {
            supplierDTOs.add(new SupplierDTO(
                    supplier.getId(), supplier.getName(), supplier.getContact(), supplier.getEmail(), supplier.getAddress()
            ));
        }
        return supplierDTOs;
    }

    @Override
    public List<String> getAllSupplierIds() throws SQLException, ClassNotFoundException {
        return supplierDAO.getAllSupplierIds();
    }

    @Override
    public void printSupplierReport() throws SQLException, JRException {
        Connection conn = DBConnection.getInstance().getConnection();
        InputStream reportObject = getClass().getResourceAsStream("/lk/ijse/athukorala_hardware/reports/Hardware_Supplier.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObject);
        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
        JasperViewer.viewReport(jp, false);
    }
}
