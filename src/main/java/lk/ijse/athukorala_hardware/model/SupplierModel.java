package lk.ijse.athukorala_hardware.model;

import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.dto.SupplierDTO;
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

public class SupplierModel {


    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException {
        return CrudUtil.execute(
                "INSERT INTO supplier (name, contact, address,email) VALUES (?, ?, ?,?)",
                supplierDTO.getName(),
                supplierDTO.getContact(),
                supplierDTO.getAddress(),
                supplierDTO.getEmail()
        );
    }

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException {
        return CrudUtil.execute(
                "UPDATE supplier SET name = ?, contact = ?, address = ?, email = ? WHERE supplier_id = ?",
                supplierDTO.getName(),
                supplierDTO.getContact(),
                supplierDTO.getAddress(),
                supplierDTO.getEmail(),
                supplierDTO.getId()
        );
    }

    public boolean deleteSupplier(String id) throws SQLException {
        return CrudUtil.execute(
                "DELETE FROM supplier WHERE supplier_id = ?",
                id
        );
    }

    public SupplierDTO searchSupplier(String id) throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT * FROM supplier WHERE supplier_id = ?",
                id
        );

        if (rs.next()) {
            return new SupplierDTO(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("address"),
                    rs.getString("email")
            );
        }
        return null;
    }

    public List<SupplierDTO> getSuppliers() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM supplier";

        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<SupplierDTO> supplierList = new ArrayList<>();

        while (rs.next()) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact"),
                    rs.getString("email"),
                    rs.getString("address")

            );
            supplierList.add(supplierDTO);
        }
        return supplierList;
    }

    public List<String> getAllSupplierIds() throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT supplier_id FROM supplier"
        );

        List<String> supplierIdList = new ArrayList<>();

        while (rs.next()) {
            supplierIdList.add(rs.getString("supplier_id"));
        }

        return supplierIdList;
    }

    public void printSupplierReport() throws SQLException, JRException {
        Connection conn = DBConnection.getInstance().getConnection();
        InputStream reportObject = getClass().getResourceAsStream("/lk/ijse/athukorala_hardware/reports/Hardware_Supplier.jrxml");


        JasperReport jr = JasperCompileManager.compileReport(reportObject);

        JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);

        JasperViewer.viewReport(jp,false);
    }
}
