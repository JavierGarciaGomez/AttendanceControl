package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Button btnRegister;
    public Button btnIncidences;
    public Button btnModify;
    public Button btnReview;
    public Button btnReports;
    public Button btnChangeUser;
    public Button btnManageUsers;
    public Label lblName;
    public ImageView imgPicture;
    private User user;

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
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initData(User user)  {
        this.user=user;
        lblName.setText(user.getName()+" "+user.getLastName());
        setPicture();

    }

    private void setPicture() {
        String userName = user.getUser();
        try{
            File file = new File("res\\"+userName+".png");
            if (!file.exists()) {
                file = new File("res\\"+"Morgana"+".png");
            }
            Image image = new Image(new FileInputStream(file));
            imgPicture.setImage(image);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
