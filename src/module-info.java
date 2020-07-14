module AttendanceControl {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;


    opens sample;
    opens sample.model;
    opens sample.controller;

    exports sample;
}