package crypter.gui.encryptsettings;

import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.helpers.CryptedFile;
import crypter.crypt.helpers.States;
import crypter.gui.elements.AlertFactory;
import crypter.gui.files.CryptController;
import static crypter.gui.files.CryptController.mainStageSetDisabled;
import crypter.gui.files.helpers.FileItem;
import crypter.gui.settings.Settings;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EncryptController implements Initializable {

    @FXML
    TextField passField1;
    @FXML
    ChoiceBox choiceCryptType;
    @FXML
    ProgressBar progressBar;

    public EncryptTask task;
    private static ExecutorService executor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //инициализация выбора типа шифрования
        for (int i = 1; i < CryptTypes.values().length; i++) {
            this.choiceCryptType.getItems().add(CryptTypes.getName(CryptTypes.values()[i]));
        }
        choiceCryptType.getSelectionModel().selectFirst();
        executor = Executors.newFixedThreadPool(2, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(false);
                return t;
            }
        });
        task = new EncryptTask();

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
                executor.execute(task);
                closeStage();
                executor.shutdown();

            } else {
                //отмена действия
            }
        } else {
            executor.execute(task);
            closeStage();
            executor.shutdown();
        }
    }

    private void encryptFile() {
        int i = 0;
        for (String filePath : CryptController.getFilesToEncryptPath()) {
            FileItem file = (FileItem) CryptController.getFilesTab().getItems().get(i++);
            CryptedFile tempFile = CryptController.getCryptSystem().encryptFile(
                    CryptTypes.getCryptType(
                            this.choiceCryptType.getSelectionModel().getSelectedItem().toString()),
                    filePath, passField1.getText());
            file.setState(tempFile);
            CryptController.getFilesTab().refresh();
            //список зашифрованных
            if (file.getCryptInfo().getState() == States.SUCCESS_ENC) {
                CryptController.getFilesListL().getItems().add(0, file);
            }
        }
        closeStage();
    }

    @FXML
    private void btCancelAction(ActionEvent event) {
        closeStage();
      //  mainStageSetDisabled(false);
    }

    private void closeStage() {
        ((Stage) passField1.getScene().getWindow()).close();
       // mainStageSetDisabled(false);
    }

    void loadMain() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/crypter/gui/files/FXML.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("CRYPTOR");
            stage.setScene(new Scene(root));
            stage.show();
            //mainStageSetDisabled(false);
        } catch (IOException ex) {
        }
    }

    //задача шифрования
    class EncryptTask extends Task {

        @Override
        protected Integer call() throws Exception {
            for (Object item : CryptController.getFilesTab().getItems()) {
                FileItem file = (FileItem) item;
                file.setStatusImage();
                CryptController.getFilesTab().refresh();
                CryptedFile tempFile = CryptController.getCryptSystem().encryptFile(
                        CryptTypes.getCryptType(choiceCryptType.getSelectionModel().getSelectedItem().toString()),
                        file.getAbsPath(), passField1.getText());
                file.setState(tempFile);
                CryptController.getFilesTab().refresh();
            }
            return null;
        }

    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void stopTaks() {

    }

}
