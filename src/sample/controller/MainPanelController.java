package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPanelController {
    public Button btnRegister;
    public Button btnIncidences;
    public Button btnModify;
    public Button btnReview;
    public Button btnReports;
    public Button btnChangeUser;
    public Button btnManageUsers;

    public void registerTime(ActionEvent event) {
        openScene("RegisterWindow.fxml", "Register window");
    }

    public void manageUsers(ActionEvent event) {
        openScene("ManageUserWindow.fxml", "Manage Users");
    }

    public void openScene(String fxmlName, String title){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}
