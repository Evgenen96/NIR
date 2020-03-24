package crypter.gui.main;

import crypter.crypt.hashfucntion.HashFunction;
import crypter.crypt.helpers.MyByteArr;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class FXMain extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/crypter/gui/files/FXML.fxml"));
        primaryStage.setTitle("CRYPTER");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
