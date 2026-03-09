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
import lk.ijse.athukorala_hardware.dto.ItemDTO;
import lk.ijse.athukorala_hardware.dto.tm.ItemTM;
import lk.ijse.athukorala_hardware.model.ItemModel;
import net.sf.jasperreports.engine.JRException;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    private final String ITEM_ID_REGEX = "^[0-9]+$";
    private final String ITEM_NAME_REGEX = "^[A-Za-z0-9 ]{2,50}$";
    private final String ITEM_PRICE_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";
    private final String ITEM_QTY_REGEX = "^[0-9]+$";

    private final ItemModel itemModel = new ItemModel();
    public TextField itemCodeField;
    public TextField itemNameField;
    public TextField itemPriceField;
    public TextField itemQtyField;
    public TableView<ItemTM> tblItems;
    public TableColumn<ItemTM, Integer> colId;
    public TableColumn<ItemTM, String> colName;
    public TableColumn<ItemTM, Double> colPrice;
    public TableColumn<ItemTM, Integer> colQty;
    private boolean isNewRecord = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Item Controller");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        loadItemTable();

        tblItems.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        setData(newVal);
                    }
                }
        );
    }

    private void setData(ItemTM tm) {
        itemCodeField.setText(String.valueOf(tm.getId()));
        itemNameField.setText(tm.getName());
        itemPriceField.setText(String.valueOf(tm.getPrice()));
        itemQtyField.setText(String.valueOf(tm.getQty()));

        isNewRecord = false;
    }

    @FXML
    public void SearchItemOnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String id = itemCodeField.getText().trim();

            if (!id.matches(ITEM_ID_REGEX)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Item ID").show();
                return;
            }
            try {
                ItemDTO dto = itemModel.searchItem(id);

                if (dto != null) {
                    itemNameField.setText(dto.getName());
                    itemPriceField.setText(String.valueOf(dto.getPrice()));
                    itemQtyField.setText(String.valueOf(dto.getQty()));
                } else {
                    new Alert(Alert.AlertType.WARNING, "Item Not Found").show();
                    clearOnAction();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void itemSaveOnAction() {
        if (!isNewRecord) {
            new Alert(Alert.AlertType.WARNING,
                    "This item already exists. Use Update instead.").show();
            return;
        }

        if (!validateFields()) return;

        try {
            ItemDTO dto = new ItemDTO(
                    itemNameField.getText().trim(),
                    Double.parseDouble(itemPriceField.getText().trim()),
                    Integer.parseInt(itemQtyField.getText().trim())
            );

            boolean isSaved = itemModel.saveItem(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Item Saved Successfully").show();
                clearOnAction();
                loadItemTable();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void itemUpdateOnAction() {
        String id = itemCodeField.getText().trim();

        if (!id.matches(ITEM_ID_REGEX) || !validateFields()) {
            new Alert(Alert.AlertType.WARNING, "Invalid Input").show();
            return;
        }

        try {
            ItemDTO dto = new ItemDTO(
                    Integer.parseInt(id),
                    itemNameField.getText().trim(),
                    Double.parseDouble(itemPriceField.getText().trim()),
                    Integer.parseInt(itemQtyField.getText().trim())
            );

            boolean isUpdated = itemModel.updateItem(dto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Item Updated Successfully").show();
                clearOnAction();
                loadItemTable();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void itemDeleteOnAction() {
        String id = itemCodeField.getText().trim();

        if (!id.matches(ITEM_ID_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Item ID").show();
            return;
        }

        try {
            boolean isDeleted = itemModel.deleteItem(id);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Item Deleted Successfully").show();
                clearOnAction();
                loadItemTable();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadItemTable() {
        try {
            List<ItemDTO> itemList = itemModel.getItem();
            ObservableList<ItemTM> obList = FXCollections.observableArrayList();

            for (ItemDTO dto : itemList) {
                obList.add(new ItemTM(
                        dto.getId(),
                        dto.getName(),
                        dto.getPrice(),
                        dto.getQty()
                ));
            }
            tblItems.setItems(obList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void clearOnAction() {
        itemCodeField.clear();
        itemNameField.clear();
        itemPriceField.clear();
        itemQtyField.clear();
        tblItems.getSelectionModel().clearSelection();

        isNewRecord = true;
    }

    /* ================= VALIDATION ================= */
    private boolean validateFields() {
        if (!itemNameField.getText().matches(ITEM_NAME_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Item Name").show();
            return false;
        }
        if (!itemPriceField.getText().matches(ITEM_PRICE_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Price").show();
            return false;
        }
        if (!itemQtyField.getText().matches(ITEM_QTY_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Quantity").show();
            return false;
        }
        return true;
    }

    public void printItemOnAction(ActionEvent actionEvent) {
        try {
            itemModel.printItemReport();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
