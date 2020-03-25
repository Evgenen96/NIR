package crypter.gui.files;

import crypter.crypt.Crypt;
import crypter.crypt.helpers.CryptedFile;
import crypter.crypt.helpers.States;
import crypter.gui.elements.AlertFactory;
import crypter.gui.files.helpers.MyFile;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import crypter.gui.files.helpers.FilePathTreeItem;
import crypter.gui.helpers.StageLoader;
import crypter.gui.settings.Settings;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class CryptController {

    @FXML
    private TreeView<String> treeviewFileBrowse;
    @FXML
    private TableView<MyFile> tableSelectedFiles;
    @FXML
    private TableColumn<MyFile, String> colFileName;
    @FXML
    private TableColumn<MyFile, String> colFileType;
    @FXML
    private TableColumn<MyFile, String> colFileSpace;
    @FXML
    private TableColumn<MyFile, Button> colFileStatus;
    @FXML
    private TextField textSelFileInfo;
    @FXML
    private TextField textFileInfo;

    private static Crypt cryptSystem;
    private static String[] filePaths;
    private static TableView filesTab;

    @FXML
    void initialize() {
        cryptSystem = new Crypt();

        //инициализация дерева
        refreshFileBrowser();

        //инициализация таблицы
        this.tableSelectedFiles.setPlaceholder(new Label("Список пуст"));
        this.colFileName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        this.colFileType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        this.colFileSpace.setCellValueFactory(new PropertyValueFactory<>("Space"));
        this.colFileStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        filesTab = this.tableSelectedFiles;

        treeviewFileBrowse.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                FilePathTreeItem item = (FilePathTreeItem) this.treeviewFileBrowse.getSelectionModel().getSelectedItem();
                if (!item.isDirectory()) {
                    MyFile file = new MyFile(item.getFile());
                    file.setState(new CryptedFile(null, null, null, States.DEFAULT));
                    if (!this.tableSelectedFiles.getItems().contains(file)) {
                        this.tableSelectedFiles.getItems().add(file);
                    }
                }
            }
        });

        MyFile.setCrypt(this);

    }

    @FXML
    public void btAddFileAction(ActionEvent actionEvent) {
        addSelected();
    }

    private void addSelected() {
        ObservableList<Integer> indices = this.treeviewFileBrowse.getSelectionModel().getSelectedIndices();
        for (int i = 0; i < indices.size(); i++) {
            int index = indices.get(i);
            if (index > 0) {
                FilePathTreeItem item = (FilePathTreeItem) this.treeviewFileBrowse.getTreeItem(index);
                if (!item.isDirectory()) {
                    MyFile file = new MyFile(item.getFile());
                    file.setState(new CryptedFile(null, null, null, States.DEFAULT));
                    if (!this.tableSelectedFiles.getItems().contains(file)) {
                        this.tableSelectedFiles.getItems().add(file);
                    }
                }
            }
        }
    }

    @FXML
    public void btRemoveSelectedAction(ActionEvent actionEvent) {
        removeSelected();
    }

    private void removeSelected() {
        this.tableSelectedFiles.getItems().removeAll(this.tableSelectedFiles.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void btRemoveAllAction(ActionEvent actionEvent) {
        removeAll();
    }

    private void removeAll() {
        this.tableSelectedFiles.getItems().clear();
    }

    @FXML
    public void btEncryptFileAction(ActionEvent actionEvent) {
        encryptFile();
    }

    private void encryptFile() {
        if (this.tableSelectedFiles.getItems().isEmpty()) {
            return;
        }
        int i = 0;
        filePaths = new String[this.tableSelectedFiles.getItems().size()];
        for (MyFile file : this.tableSelectedFiles.getItems()) {
            filePaths[i++] = file.getAbsPath();
        }
        this.tableSelectedFiles.getParent().getParent().getParent().getParent().setDisable(true);
        mainStageSetDisabled(true);
        StageLoader.loadWindow(getClass().getResource("/crypter/gui/encryptsettings/Encrypt.fxml"), "Шифрование", null);
    }

    @FXML
    public void btDecryptFileAction(ActionEvent actionEvent) {
        decryptFile();
    }

    private void decryptFile() {
        if (this.tableSelectedFiles.getItems().isEmpty()) {
            return;
        }
        int i = 0;
        filePaths = new String[this.tableSelectedFiles.getItems().size()];
        for (MyFile file : this.tableSelectedFiles.getItems()) {
            filePaths[i++] = file.getAbsPath();
        }
        mainStageSetDisabled(true);
        StageLoader.loadWindow(getClass().getResource("/crypter/gui/decryptsettings/Decrypt.fxml"), "Расшифровка", null);
    }

    @FXML
    private void btRefreshTreeAction() {

        refreshFileBrowser();

    }

    private void refreshFileBrowser() {

        String hostName = "computer";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException x) {
        }
        TreeItem<String> rootNode = new TreeItem<>(hostName, new ImageView(new Image(ClassLoader.getSystemResourceAsStream("res/computer.png"))));
        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path name : rootDirectories) {
            FilePathTreeItem treeNode = new FilePathTreeItem(new File(name.toString()));
            rootNode.getChildren().add(treeNode);
        }
        rootNode.setExpanded(true);
        this.treeviewFileBrowse.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.treeviewFileBrowse.setRoot(rootNode);

    }

    public static Crypt getCryptSystem() {
        return cryptSystem;
    }

    public static String[] getFileToEncryptPath() {
        return filePaths;
    }

    public static TableView getFilesTab() {
        return filesTab;
    }

    public static void mainStageSetDisabled(boolean disable) {
        filesTab.getParent().getParent().getParent().getParent().setDisable(disable);
    }

    //создание кнопок в таблице выбранных файлов
    public Button getButton(MyFile file) {
        Button button = new Button("");
        Tooltip tooltip = new Tooltip(file.getCryptInfo().getState().getDescription());
        tooltip.setStyle("-fx-font-size: 12");
        //чтобы тултип вслывал мнгновенно
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D p = new Point2D(event.getSceneX() + 250, event.getSceneY() + 80);
                tooltip.show(button, p.getX(), p.getY());
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tooltip.hide();
            }
        });
        switch (file.getCryptInfo().getState()) {
            case NO_FILE: {
                button.setGraphic(new ImageView(new Image("/res/no-file.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    mainStageSetDisabled(true);
                    StageLoader.loadWindow(getClass().getResource("/crypter/gui/encryptsettings/Encrypt.fxml"), file.getCryptInfo().getCrypt().getName(), null);
                });
                break;
            }
            case NO_MARK: {
                button.setGraphic(new ImageView(new Image("/res/no-mark.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    filesTab.getItems().remove(file);
                    filesTab.refresh();
                });
                //777пока без марки не дешифруем
                break;
            }
            case SUCCESS_DEC: {
                button.setGraphic(new ImageView(new Image("/res/success.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    if (Settings.isSHOW_ENCRYPT_CONFIRMATION_DIALOG()) {
                        Optional<ButtonType> action = AlertFactory.showOkCancelWithOpt(
                                AlertType.WARNING,
                                "Подтверждение", "Проверьте файл!", "Все ок!", "Отмена расшифровки",
                                Settings.SHOW_DECRYPT_CONFIRMATION_DIALOG());
                        if (action.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                            filesTab.getItems().remove(file);
                            filesTab.refresh();
                        } else {
                            //шифруем обратно, если накосячили
                            CryptedFile tempFile = cryptSystem.encryptFile(file.getCryptInfo().getCrypt(), file.getAbsPath(), file.getCryptInfo().getKey());
                            file.setState(new CryptedFile(tempFile.getFile(), tempFile.getKey(), tempFile.getCrypt(), States.DEFAULT));
                            filesTab.refresh();
                        }
                    } else {
                        filesTab.getItems().remove(file);
                        filesTab.refresh();
                    }
                });
                break;
            }
            case SUCCESS_ENC: {
                button.setGraphic(new ImageView(new Image("/res/success.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    filesTab.getItems().remove(file);
                    filesTab.refresh();
                });
                break;
            }
            case WRONG_KEY: {
                button.setGraphic(new ImageView(new Image("/res/wrong-key.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    Optional<ButtonType> action = AlertFactory.showOkCanсel("Неверный ключ", "Файл был зашифрован другим ключом. \n"
                            + "Хотите расшифровать с помощью вашего ключа?", "Да", "Нет");
                    if (action.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                        CryptedFile tempFile = cryptSystem.decryptFile(file.getCryptInfo().getCrypt(), file.getAbsPath(), file.getCryptInfo().getKey(), true);
                        file.setState(tempFile);
                        filesTab.refresh();
                    } else {
                        mainStageSetDisabled(true);
                        StageLoader.loadWindow(getClass().getResource("/crypter/gui/decryptsettings/Decrypt.fxml"), file.getCryptInfo().getCrypt().getName(), null);
                    }
                });
                break;
            }
            case DEFAULT: {
                button.setGraphic(new ImageView(new Image("/res/remove-file.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    filesTab.getItems().remove(file);
                    filesTab.refresh();
                });
                break;
            }
        }
        return button;
    }
}
