package crypter.gui.decryptsettings;

import crypter.gui.encryptsettings.*;
import crypter.crypt.helpers.CryptTypes;
import crypter.gui.alert.AlertStage;
import crypter.gui.files.CryptFXMLController;
import crypter.gui.files.helpers.MyFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DecryptSettingsFXMLController implements Initializable {

    @FXML
    TextField passField1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void btContinueAction(ActionEvent event) {
        int i = 0;
        CryptTypes type = CryptFXMLController.getType();
        for (String filePath : CryptFXMLController.getFileToEncryptPath()) {
            MyFile file = (MyFile) CryptFXMLController.getFilesTab().getItems().get(i++);
            if (CryptFXMLController.getCryptSystem().decryptFile(type, filePath, passField1.getText())) {
                file.setStatus("dec");
                CryptFXMLController.getFilesTab().refresh();
            } else {
                file.setStatus("err");
                CryptFXMLController.getFilesTab().refresh();
            }
        }
        closeStage();

    }

    @FXML
    private void btCancelAction(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        ((Stage) passField1.getScene().getWindow()).close();
    }

    void loadMain() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/crypter/gui/files/FXML.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("CRYPTOR");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
        }
    }

}
