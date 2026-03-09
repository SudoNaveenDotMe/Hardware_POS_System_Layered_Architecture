package lk.ijse.athukorala_hardware.bo.custom.impl;

import lk.ijse.athukorala_hardware.bo.custom.PlaceOrderBO;
import lk.ijse.athukorala_hardware.dao.DAOFactory;
import lk.ijse.athukorala_hardware.dao.custom.*;
import lk.ijse.athukorala_hardware.dao.custom.impl.ItemDAOImpl;
import lk.ijse.athukorala_hardware.dao.custom.impl.OrderDAOImpl;
import lk.ijse.athukorala_hardware.dao.custom.impl.OrderDetailsDAOImpl;
import lk.ijse.athukorala_hardware.dao.custom.impl.QueryDAOImpl;
import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.dto.OrderDTO;
import lk.ijse.athukorala_hardware.dto.OrderItemDTO;
import lk.ijse.athukorala_hardware.dto.tm.OrderTM;
import lk.ijse.athukorala_hardware.entity.CustomOrderEntity;
import lk.ijse.athukorala_hardware.entity.Order;
import lk.ijse.athukorala_hardware.entity.OrderDetails;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceOrderBOImpl implements PlaceOrderBO {

    /*private final OrderDAO orderDAO = new OrderDAOImpl();
    private final OrderDetailsDAO orderDetailDAO = new OrderDetailsDAOImpl();
    private final ItemDAO itemDAO = new ItemDAOImpl();
    private final QueryDAO queryDAO = new QueryDAOImpl();*/

    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.CUSTOMER);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ITEM);
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDER);
    OrderDetailsDAO orderDetailDAO = (OrderDetailsDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDER_DETAIL);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.QUERY);

    @Override
    public boolean placeOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false); // TRANSACTION START

            // 1. Save Order
            boolean isOrderSaved = orderDAO.save(new Order(
                    new java.sql.Date(orderDTO.getOrderDate().getTime()),
                    orderDTO.getCustomerId()
            ));

            if (!isOrderSaved) {
                conn.rollback();
                return false;
            }

            // 2. Get latest Order ID
            int orderId = orderDAO.getLatestOrderId();
            if (orderId == -1) {
                conn.rollback();
                return false;
            }

            // 3. Save Order Details & Update Item Qty
            for (OrderItemDTO item : orderDTO.getOrderItems()) {
                boolean isDetailSaved = orderDetailDAO.save(new OrderDetails(
                        orderId, item.getItemId(), item.getQty(), item.getPrice()
                ));

                if (!isDetailSaved) {
                    conn.rollback();
                    return false;
                }

                boolean isItemUpdated = itemDAO.updateQty(item.getItemId(), item.getQty());
                if (!isItemUpdated) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit(); // TRANSACTION COMMIT

            // Print Invoice
            try {
                printOrderInvoice(orderId);
            } catch (JRException e) {
                e.printStackTrace(); // Non-fatal, order is already saved
            }

            return true;

        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public List<OrderTM> getAllOrders() throws SQLException, ClassNotFoundException {
        List<CustomOrderEntity> customEntities = queryDAO.getOrdersWithTotals();
        List<OrderTM> orderTMs = new ArrayList<>();

        for (CustomOrderEntity c : customEntities) {
            orderTMs.add(new OrderTM(
                    c.getOrderId(),
                    c.getCustomerId(),
                    c.getOrderDate(),
                    c.getOrderTotal()
            ));
        }
        return orderTMs;
    }

    @Override
    public void printOrderInvoice(int orderId) throws SQLException, JRException {
        Connection conn = DBConnection.getInstance().getConnection();
        InputStream reportObject = getClass().getResourceAsStream("/lk/ijse/athukorala_hardware/reports/Hardaware_Invoice.jrxml");
        JasperReport jr = JasperCompileManager.compileReport(reportObject);
        HashMap<String, Object> params = new HashMap<>();
        params.put("ORDER_ID", orderId);
        JasperPrint jp = JasperFillManager.fillReport(jr, params, conn);
        JasperViewer.viewReport(jp, false);
    }
}