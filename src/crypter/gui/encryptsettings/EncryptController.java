package crypter.gui.encryptsettings;

import crypter.crypt.helpers.CryptTypes;
import crypter.gui.elements.AlertFactory;
import crypter.gui.files.CryptController;
import static crypter.gui.files.CryptController.mainStageSetDisabled;
import crypter.gui.main.Controllers;
import crypter.gui.settings.Settings;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EncryptController implements Initializable {

    @FXML
    TextField passField1;
    @FXML
    ChoiceBox choiceCryptType;
    @FXML
    Button saveKeyBt;

    private CryptController mainController;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //инициализация выбора типа шифрования
        for (int i = 1; i < CryptTypes.values().length; i++) {
            this.choiceCryptType.getItems().add(CryptTypes.getName(CryptTypes.values()[i]));
        }
        choiceCryptType.getSelectionModel().selectFirst();
        mainController = Controllers.getCryptController();
        Platform.runLater(
                () -> {
                    stage = ((Stage) choiceCryptType.getScene().getWindow());

                }
        );
        passField1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() != 0) {
                    saveKeyBt.setDisable(false);
                } else {
                    saveKeyBt.setDisable(true);
                }
            }
        });
        choiceCryptType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() == 1) {
                    passField1.setDisable(true);
                } else {
                    passField1.setDisable(false);
                }
            }

        });
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
                mainController.setCryptData(this.getType(), null, this.getKey());
                mainController.execEncryptTask();
                closeStage();
            } else {
                //отмена действия777
            }
        } else {
            mainController.setCryptData(this.getType(), null, this.getKey());
            mainController.execEncryptTask();
            closeStage();
        }
    }

    @FXML
    private void saveKeyBtAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.key)", "*.key");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveTextToFile("key: " + getKey() + " "
                    + "type: " + getType().getName(), file);
        }
    }

    private void saveTextToFile(String content, File file) {
        try {
            FileWriter writer;
            writer = new FileWriter(file);
            writer.append(content);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(EncryptController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btCancelAction(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        ((Stage) passField1.getScene().getWindow()).close();
        mainStageSetDisabled(false);
    }

    public String getKey() {
        return this.passField1.getText();
    }

    public CryptTypes getType() {
        return CryptTypes.getCryptType(this.choiceCryptType.getSelectionModel().getSelectedItem().toString());
    }

    //not used (old)
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
