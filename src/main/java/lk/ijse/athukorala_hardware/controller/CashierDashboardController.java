package lk.ijse.athukorala_hardware.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.athukorala_hardware.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CashierDashboardController implements Initializable {

    @FXML
    public AnchorPane mainContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            dashboardOnAction(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void customerOnAction() throws IOException {
        Parent customerFXML = App.loadFXML("Customer");
        mainContent.getChildren().setAll(customerFXML);
    }

    @FXML
    public void itemOnAction(ActionEvent actionEvent) throws IOException {
        Parent itemFXML = App.loadFXML("Item");
        mainContent.getChildren().setAll(itemFXML);
    }

    @FXML
    public void driverOnAction(ActionEvent actionEvent) throws IOException {
        Parent driverFXML = App.loadFXML("Driver");
        mainContent.getChildren().setAll(driverFXML);
    }

    @FXML
    public void supplierOnAction(ActionEvent actionEvent) throws IOException {
        Parent supplierFXML = App.loadFXML("Supplier");
        mainContent.getChildren().setAll(supplierFXML);
    }

    @FXML
    public void orderOnAction(ActionEvent actionEvent) throws IOException {
        Parent orderFXML = App.loadFXML("Order");
        mainContent.getChildren().setAll(orderFXML);
    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        App.setRoot("Login",1000,600);
    }

    public void dashboardOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = App.loadFXML("DashboardHome");
        mainContent.getChildren().setAll(root);
    }
}
