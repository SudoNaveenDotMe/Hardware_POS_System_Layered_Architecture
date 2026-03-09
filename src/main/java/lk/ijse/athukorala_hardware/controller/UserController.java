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
import lk.ijse.athukorala_hardware.dto.UserDTO;
import lk.ijse.athukorala_hardware.dto.tm.UserTM;
import lk.ijse.athukorala_hardware.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    private final String NAME_REGEX = "^[A-Za-z ]{2,50}$";
    private final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final UserModel userModel = new UserModel();

    @FXML
    public AnchorPane userContent;
    @FXML
    public TextField idField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;
    @FXML
    public ComboBox<String> cmbRole;
    @FXML
    public Button btnSave;

    @FXML
    public TableView<UserTM> tblUser; 
    @FXML
    public TableColumn<UserTM, Integer> colId;
    @FXML
    public TableColumn<UserTM, String> colName;
    @FXML
    public TableColumn<UserTM, String> colEmail;
    @FXML
    public TableColumn<UserTM, String> colRole;
    public TableColumn<UserTM, String> colPassword;

    private boolean isUserExists = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> roles = FXCollections.observableArrayList("Admin", "Cashier");
        cmbRole.setItems(roles);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadUserTable();

        tblUser.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                setData(newVal);
            }
        });
    }

    private void setData(UserTM tm) {
        idField.setText(String.valueOf(tm.getId()));
        nameField.setText(tm.getName());
        emailField.setText(tm.getEmail());
        cmbRole.setValue(tm.getRole());
        passwordField.setText(tm.getPassword());

        isUserExists = true;
    }

    private void loadUserTable() {
        try {
            List<UserDTO> allUsers = userModel.getAllUsers();
            ObservableList<UserTM> obList = FXCollections.observableArrayList();

            for (UserDTO dto : allUsers) {
                obList.add(new UserTM(
                        dto.getUserId(),
                        dto.getName(),
                        dto.getEmail(),
                        dto.getRole(),
                        dto.getPassword()
                ));
            }
            tblUser.setItems(obList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load users").show();
            e.printStackTrace();
        }
    }

    @FXML
    public void searchUserOnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String id = idField.getText();

            try {
                UserDTO userDTO = userModel.searchUserById(id);

                if (userDTO != null) {
                    nameField.setText(userDTO.getName());
                    emailField.setText(userDTO.getEmail());
                    passwordField.setText(userDTO.getPassword());
                    cmbRole.setValue(userDTO.getRole());

                    isUserExists = true;
                    btnSave.setDisable(true);
                } else {
                    new Alert(Alert.AlertType.ERROR, "User Not Found").show();
                    clearFields(null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveUserOnAction(ActionEvent actionEvent) {
        if (!validateFields()) return;

        try {
            UserDTO userDTO = new UserDTO(
                    nameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    cmbRole.getValue()
            );

            boolean isSaved = userModel.saveUser(userDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "User Saved Successfully").show();
                clearFields(null);
                loadUserTable();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error Saving User: " + e.getMessage()).show();
        }
    }

    @FXML
    public void updateUserOnAction(ActionEvent actionEvent) {
        if (!isUserExists) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to update").show();
            return;
        }
        if (!validateFields()) return;

        try {
            UserDTO userDTO = new UserDTO(
                    Integer.parseInt(idField.getText()),
                    nameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    cmbRole.getValue()
            );

            boolean isUpdated = userModel.updateUser(userDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "User Updated Successfully").show();
                clearFields(null);
                loadUserTable();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error Updating User").show();
        }
    }

    @FXML
    public void deleteUserOnAction(ActionEvent actionEvent) {
        if (!isUserExists) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete").show();
            return;
        }

        try {
            boolean isDeleted = userModel.deleteUser(idField.getText());
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "User Deleted Successfully").show();
                clearFields(null);
                loadUserTable();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error Deleting User").show();
        }
    }

    @FXML
    public void clearFields(ActionEvent actionEvent) {
        idField.clear();
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        cmbRole.getSelectionModel().clearSelection();

        isUserExists = false;
        btnSave.setDisable(false);
        tblUser.getSelectionModel().clearSelection();
    }

    @FXML
    public void printUserOnAction(ActionEvent actionEvent) {
        new Alert(Alert.AlertType.INFORMATION, "Report feature pending...").show();
    }

    private boolean validateFields() {
        if (nameField.getText().isEmpty() || !nameField.getText().matches(NAME_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Name").show();
            return false;
        }
        if (emailField.getText().isEmpty() || !emailField.getText().matches(EMAIL_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Email").show();
            return false;
        }
        if (passwordField.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Password cannot be empty").show();
            return false;
        }
        if (cmbRole.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a role").show();
            return false;
        }
        return true;
    }
}