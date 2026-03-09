package lk.ijse.athukorala_hardware.model;

import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardModel {

    public int getCustomerCount() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM customer");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int getItemCount() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM item");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int getOrderCount() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM `order`");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public double getTotalEarnings() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT SUM(total_amount) FROM orderDetail");
        if (rs.next()) {
            return rs.getDouble(1);
        }
        return 0.0;
    }
}