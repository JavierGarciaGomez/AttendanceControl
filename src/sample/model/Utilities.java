package sample.model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Utilities {

    public boolean showAlert(Alert.AlertType alertType, String title, String contentText){
        boolean confirm = false;
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(contentText);

        if(alertType==Alert.AlertType.CONFIRMATION){
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                confirm=true;
            }
        } else{
            alert.showAndWait();
        }
        return confirm;
    }
}
