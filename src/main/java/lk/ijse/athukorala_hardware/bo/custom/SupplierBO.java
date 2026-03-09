package lk.ijse.athukorala_hardware.bo.custom;

import lk.ijse.athukorala_hardware.bo.SuperBO;
import lk.ijse.athukorala_hardware.dto.SupplierDTO;
import net.sf.jasperreports.engine.JRException;

import java.sql.SQLException;
import java.util.List;

public interface SupplierBO extends SuperBO {
    boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException;
    boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException, ClassNotFoundException;
    boolean deleteSupplier(String id) throws SQLException, ClassNotFoundException;
    SupplierDTO searchSupplier(String id) throws SQLException, ClassNotFoundException;
    List<SupplierDTO> getSuppliers() throws SQLException, ClassNotFoundException;
    List<String> getAllSupplierIds() throws SQLException, ClassNotFoundException;
    void printSupplierReport() throws SQLException, JRException;
}
