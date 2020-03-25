package crypter.gui.decryptsettings;

import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.helpers.CryptedFile;
import crypter.gui.files.CryptController;
import static crypter.gui.files.CryptController.mainStageSetDisabled;
import crypter.gui.files.helpers.MyFile;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DecryptController implements Initializable {

    @FXML
    TextField passField1;
    @FXML
    ChoiceBox choiceCryptType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //инициализация выбора типа шифрования
        for (int i = 1; i < CryptTypes.values().length; i++) {
            this.choiceCryptType.getItems().add(CryptTypes.getName(CryptTypes.values()[i]));
        }
        choiceCryptType.getSelectionModel().selectFirst();
    }

    @FXML
    private void btContinueAction(ActionEvent event) {
        int i = 0;
        for (String filePath : CryptController.getFileToEncryptPath()) {
            MyFile file = (MyFile) CryptController.getFilesTab().getItems().get(i++);
            CryptedFile tempFile = CryptController.getCryptSystem().decryptFile(
                    CryptTypes.getCryptType(
                            choiceCryptType.getSelectionModel().getSelectedItem().toString()),
                    filePath, passField1.getText());
            file.setState(tempFile);
            CryptController.getFilesTab().refresh();
        }
        closeStage();
    }

    @FXML
    private void btCancelAction(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        ((Stage) passField1.getScene().getWindow()).close();
        mainStageSetDisabled(false);
    }

    void loadMain() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/crypter/gui/files/FXML.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("CRYPTOR");
            stage.setScene(new Scene(root));
            stage.show();
            mainStageSetDisabled(false);
        } catch (IOException ex) {
        }
    }

}
