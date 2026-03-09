package lk.ijse.athukorala_hardware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.athukorala_hardware.dto.SupplierDTO;
import lk.ijse.athukorala_hardware.dto.tm.SupplierTM;
import lk.ijse.athukorala_hardware.model.SupplierModel;
import net.sf.jasperreports.engine.JRException;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    private final String SUPPLIER_ID_REGEX = "^[0-9]+$";
    private final String NAME_REGEX = "^[A-Za-z ]{2,50}$";
    private final String CONTACT_REGEX = "^07[0-9]{8}$";
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private final String ADDRESS_REGEX = "^[A-Za-z0-9 ,./-]{5,100}$";

    @FXML
    public TextField idField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField contactField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField addressField;

    @FXML
    public TableView<SupplierTM> tblSuppliers;
    @FXML
    public TableColumn<SupplierTM, Integer> colId;
    @FXML
    public TableColumn<SupplierTM, String> colName;
    @FXML
    public TableColumn<SupplierTM, String> colContact;
    @FXML
    public TableColumn<SupplierTM, String> colEmail;
    @FXML
    public TableColumn<SupplierTM, String> colAddress;

    private final SupplierModel supplierModel = new SupplierModel();
    private boolean isSupplierExists = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadSupplierTable();

        tblSuppliers.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        setData(newVal);
                    }
                }
        );
    }

    private void setData(SupplierTM tm) {
        idField.setText(String.valueOf(tm.getId()));
        nameField.setText(tm.getName());
        contactField.setText(tm.getContact());
        emailField.setText(tm.getEmail());
        addressField.setText(tm.getAddress());

        isSupplierExists = true;
    }

    private void loadSupplierTable() {
        try {
            List<SupplierDTO> supplierList = supplierModel.getSuppliers();
            ObservableList<SupplierTM> obList = FXCollections.observableArrayList();

            for (SupplierDTO dto : supplierList) {
                obList.add(new SupplierTM(
                        dto.getId(),
                        dto.getName(),
                        dto.getContact(),
                        dto.getAddress(),
                        dto.getEmail()
                ));
            }
            tblSuppliers.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load suppliers: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    public void searchSupplierOnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String id = idField.getText().trim();

            if (!id.matches(SUPPLIER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID").show();
                return;
            }

            try {
                SupplierDTO dto = supplierModel.searchSupplier(id);

                if (dto != null) {
                    nameField.setText(dto.getName());
                    contactField.setText(dto.getContact());
                    emailField.setText(dto.getEmail());
                    addressField.setText(dto.getAddress());

                    isSupplierExists = true;
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Not Found").show();
                    clearFields(null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database Error").show();
            }
        }
    }

    public void saveSupplierOnAction(ActionEvent actionEvent) {
        if (isSupplierExists) {
            new Alert(Alert.AlertType.WARNING, "This Supplier already exists.\nUse UPDATE instead.").show();
            return;
        }

        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (validateFields(name, contact, email, address)) {
            try {
                // Constructor: name, contact, email, address
                SupplierDTO dto = new SupplierDTO(name, contact, email, address);

                boolean isSaved = supplierModel.saveSupplier(dto);

                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Saved Successfully").show();
                    clearFields(null);
                    loadSupplierTable();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error saving supplier: " + e.getMessage()).show();
            }
        }
    }

    public void updateSupplierOnAction(ActionEvent actionEvent) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (!id.matches(SUPPLIER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID").show();
            return;
        }

        if (validateFields(name, contact, email, address)) {
            try {
                // Constructor: id, name, contact, email, address
                SupplierDTO dto = new SupplierDTO(Integer.parseInt(id), name, contact, email, address);

                boolean isUpdated = supplierModel.updateSupplier(dto);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Updated Successfully").show();
                    clearFields(null);
                    loadSupplierTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Update Failed").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error updating supplier: " + e.getMessage()).show();
            }
        }
    }

    public void deleteSupplierOnAction(ActionEvent actionEvent) {
        String id = idField.getText().trim();

        if (!id.matches(SUPPLIER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID").show();
            return;
        }

        try {
            boolean isDeleted = supplierModel.deleteSupplier(id);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted Successfully").show();
                clearFields(null);
                loadSupplierTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Delete Failed (ID might not exist)").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error deleting supplier: " + e.getMessage()).show();
        }
    }

    public void clearFields(ActionEvent actionEvent) {
        idField.clear();
        nameField.clear();
        contactField.clear();
        emailField.clear();
        addressField.clear();
        tblSuppliers.getSelectionModel().clearSelection();

        isSupplierExists = false;
    }

    private boolean validateFields(String name, String contact, String email, String address) {
        if (!name.matches(NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name (2-50 characters)").show();
            return false;
        }
        if (!contact.matches(CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Contact Number (07xxxxxxxx)").show();
            return false;
        }
        if (!email.matches(EMAIL_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Email Address").show();
            return false;
        }
        if (!address.matches(ADDRESS_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Address").show();
            return false;
        }
        return true;
    }

    public void printSupplierOnAction(ActionEvent actionEvent) {
        try {
            supplierModel.printSupplierReport();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}