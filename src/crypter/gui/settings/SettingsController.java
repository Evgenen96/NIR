package crypter.gui.settings;

import com.jfoenix.controls.JFXToggleButton;
import static crypter.gui.files.CryptController.mainStageSetDisabled;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class SettingsController implements Initializable {

    @FXML
    JFXToggleButton delEncFileTB;
    @FXML
    JFXToggleButton delDecFileTB;
    @FXML
    JFXToggleButton showEncWarningTB;
    @FXML
    JFXToggleButton showDecWarningTB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        delDecFileTB.setSelected(Settings.isCLEAR_DEC_FILES_LIST_AFTER_WORK());
        delEncFileTB.setSelected(Settings.isCLEAR_ENC_FILES_LIST_AFTER_WORK());
        showEncWarningTB.setSelected(Settings.isSHOW_ENCRYPT_CONFIRMATION_DIALOG());
        showDecWarningTB.setSelected(Settings.isSHOW_DECRYPT_CONFIRMATION_DIALOG());

        delEncFileTB.selectedProperty().addListener(new ChangeListener< Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                Settings.setOption(Settings.CLEAR_ENC_FILES_LIST_AFTER_WORK(), arg2);
            }
        });
        delDecFileTB.selectedProperty().addListener(new ChangeListener< Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                Settings.setOption(Settings.CLEAR_DEC_FILES_LIST_AFTER_WORK(), arg2);
            }
        });
        showEncWarningTB.selectedProperty().addListener(new ChangeListener< Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                Settings.setOption(Settings.SHOW_ENCRYPT_CONFIRMATION_DIALOG(), arg2);
            }

        });
        showDecWarningTB.selectedProperty().addListener(new ChangeListener< Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                Settings.setOption(Settings.SHOW_DECRYPT_CONFIRMATION_DIALOG(), arg2);
            }

        });

    }

    private void closeStage() {
        ((Stage) delDecFileTB.getScene().getWindow()).close();
        mainStageSetDisabled(false);
    }

}
