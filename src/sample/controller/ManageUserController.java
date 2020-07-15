package sample.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class ManageUserController implements Initializable {
    public Button btnRegister;
    public Button btnCancel;
    public TextField txtId;
    public PasswordField txtPass;
    public TextField txtUser;
    public TextField txtlastName;
    public TextField txtName;
    public Button btnAddPicture;
    public CheckBox chkActive;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.txtId.setText(String.valueOf(new User().getMaxID()+1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.chkActive.setSelected(true);
    }

    public void register(ActionEvent event) {
        boolean isValid=true;

        int id= Integer.parseInt(txtId.getText());
        String name = txtName.getText();
        String lastName = txtlastName.getText();
        String userName = txtUser.getText().toUpperCase();
        String pass = txtPass.getText();
        boolean isActive = chkActive.isSelected();
        String errorList ="No se ha podido registrar el usuario, porque se encontraron los siguientes errores:\n";

        User user = new User(id, name,lastName,userName,pass, isActive);

        try {

            if(!user.checkAvailableId()){
                errorList+="Id ya registrado\n";
                isValid=false;
            }
            if(name.length()<=3){
                errorList+="El nombre no puede tener menos de tres caracteres\n";
                isValid=false;
            }
            if(lastName.length()<=3){
                errorList+="El apellido no puede tener menos de tres caracteres\n";
                isValid=false;
            }
            if(userName.length()!=3){
                errorList+="El usuario se debe conformar por tres caracteres\n";
                isValid=false;
            }
            if(!user.checkAvailableUser()){
                errorList+="Usuario ya registrado\n";
                isValid=false;
            }
            if(pass.length()<4 || pass.length()>11){
                errorList+="El password debe tener entre 4 y 10 caracteres\n";
                isValid=false;
            }
            if (isValid){
                user.addUser();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText(errorList);
                alert.showAndWait();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void checkDigits(TextField textField, String regex){
        TextFormatter<String> formatter = new TextFormatter<String>(change -> {
            change.setText(change.getText().replaceAll(regex, ""));
            return change;
        });
        textField.setTextFormatter(formatter);
    }

    public void validateNumbers(KeyEvent keyEvent) {
        DecimalFormat format = new DecimalFormat( "#.0" );
        txtId.setTextFormatter( new TextFormatter<>( c ->
        {
            System.out.println(c.getText());
            if ( c.getControlNewText().isEmpty() )
            {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( c.getControlNewText(), parsePosition );

            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
            {
                return null;
            }
            else
            {
                return c;
            }
        }));
    }

    public void generateUserName(){
        try{
            String name = txtName.getText();
            String lastName = txtlastName.getText();
            String fullName = name+" "+lastName;
            String [] nameElements = fullName.split("\\s+");
            String userName;

            char firstChar = 0;
            char secondChar= 0;
            char thirdChar = 0;

            firstChar=nameElements[0].charAt(0);
            secondChar=nameElements[nameElements.length-2].charAt(0);
            thirdChar=nameElements[nameElements.length-1].charAt(0);
            userName=Character.toString(firstChar)+Character.toString(secondChar)+Character.toString(thirdChar);
            System.out.println(fullName);
            System.out.println(userName);
            txtUser.setText(userName);

        } catch(ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e){

        }
    }

    public void addPicture(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddImageView.fxml"));

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
        stage.showAndWait();

    }
}
