package lk.ijse.athukorala_hardware.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lk.ijse.athukorala_hardware.App;
import lk.ijse.athukorala_hardware.db.DBConnection;
import lk.ijse.athukorala_hardware.dto.UserDTO;
import lk.ijse.athukorala_hardware.model.UserModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private final UserModel userModel = new UserModel();

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField userEmailField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        String email = userEmailField.getText().toLowerCase();
        String password = passwordField.getText().trim();

        try {
            DBConnection.getInstance().getConnection();
            UserDTO userDTO = userModel.searchUser(email);

            if (userDTO != null) {
                if (userDTO.getPassword().equals(password)) {
                    if (userDTO.getRole().equals("Admin"))
                        App.setRoot("AdminDashboard",1500,950);
                    else {
                        App.setRoot("CashierDashboard",1500,950);
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Wrong Password !!").show();
                }

            } else {
                new Alert(Alert.AlertType.ERROR, String.format("User Not Found (%s)", email)).show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void resetOnAction(ActionEvent actionEvent) {
        userEmailField.clear();
        passwordField.clear();
    }

    @FXML
    public void forgotPasswordOnAction(ActionEvent actionEvent) {

    }
}
