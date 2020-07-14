package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField txtUser;
    public PasswordField txtPass;
    public Button btnLogin;
    public Button btnCancel;

    public void login(ActionEvent event) throws SQLException {
        String userName=this.txtUser.getText();
        String pass=this.txtPass.getText();

        User user = new User(userName, pass);
        if(user.checkLogin()){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPanel.fxml"));
                System.out.println(fxmlLoader);
                Parent root1 = fxmlLoader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root1);
                stage.setScene(scene);
                stage.setTitle("Main Window");
                Stage thisStage = (Stage) btnCancel.getScene().getWindow();
                thisStage.close();
            } catch (IOException e) {
                System.out.println("***********************NOT FOUNDED IO");
                e.printStackTrace();
            }

        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("NON-EXISTENT USER");
            alert.setContentText("Usuario no registrado. Intenta nuevamente.");
            alert.showAndWait();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
