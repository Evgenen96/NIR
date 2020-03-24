package crypter.gui.files;

import crypter.crypt.Crypt;
import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.helpers.States;
import crypter.gui.elements.AlertStage;
import crypter.gui.encryptsettings.EncryptSettingsFXMLController;
import static crypter.gui.files.CryptFXMLController.tabButtons;
import crypter.gui.files.helpers.MyFile;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import crypter.gui.files.helpers.FilePathTreeItem;
import crypter.gui.helpers.StageLoader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class CryptFXMLController {

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
    @FXML
    private ChoiceBox choiceCryptType;

    private static Crypt cryptSystem;
    private static CryptTypes type;
    private static String[] filePaths;
    private static TableView filesTab;
    public static ArrayList<Button> tabButtons;

    @FXML
    void initialize() {
        cryptSystem = new Crypt();
        tabButtons = new ArrayList<>();
        //инициализация дерева
        refreshFileBrowser();

        //инициализация таблицы
        this.tableSelectedFiles.setPlaceholder(new Label("Список пуст"));
        this.colFileName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        this.colFileType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        this.colFileSpace.setCellValueFactory(new PropertyValueFactory<>("Space"));
        this.colFileStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        filesTab = this.tableSelectedFiles;

        //инициализация выбора типа шифрования
        for (int i = 1; i < CryptTypes.values().length; i++) {
            this.choiceCryptType.getItems().add(CryptTypes.getName(CryptTypes.values()[i]));
        }
        choiceCryptType.getSelectionModel().selectFirst();
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
                    file.setLog(States.NORMAL, null);
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
        type = CryptTypes.getCryptType(this.choiceCryptType.getSelectionModel().getSelectedItem().toString());

        StageLoader.loadWindow(getClass().getResource("/crypter/gui/encryptsettings/EncryptSettings.fxml"), type.getName(), null);
    }

    @FXML
    public void btDecryptFileAction(ActionEvent actionEvent) {
        decryptFile();
    }

    private void decryptFile() {
        if (this.tableSelectedFiles.getItems().isEmpty()) {
            return;
        }
        type = CryptTypes.getCryptType(this.choiceCryptType.getSelectionModel().getSelectedItem().toString());
        int i = 0;
        filePaths = new String[this.tableSelectedFiles.getItems().size()];
        for (MyFile file : this.tableSelectedFiles.getItems()) {
            filePaths[i++] = file.getAbsPath();
        }
        StageLoader.loadWindow(getClass().getResource("/crypter/gui/decryptsettings/decryptFXML.fxml"), type.getName(), null);
    }

    @FXML
    private void btRefreshTreeAction() {
//        System.out.println(this.treeviewFileBrowse.getRoot());
//        TreeView fake = new TreeView();
//        Callback sel = this.treeviewFileBrowse.getCellFactory();
//        refreshFileBrowser();
//        this.treeviewFileBrowse.setsetCellFactory(sel);
        //this.tableSelectedFiles.refresh();
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

    public static CryptTypes getType() {
        return type;
    }

    public static String[] getFileToEncryptPath() {
        return filePaths;
    }

    public static TableView getFilesTab() {
        return filesTab;
    }

    //создание кнопок в таблице выбранных файлов
    public static Button getButton(States log, MyFile file) {
        Button button = new Button("");
        Tooltip tooltip = new Tooltip(log.getDescription());
        tooltip.setStyle("-fx-font-size: 12");
        //чтобы тултип вслывал мнгновенно
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D p = new Point2D(event.getSceneX() + 300, event.getSceneY() + 50);
                tooltip.show(button, p.getX(), p.getY());
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                tooltip.hide();
            }
        });
        switch (log) {
            case NO_FILE: {
                button.setGraphic(new ImageView(new Image("/src/res/no-file.png", 16, 16, true, true)));
                break;
            }
            case NO_MARK: {
                button.setGraphic(new ImageView(new Image("/res/no-mark.png", 16, 16, true, true)));
                break;
            }
            case SUCCESS_DEC: {
                button.setGraphic(new ImageView(new Image("/res/success.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    Optional<ButtonType> action = AlertStage.showOkCanсel("Подтверждение", "Проверьте файл", "Все ок", "Отмена");
                    if (action.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                        //777действия если нужно отменить шифрование
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
                    Optional<ButtonType> action = AlertStage.showOkCanсel("Неверный ключ", "Файл был зашифрован другим ключом. \n"
                            + "Хотите расшифровать с помощью вашего ключа?", "Да", "Нет");
                    if (action.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                        //777действия если нужно все таки расшифровать
                    } else {
                        //777 другой ключ??
                    }
                });
                break;
            }
            case NORMAL: {
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
