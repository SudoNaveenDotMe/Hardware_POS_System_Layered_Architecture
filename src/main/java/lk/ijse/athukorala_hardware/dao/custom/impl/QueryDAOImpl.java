package lk.ijse.athukorala_hardware.dao.custom.impl;

import lk.ijse.athukorala_hardware.dao.custom.QueryDAO;
import lk.ijse.athukorala_hardware.dao.util.CrudUtil;
import lk.ijse.athukorala_hardware.entity.CustomOrderEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public List<CustomOrderEntity> getOrdersWithTotals() throws SQLException, ClassNotFoundException {
        String sql = "SELECT o.order_id, o.customer_id, o.order_date, " +
                "COALESCE(SUM(od.total_amount), 0) AS order_total " +
                "FROM `order` o " +
                "LEFT JOIN orderDetail od ON o.order_id = od.order_id " +
                "GROUP BY o.order_id " +
                "ORDER BY o.order_id DESC";

        ResultSet resultSet = CrudUtil.execute(sql);
        List<CustomOrderEntity> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(new CustomOrderEntity(
                    resultSet.getInt("order_id"),
                    resultSet.getInt("customer_id"),
                    resultSet.getDate("order_date"),
                    resultSet.getDouble("order_total")
            ));
        }
        return list;
    }
}
