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
import lk.ijse.athukorala_hardware.dto.DriverDTO;
import lk.ijse.athukorala_hardware.dto.tm.DriverTM;
import lk.ijse.athukorala_hardware.model.DriverModel;
import net.sf.jasperreports.engine.JRException;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DriverController implements Initializable {
    private final String DRIVER_ID_REGEX = "^[0-9]+$";
    private final String DRIVER_NAME_REGEX = "^[A-Za-z ]{2,50}$";
    private final String DRIVER_CONTACT_REGEX = "^07[0-9]{8}$";
    private final String DRIVER_ADDRESS_REGEX = "^[A-Za-z0-9 ,./-]{5,100}$";


    public TextField driverIdField;
    public TextField driverNameField;
    public TextField driverContactField;
    public TextField driverAddressField;

    @FXML
    public TableView<DriverTM> tblDrivers;
    @FXML
    public TableColumn<DriverTM, Integer> colId;
    @FXML
    public TableColumn<DriverTM, String> colName;
    @FXML
    public TableColumn<DriverTM, String> colContact;
    @FXML
    public TableColumn<DriverTM, String> colAddress;

    private final DriverModel driverModel = new DriverModel();
    private boolean isDriverExists = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadDriverTable();

        tblDrivers.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        setData(newVal);
                    }
                }
        );
    }

    private void setData(DriverTM tm) {
        driverIdField.setText(String.valueOf(tm.getId()));
        driverNameField.setText(tm.getName());
        driverContactField.setText(tm.getContact());
        driverAddressField.setText(tm.getAddress());

        isDriverExists = true;
    }


    public void SearchDriverOnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String id = driverIdField.getText().trim();

            if (!id.matches(DRIVER_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Driver ID").show();
                return;
            }

            try {
                DriverDTO dto = driverModel.searchDriver(id);

                if (dto != null) {
                    driverNameField.setText(dto.getName());
                    driverContactField.setText(dto.getContact());
                    driverAddressField.setText(dto.getAddress());

                    isDriverExists = true;

                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Driver Not Found").show();
                    clearOnAction(null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void driverSaveOnAction(ActionEvent actionEvent) {
        if (isDriverExists) {
            new Alert(Alert.AlertType.WARNING,
                    "This Driver already exists.\nUse UPDATE instead.")
                    .show();
            return;
        }

        String name = driverNameField.getText().trim();
        String contact = driverContactField.getText().trim();
        String address = driverAddressField.getText().trim();

        if (!name.matches(DRIVER_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name").show();
            return;
        }

        if (!contact.matches(DRIVER_CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Contact Number").show();
            return;
        }

        if (!address.matches(DRIVER_ADDRESS_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Address").show();
            return;
        }

        try {
            DriverDTO dto = new DriverDTO(name, contact, address);
            boolean isSaved = driverModel.saveDriver(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Driver Saved Successfully").show();
                clearOnAction(null);
                loadDriverTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void driverUpdateOnAction(ActionEvent actionEvent) {
        String id = driverIdField.getText().trim();
        String name = driverNameField.getText().trim();
        String contact = driverContactField.getText().trim();
        String address = driverAddressField.getText().trim();

        if (!id.matches(DRIVER_ID_REGEX) ||
                !name.matches(DRIVER_NAME_REGEX) ||
                !contact.matches(DRIVER_CONTACT_REGEX) ||
                !address.matches(DRIVER_ADDRESS_REGEX)) {

            new Alert(Alert.AlertType.ERROR, "Invalid Input").show();
            return;
        }

        try {
            DriverDTO dto = new DriverDTO(
                    Integer.parseInt(id),
                    name,
                    contact,
                    address
            );

            boolean isUpdated = driverModel.updateDriver(dto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Driver Updated Successfully").show();
                clearOnAction(null);
                loadDriverTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void driverDeleteOnAction(ActionEvent actionEvent) {
        String id = driverIdField.getText().trim();

        if (!id.matches(DRIVER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Driver ID").show();
            return;
        }

        try {
            boolean isDeleted = driverModel.deleteDriver(id);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Driver Deleted Successfully").show();
                clearOnAction(null);
                loadDriverTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearOnAction(ActionEvent actionEvent) {
        driverIdField.clear();
        driverNameField.clear();
        driverContactField.clear();
        driverAddressField.clear();
        tblDrivers.getSelectionModel().clearSelection();

        isDriverExists = false;
    }

    private void loadDriverTable() {
        try {
            List<DriverDTO> driverList = driverModel.getDrivers();
            ObservableList<DriverTM> obList = FXCollections.observableArrayList();

            for (DriverDTO dto : driverList) {
                obList.add(new DriverTM(
                        dto.getId(),
                        dto.getName(),
                        dto.getContact(),
                        dto.getAddress()
                ));
            }

            tblDrivers.setItems(obList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printDriverOnAction(ActionEvent actionEvent) {
        try {
            driverModel.printDriverReport();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
