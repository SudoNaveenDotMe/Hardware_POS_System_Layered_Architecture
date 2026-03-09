module lk.ijse.athukorala_hardware.athukorala_hardware {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires net.sf.jasperreports.core;


    opens lk.ijse.athukorala_hardware to javafx.fxml;
    exports lk.ijse.athukorala_hardware;
    exports lk.ijse.athukorala_hardware.controller;
    opens lk.ijse.athukorala_hardware.controller to javafx.fxml;
    exports lk.ijse.athukorala_hardware.dto;
    opens lk.ijse.athukorala_hardware.dto to javafx.fxml;
    exports lk.ijse.athukorala_hardware.model;
    opens lk.ijse.athukorala_hardware.model to javafx.fxml;
    exports lk.ijse.athukorala_hardware.dto.tm;
}