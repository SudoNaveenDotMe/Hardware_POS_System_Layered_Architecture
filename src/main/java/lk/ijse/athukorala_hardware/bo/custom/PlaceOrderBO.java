package lk.ijse.athukorala_hardware.bo.custom;

import lk.ijse.athukorala_hardware.bo.SuperBO;
import lk.ijse.athukorala_hardware.dao.SuperDAO;
import lk.ijse.athukorala_hardware.dto.OrderDTO;
import lk.ijse.athukorala_hardware.dto.tm.OrderTM;
import net.sf.jasperreports.engine.JRException;

import java.sql.SQLException;
import java.util.List;

public interface PlaceOrderBO extends SuperBO {
    boolean placeOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException;
    List<OrderTM> getAllOrders() throws SQLException, ClassNotFoundException;
    void printOrderInvoice(int orderId) throws SQLException, JRException;
}
