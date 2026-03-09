package lk.ijse.athukorala_hardware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.athukorala_hardware.bo.BOFactory;
import lk.ijse.athukorala_hardware.bo.custom.CustomerBO;
import lk.ijse.athukorala_hardware.dto.CustomerDTO;
import lk.ijse.athukorala_hardware.dto.tm.CustomerTM;
import net.sf.jasperreports.engine.JRException;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    private final String CUSTOMER_ID_REGEX = "^[0-9]+$";
    private final String CUSTOMER_NAME_REGEX = "^[A-Za-z ]{2,50}$";
    private final String CUSTOMER_CONTACT_REGEX = "^07[0-9]{8}$";
    private final String CUSTOMER_ADDRESS_REGEX = "^[A-Za-z0-9]{5,}$";
    private final String CUSTOMER_EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

//    private final CustomerModel customerModel = new CustomerModel();
    CustomerBO customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOType.CUSTOMER);
    private boolean isCustomerExsist = false;


    @FXML
    public TextField idField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField contactField;
    @FXML
    public TextField addressField;

    @FXML
    public TableView<CustomerTM> tblCustomer;
    @FXML
    public TableColumn<CustomerTM,Integer> colId;
    @FXML
    public TableColumn<CustomerTM,String> colName;
    @FXML
    public TableColumn<CustomerTM,String> colContact;
    @FXML
    public TableColumn<CustomerTM,String> colEmail;
    @FXML
    public TableColumn<CustomerTM,String> colAddress;
    @FXML
    public AnchorPane customerContent;
    @FXML
    public Button btnSave;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadCustomerTableOnAction();
        tblCustomer.getSelectionModel().
                selectedItemProperty().
                addListener((observableValue, oldValue, newValue) -> {
                    if (null != newValue) {
                        setData(newValue);
                    }
                });

    }

    private void setData(CustomerTM tm) {
        idField.setText(String.valueOf(tm.getId()));
        nameField.setText(tm.getName());
        contactField.setText(tm.getContact());
        addressField.setText(tm.getAddress());
        emailField.setText(tm.getEmail());

        isCustomerExsist = true;
    }

    @FXML
    public void searchCustomerOnAction(KeyEvent keyEvent) {
        try {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String id = idField.getText();

                if (!id.matches(CUSTOMER_ID_REGEX)) {
                    new Alert(Alert.AlertType.ERROR, "Invalid ID").show();
                } else {
                    CustomerDTO customerDTO = customerBO.searchCustomer(id);

                    if (customerDTO != null) {
                        nameField.setText(customerDTO.getName());
                        emailField.setText(customerDTO.getEmail());
                        contactField.setText(customerDTO.getContact());
                        addressField.setText(customerDTO.getAddress());

                        isCustomerExsist = true;

                    } else {
                        new Alert(Alert.AlertType.ERROR, "Customer Not Found").show();
                        clearFields();
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void saveCustomerOnAction(ActionEvent actionEvent) {

        if (isCustomerExsist){
            new Alert(Alert.AlertType.WARNING,
                    "This customer already exists.\nUse UPDATE instead.")
                    .show();
            return;
        }

        if (!validateAllFields()) return;

        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim().toLowerCase();

        if (!name.matches(CUSTOMER_NAME_REGEX) || !contact.matches(CUSTOMER_CONTACT_REGEX) || !address.matches(CUSTOMER_ADDRESS_REGEX) || !email.matches(CUSTOMER_EMAIL_REGEX)) {
            new Alert(Alert.AlertType.INFORMATION, "Invalid Input").show();
        } else {
            try {
                CustomerDTO customerDTO = new CustomerDTO(name, contact, address, email);
                boolean result = customerBO.saveCustomer(customerDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully").show();
                    clearFields();
                    loadCustomerTableOnAction();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer Save Failed").show();
                }


            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @FXML
    public void updateCustomerOnAction(ActionEvent actionEvent) {
        if (!idField.getText().matches(CUSTOMER_ID_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Customer ID").show();
            return;
        }

        if (!validateAllFields()) return;

        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();
            String email = emailField.getText().trim().toLowerCase();

            if (!id.matches(CUSTOMER_ID_REGEX) || !name.matches(CUSTOMER_NAME_REGEX) || !contact.matches(CUSTOMER_CONTACT_REGEX) || !address.matches(CUSTOMER_ADDRESS_REGEX) || !email.matches(CUSTOMER_EMAIL_REGEX)) {
                new Alert(Alert.AlertType.INFORMATION, "Invalid Input").show();
            } else {
                CustomerDTO customerDTO = new CustomerDTO(Integer.parseInt(id), name, contact, address, email);
                boolean result = customerBO.updateCustomer(customerDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Updated Successfully").show();
                    clearFields();
                    loadCustomerTableOnAction();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer Update Failed").show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong").show();
        }
    }

    @FXML
    public void deleteCustomerOnAction(ActionEvent actionEvent) {
        try {
            String id = idField.getText().trim();

            if (!id.matches(CUSTOMER_ID_REGEX)) {
                new Alert(Alert.AlertType.INFORMATION, "Invalid Input").show();
            } else {
                boolean result = customerBO.deleteCustomer(id);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully").show();
                    clearFields();
                    loadCustomerTableOnAction();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Something Went Wrong").show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong").show();
        }
    }


    @FXML
    private void loadCustomerTableOnAction() {
        try {
            List<CustomerDTO> customerList = customerBO.getCustomers();
            ObservableList<CustomerTM> obList = FXCollections.observableArrayList();

            for (CustomerDTO dto : customerList) {
                CustomerTM tm = new CustomerTM(
                        dto.getId(),
                        dto.getName(),
                        dto.getContact(),
                        dto.getAddress(),
                        dto.getEmail()
                );
                obList.add(tm);
            }
            tblCustomer.setItems(obList);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void clearFields() {
        idField.clear();
        nameField.clear();
        contactField.clear();
        addressField.clear();
        emailField.clear();

        isCustomerExsist = false;
        btnSave.setDisable(false);
        tblCustomer.getSelectionModel().clearSelection();
    }

    @FXML
    public void printCustomerOnAction(ActionEvent actionEvent) {
        try {
            customerBO.printCustomerReport();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    //Validation Part
    private boolean validateField(TextField field, String regex) {
        boolean isValid = field.getText().trim().matches(regex);

        if (isValid) {
            field.setStyle("-fx-border-color: #2ecc71; -fx-border-width: 1.5;");
        } else {
            field.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 1.5;");
        }
        return isValid;
    }

    @FXML
    public void nameKeyReleased(KeyEvent event) {
        validateField(nameField, CUSTOMER_NAME_REGEX);
    }

    @FXML
    public void contactKeyReleased(KeyEvent event) {
        validateField(contactField, CUSTOMER_CONTACT_REGEX);
    }

    @FXML
    public void addressKeyReleased(KeyEvent event) {
        validateField(addressField, CUSTOMER_ADDRESS_REGEX);
    }

    @FXML
    public void emailKeyReleased(KeyEvent event) {
        validateField(emailField, CUSTOMER_EMAIL_REGEX);
    }

    private boolean validateAllFields() {
        boolean nameValid = validateField(nameField, CUSTOMER_NAME_REGEX);
        boolean contactValid = validateField(contactField, CUSTOMER_CONTACT_REGEX);
        boolean addressValid = validateField(addressField, CUSTOMER_ADDRESS_REGEX);
        boolean emailValid = validateField(emailField, CUSTOMER_EMAIL_REGEX);

        if (!(nameValid && contactValid && addressValid && emailValid)) {
            new Alert(Alert.AlertType.WARNING,
                    "Please correct highlighted fields").show();
            return false;
        }
        return true;
    }
}
