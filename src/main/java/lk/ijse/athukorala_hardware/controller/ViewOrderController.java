package lk.ijse.athukorala_hardware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.athukorala_hardware.dto.tm.OrderTM;
import lk.ijse.athukorala_hardware.model.OrderModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ViewOrderController implements Initializable {

    private final OrderModel orderModel = new OrderModel();
    @FXML
    private TableView<OrderTM> tblOrders;
    @FXML
    private TableColumn<OrderTM, Integer> colOrderId;
    @FXML
    private TableColumn<OrderTM, Integer> colCustomerId;
    @FXML
    private TableColumn<OrderTM, Date> colDate;
    @FXML
    private TableColumn<OrderTM, Double> colTotal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        loadAllOrders();
    }


    private void loadAllOrders() {
        try {
            List<OrderTM> orderList = orderModel.getAllOrders();
            ObservableList<OrderTM> obList = FXCollections.observableArrayList(orderList);

            tblOrders.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load orders: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }
}