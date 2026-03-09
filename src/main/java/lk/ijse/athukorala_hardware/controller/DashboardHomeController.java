package lk.ijse.athukorala_hardware.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.athukorala_hardware.model.DashboardModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardHomeController implements Initializable {

    @FXML
    private Label lblCustomerCount;

    @FXML
    private Label lblItemCount;

    @FXML
    private Label lblOrderCount;

    @FXML
    private Label lblTotalEarnings;

    private final DashboardModel dashboardModel = new DashboardModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        try {
            int customerCount = dashboardModel.getCustomerCount();
            int itemCount = dashboardModel.getItemCount();
            int orderCount = dashboardModel.getOrderCount();
            double totalEarnings = dashboardModel.getTotalEarnings();

            lblCustomerCount.setText(String.valueOf(customerCount));
            lblItemCount.setText(String.valueOf(itemCount));
            lblOrderCount.setText(String.valueOf(orderCount));
            lblTotalEarnings.setText(String.format("Rs. %.2f", totalEarnings));

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load dashboard data!").show();
        }
    }
}