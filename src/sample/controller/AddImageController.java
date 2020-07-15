package sample.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddImageController implements Initializable {
    public ImageView imvImage;
    public Button saveImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
        dragEvent.consume();
    }

    public void handleOnDragDropped(DragEvent dragEvent) throws FileNotFoundException {
        List<File> files = dragEvent.getDragboard().getFiles();
        Image image = new Image(new FileInputStream(files.get(0)));
        imvImage.setImage(image);
        File copy = new File("res\\RRR.png");

        // LECTURA BINARIA
        List<Integer> bytes = new ArrayList<>();
        try {
            // Creo el FileInputStream
            FileInputStream fileInputStream = new FileInputStream(files.get(0));
            int stream;
            while ((stream = fileInputStream.read()) != -1) {
                bytes.add(stream);
            }
            System.out.println("Terminé el fileInputStream");
            fileInputStream.close();

            FileOutputStream fileOutputStream = new FileOutputStream(copy, true);
            for (Integer b : bytes) {
                fileOutputStream.write(b);
            }
            System.out.println("He terminado de hacer la copia");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
