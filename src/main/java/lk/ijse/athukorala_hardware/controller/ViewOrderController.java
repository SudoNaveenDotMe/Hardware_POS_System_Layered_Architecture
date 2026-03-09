package lk.ijse.athukorala_hardware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import lk.ijse.athukorala_hardware.bo.BOFactory;
import lk.ijse.athukorala_hardware.bo.custom.PlaceOrderBO;
import lk.ijse.athukorala_hardware.dto.tm.OrderTM;
import net.sf.jasperreports.engine.JRException;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ViewOrderController implements Initializable {

    private final PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getInstance().getBO(BOFactory.BOType.ORDER);

    @FXML
    private TextField txtSearch;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
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
    @FXML
    private TableColumn<OrderTM, Void> colAction;

    private ObservableList<OrderTM> masterData = FXCollections.observableArrayList();
    private FilteredList<OrderTM> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        setupActionColumn();
        loadAllOrders();
        setupSearchAndFilter();
    }

    private void loadAllOrders() {
        try {
            List<OrderTM> orderList = placeOrderBO.getAllOrders();
            masterData.clear();
            masterData.addAll(orderList);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load orders: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void setupSearchAndFilter() {
        filteredData = new FilteredList<>(masterData, b -> true);

        // Add listeners to all three inputs
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        dpStartDate.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        dpEndDate.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        SortedList<OrderTM> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblOrders.comparatorProperty());
        tblOrders.setItems(sortedData);
    }

    private void applyFilters() {
        filteredData.setPredicate(orderTM -> {
            boolean matchesSearch = true;
            boolean matchesDateRange = true;

            // 1. Text Search Filter
            String searchText = txtSearch.getText();
            if (searchText != null && !searchText.isEmpty()) {
                String lowerCaseFilter = searchText.toLowerCase();
                matchesSearch = String.valueOf(orderTM.getOrderId()).toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(orderTM.getCustomerId()).toLowerCase().contains(lowerCaseFilter);
            }

            // 2. Date Range Filter
            LocalDate startDate = dpStartDate.getValue();
            LocalDate endDate = dpEndDate.getValue();

            if (startDate != null || endDate != null) {
                // Convert java.util.Date (from DB) to LocalDate for comparison
                LocalDate orderDate = new java.sql.Date(orderTM.getOrderDate().getTime()).toLocalDate();

                if (startDate != null && endDate != null) {
                    matchesDateRange = !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                } else if (startDate != null) {
                    matchesDateRange = !orderDate.isBefore(startDate);
                } else { // endDate != null
                    matchesDateRange = !orderDate.isAfter(endDate);
                }
            }

            // Return true only if both conditions are met
            return matchesSearch && matchesDateRange;
        });
    }

    private void setupActionColumn() {
        Callback<TableColumn<OrderTM, Void>, TableCell<OrderTM, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<OrderTM, Void> call(final TableColumn<OrderTM, Void> param) {
                return new TableCell<>() {
                    private final Button printBtn = new Button("Print Invoice");

                    {
                        printBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

                        printBtn.setOnAction((ActionEvent event) -> {
                            OrderTM data = getTableView().getItems().get(getIndex());
                            try {
                                placeOrderBO.printOrderInvoice(data.getOrderId());
                            } catch (SQLException | JRException e) {
                                new Alert(Alert.AlertType.ERROR, "Failed to print invoice!").show();
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(printBtn);
                        }
                    }
                };
            }
        };

        colAction.setCellFactory(cellFactory);
    }

    @FXML
    public void refreshOnAction(ActionEvent actionEvent) {
        // Clear all filters
        txtSearch.clear();
        dpStartDate.setValue(null);
        dpEndDate.setValue(null);

        // Reload fresh data from the database
        loadAllOrders();
    }
}