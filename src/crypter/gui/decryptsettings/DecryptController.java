package crypter.gui.decryptsettings;

import crypter.crypt.helpers.CryptTypes;
import crypter.gui.files.CryptController;
import static crypter.gui.files.CryptController.mainStageSetDisabled;
import crypter.gui.main.Controllers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DecryptController implements Initializable {

    @FXML
    TextField passField1;
    @FXML
    ChoiceBox choiceCryptType;
    @FXML
    Button btExtractKey;

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
        mainController.setCryptData(this.getType(), null, this.getKey());
        mainController.execDecryptTask();
        closeStage();
    }

    @FXML
    private void btExtractKeyAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.key)", "*.key");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            readTextFromFile(file);
        }
    }

    private String readTextFromFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            line = reader.readLine();

            int typeIndex = line.lastIndexOf("type:");
            String key = line.substring(5, typeIndex);
            passField1.setText(key);
            
            String type = line.substring(typeIndex + 6, line.length());
            int i = CryptTypes.getCryptType(type).ordinal() - 1;
            choiceCryptType.getSelectionModel().select(i);
            
            reader.close();
            return line;
        } catch (FileNotFoundException ex) {
            System.out.println("Файл не найден");
        } catch (IOException ex) {
            System.out.println("Ошибка чтения");
        }
        return null;
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
