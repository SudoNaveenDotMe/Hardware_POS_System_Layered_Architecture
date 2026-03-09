package lk.ijse.athukorala_hardware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.athukorala_hardware.dto.CustomerDTO;
import lk.ijse.athukorala_hardware.dto.ItemDTO;
import lk.ijse.athukorala_hardware.dto.OrderDTO;
import lk.ijse.athukorala_hardware.dto.OrderItemDTO;
import lk.ijse.athukorala_hardware.dto.tm.OrderItemTM;
import lk.ijse.athukorala_hardware.model.CustomerModel;
import lk.ijse.athukorala_hardware.model.ItemModel;
import lk.ijse.athukorala_hardware.model.OrderModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {
    private final CustomerModel customerModel = new CustomerModel();
    private final ItemModel itemModel = new ItemModel();
    private final OrderModel orderModel = new OrderModel();

    @FXML
    public ComboBox<String> cmbCustomer;
    @FXML
    public ComboBox<String> cmbItem;

    @FXML
    public Label lblCustomerName;
    @FXML
    public Label lblCustomerAddress;
    @FXML
    public Label lblItemName;
    @FXML
    public TextField txtItemPrice;
    @FXML
    public TextField txtStock;
    @FXML
    public TextField txtQty;

    @FXML
    public TableView<OrderItemTM> tblCart;
    @FXML
    public TableColumn<OrderItemTM, Integer> colIndex;
    @FXML
    public TableColumn<OrderItemTM, String> colItemName;
    @FXML
    public TableColumn<OrderItemTM, Integer> colQty;
    @FXML
    public TableColumn<OrderItemTM, Double> colUnitPrice;
    @FXML
    public TableColumn<OrderItemTM, Double> colTotalPrice;
    @FXML
    public TableColumn<OrderItemTM, Button> colAction;
    @FXML
    public Label lblNetTotal;

    ObservableList<OrderItemTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCustomerIds();
        loadItemIds();
    }

    private void loadCustomerIds() {
        try {
            List<String> allCustomerIds = customerModel.getAllCustomerIds();
            ObservableList<String> obList = FXCollections.observableArrayList();

            obList.addAll(allCustomerIds);

            cmbCustomer.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadItemIds() {

        try {
            List<String> itemIdList = itemModel.getAllItemIds();
            ObservableList<String> obList = FXCollections.observableArrayList();

            obList.addAll(itemIdList);
            cmbItem.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    public void selectCustomerOnAction(ActionEvent actionEvent) {
        String selectedCustomer = cmbCustomer.getSelectionModel().getSelectedItem();
        System.out.println("Selected Customer : " + selectedCustomer);

        try {
            CustomerDTO customerDTO = customerModel.searchCustomer(selectedCustomer);
            lblCustomerName.setText(customerDTO.getName());
            lblCustomerAddress.setText(customerDTO.getAddress());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void selectItemOnAction(ActionEvent actionEvent) {
        String selectedItem = cmbItem.getSelectionModel().getSelectedItem();
        System.out.println("selectedItem : " + selectedItem);

        try {
            ItemDTO itemDTO = itemModel.searchItem(selectedItem);
            lblItemName.setText(itemDTO.getName());
            txtStock.setText(String.valueOf(itemDTO.getQty()));
            txtItemPrice.setText(String.valueOf(itemDTO.getPrice()));

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void resetOnAction(ActionEvent actionEvent) {
        cmbCustomer.getSelectionModel().clearSelection();
        cmbItem.getSelectionModel().clearSelection();

        cmbCustomer.setValue(null);
        cmbItem.setValue(null);

        lblCustomerName.setText("");
        lblCustomerAddress.setText("");
        lblItemName.setText("");

        txtStock.clear();
        txtItemPrice.clear();
    }

    public void addToCartOnAction(ActionEvent actionEvent) {
        String itemId = cmbItem.getSelectionModel().getSelectedItem();
        String itemName = lblItemName.getText();
        String itemQty = txtStock.getText();
        String itemPrice = txtItemPrice.getText();
        String orderQty = txtQty.getText();

        double total = Double.parseDouble(itemPrice) * Integer.parseInt(orderQty);
        Button deleteBtn = new Button();
        deleteBtn.setText("Remove");

        if (Integer.parseInt(itemQty) >= Integer.parseInt(orderQty)) {
            OrderItemTM orderItemTM = new OrderItemTM(
                    Integer.parseInt(itemId),
                    itemName,
                    Double.parseDouble(itemPrice),
                    Integer.parseInt(orderQty),
                    total,
                    deleteBtn
            );

            deleteBtn.setOnAction(e -> {
                obList.remove(orderItemTM);
                loadOrderItemTable();
                tblCart.refresh();
            });

            obList.add(orderItemTM);
            tblCart.setItems(obList);

            colIndex.setCellValueFactory(new PropertyValueFactory<>("itemId"));
            colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("orderQty"));
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("itemTotal"));
            colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

            loadOrderItemTable();

        } else {
            new Alert(Alert.AlertType.ERROR, "Wrong Qty").show();
        }
    }

    private void loadOrderItemTable() {
        tblCart.setItems(obList);

        double total = 0.0;

        for (OrderItemTM tm : obList) {
            total += tm.getItemTotal();
        }

        lblNetTotal.setText(String.valueOf(total));
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        try {
            String selectedCustomerId = cmbCustomer.getSelectionModel().getSelectedItem();

            List<OrderItemDTO> orderItem = new ArrayList<>();

            for (OrderItemTM orderItemTM : obList) {
                OrderItemDTO orderItemDTO = new OrderItemDTO(
                        orderItemTM.getItemId(),
                        orderItemTM.getOrderQty(),
                        orderItemTM.getUnitPrice()
                );

                orderItem.add(orderItemDTO);
            }

            OrderDTO orderDTO = new OrderDTO(
                    orderItem,
                    Integer.parseInt(selectedCustomerId),
                    new Date()
            );

            boolean isSaved = orderModel.placeOrder(orderDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!!");

        }

    }
}
