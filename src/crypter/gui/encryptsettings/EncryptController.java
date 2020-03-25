package crypter.gui.encryptsettings;

import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.helpers.CryptedFile;
import crypter.gui.elements.AlertFactory;
import crypter.gui.files.CryptController;
import static crypter.gui.files.CryptController.mainStageSetDisabled;
import crypter.gui.files.helpers.MyFile;
import crypter.gui.settings.Settings;
import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EncryptController implements Initializable {

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
        if (Settings.isSHOW_ENCRYPT_CONFIRMATION_DIALOG()) {
            Optional<ButtonType> action = AlertFactory.showOkCancelWithOpt(
                    AlertType.WARNING, "Подтверждение",
                    "Внимание! Если вы забудете\n"
                    + "или потеряете ключ шифрования,\n"
                    + "то данные в файле будут потеряны!",
                    "Зашифровать", "Отменить", 2);
            if (action.get().getButtonData() == ButtonData.OK_DONE) {
                encryptFile();
            } else {
                //отмена действия
            }
        } else {
            encryptFile();
        }
    }

    private void encryptFile() {
        int i = 0;
        for (String filePath : CryptController.getFileToEncryptPath()) {
            MyFile file = (MyFile) CryptController.getFilesTab().getItems().get(i++);
            CryptedFile tempFile = CryptController.getCryptSystem().encryptFile(
                    CryptTypes.getCryptType(
                            this.choiceCryptType.getSelectionModel().getSelectedItem().toString()),
                    filePath, passField1.getText());
            file.setState(tempFile);
            CryptController.getFilesTab().refresh();
        }
        closeStage();
    }

    @FXML
    private void btCancelAction(ActionEvent event) {
        closeStage();
        mainStageSetDisabled(false);
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
