package crypter.gui.files;

import crypter.crypt.CryptFactory;
import crypter.crypt.helpers.CryptCredentionals;
import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.helpers.CryptedFile;
import crypter.crypt.helpers.States;
import crypter.gui.decryptsettings.DecryptController;
import crypter.gui.elements.AlertFactory;
import crypter.gui.elements.FileListCellFactory;
import crypter.gui.encryptsettings.EncryptController;
import crypter.gui.files.helpers.FileItem;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import crypter.gui.files.helpers.FilePathTreeItem;
import crypter.gui.helpers.StageLoader;
import crypter.gui.main.Controllers;
import crypter.gui.settings.Settings;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CryptController {

    @FXML
    private BorderPane MainPain;
    @FXML
    private TreeView<String> treeviewFileBrowse;
    @FXML
    private TableView<FileItem> tableSelectedFiles;
    @FXML
    private TableColumn<FileItem, String> colFileName;
    @FXML
    private TableColumn<FileItem, String> colFileType;
    @FXML
    private TableColumn<FileItem, String> colFileSpace;
    @FXML
    private TableColumn<FileItem, Button> colFileStatus;
    @FXML
    private TableColumn<FileItem, Button> colRemoveFile;
    @FXML
    private ListView fileHistory;
    @FXML
    private TextField textSelFileInfo;
    @FXML
    private TextField textFileInfo;

    private static CryptFactory cryptSystemL;
    private static String[] filesAPathL;
    private static TableView filesTableL;
    private static ListView filesListL;
    private FileListCellFactory listCellFactory;
    private ArrayList<CryptedFile> saveHistoryList;

    private static ExecutorService executor;
    private CryptCredentionals cryptData;

    @FXML
    void initialize() {

        //инициализация системы шифров
        cryptSystemL = new CryptFactory();

        //инициализация дерева
        refreshFileBrowser();
        //инициализация таблицы
        this.tableSelectedFiles.setPlaceholder(new Label("Список пуст"));
        this.colFileName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        this.colFileType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        this.colFileSpace.setCellValueFactory(new PropertyValueFactory<>("Space"));
        this.colFileStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.colRemoveFile.setCellValueFactory(new PropertyValueFactory<>("relove"));
        filesTableL = this.tableSelectedFiles;

        //добавление дабл кликом
        treeviewFileBrowse.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (!this.treeviewFileBrowse.getSelectionModel().getSelectedItem().getValue().equals("EUGENE")) {
                    FilePathTreeItem item = (FilePathTreeItem) this.treeviewFileBrowse.getSelectionModel().getSelectedItem();
                    if (item != null) {
                        if (!item.isDirectory()) {
                            FileItem file = new FileItem(new CryptedFile(item.getFile()));
                            if (!this.tableSelectedFiles.getItems().contains(file)) {
                                file.setState(new CryptedFile(null, null, null, States.DEFAULT, null));
                                this.tableSelectedFiles.getItems().add(file);
                            }
                        }
                    }
                }
            }
        });

        //добавление дабл кликом
        fileHistory.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                FileItem item = (FileItem) this.fileHistory.getSelectionModel().getSelectedItem();
                if (item != null) {
                    if (!this.tableSelectedFiles.getItems().contains(item)) {
                        item.setState(new CryptedFile(null, null, null, States.DEFAULT,null));
                        this.tableSelectedFiles.getItems().add(item);
                    }
                }
            }
        });

        //передается ссылка на контроллер
        FileItem.setCrypt(this);

        //инициализация списка зашифрованных файлов
        listCellFactory = new FileListCellFactory(fileHistory);
        saveHistoryList = new ArrayList<>();
        readFileHistory();
        if (saveHistoryList != null && !saveHistoryList.isEmpty()) {
            for (CryptedFile cryptedFile : saveHistoryList) {
                FileItem fileItem = new FileItem(cryptedFile);
                fileHistory.getItems().add(fileItem);
            }
        }

        //инициализация пула задач ш/д
        executor = Executors.newFixedThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(false);
                return t;
            }
        });

        cryptData = new CryptCredentionals(CryptTypes.SIMPLE, "asd", null);

        //при выключении
        Platform.runLater(
                () -> {
                    Stage stage = ((Stage) tableSelectedFiles.getScene().getWindow());
                    stage.setOnCloseRequest(event -> {
                        saveFileHistory();
                        executor.shutdown();
                    });
                }
        );

    }

    @FXML
    public void btAddFileAction(ActionEvent actionEvent) {
        addSelected();
        for (FilePathTreeItem expandedFolder : FilePathTreeItem.getExpandedFolders()) {
            System.out.println(expandedFolder.getAbsolutePath());
        }
    }

    private void addSelected() {
        ObservableList<Integer> indices = this.treeviewFileBrowse.getSelectionModel().getSelectedIndices();
        for (int i = 0; i < indices.size(); i++) {
            int index = indices.get(i);
            if (index > 0) {
                FilePathTreeItem item = (FilePathTreeItem) this.treeviewFileBrowse.getTreeItem(index);
                if (!item.isDirectory()) {
                    FileItem file = new FileItem(new CryptedFile(item.getFile()));
                    file.setState(new CryptedFile(null, null, null, States.DEFAULT,null));
                    if (!this.tableSelectedFiles.getItems().contains(file)) {
                        this.tableSelectedFiles.getItems().add(file);
                    }
                }
            }
        }
    }

    @FXML
    public void btRemoveAllAction(ActionEvent actionEvent) {
        removeAll();
    }

    private void removeAll() {
        this.tableSelectedFiles.getItems().clear();
    }

    private void removeItem(FileItem item) {
        this.tableSelectedFiles.getItems().remove(item);
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
        filesAPathL = new String[this.tableSelectedFiles.getItems().size()];
        for (FileItem file : this.tableSelectedFiles.getItems()) {
            filesAPathL[i++] = file.getAbsPath();
        }
        this.tableSelectedFiles.getParent().getParent().getParent().getParent().setDisable(true);
        mainStageSetDisabled(true);
        Controllers.setEncryptController((EncryptController) StageLoader.loadWindow(
                getClass().getResource("/crypter/gui/encryptsettings/Encrypt.fxml"),
                "Шифрование", null)
        );
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
        filesAPathL = new String[this.tableSelectedFiles.getItems().size()];
        for (FileItem file : this.tableSelectedFiles.getItems()) {
            filesAPathL[i++] = file.getAbsPath();
        }
        mainStageSetDisabled(true);
        Controllers.setDecryptController((DecryptController) StageLoader.loadWindow(
                getClass().getResource("/crypter/gui/decryptsettings/Decrypt.fxml"),
                "Расшифровка", null)
        );
    }

    @FXML
    public void btRefreshTreeAction() {
        refreshFileBrowser();
    }

    //обновить дерево файлов
    private void refreshFileBrowser() {
        String hostName = "computer";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException x) {
        }
        TreeItem<String> rootNode = new TreeItem<>(hostName, new ImageView(new Image(ClassLoader.getSystemResourceAsStream("res/computer.png"))));
        rootNode.setExpanded(true);

        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path name : rootDirectories) {
            FilePathTreeItem treeNode = new FilePathTreeItem(new File(name.toString()));
            rootNode.getChildren().add(treeNode);
            if (FilePathTreeItem.isFolderExpanded(name.toString())) {
                treeNode.setExpanded(true);
            }
        }

        this.treeviewFileBrowse.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.treeviewFileBrowse.setRoot(rootNode);

        //this.treeviewFileBrowse.getRoot().getChildren().get(0).setExpanded(true);
    }

    @FXML
    public void btSettingsAction() {
        mainStageSetDisabled(true);
        StageLoader.loadWindow(
                getClass().getResource("/crypter/gui/settings/settings.fxml"),
                "Настройки", null);

    }

    //блокирование главного окна
    public static void mainStageSetDisabled(boolean disable) {
        filesTableL.getParent().getParent().getParent().getParent().setDisable(disable);
    }

    //создание кнопок в таблице выбранных файлов
    public Button getTableButton(FileItem item) {
        Button button = new Button("");
        Tooltip tooltip = new Tooltip(item.getCryptedFile().getState().getDescription());
        tooltip.setStyle("-fx-font-size: 12");
        //чтобы тултип вслывал мгновенно
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D p = new Point2D(event.getSceneX() + 260, event.getSceneY() + 70);
                tooltip.show(button, p.getX(), p.getY());
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tooltip.hide();
            }
        });
        switch (item.getCryptedFile().getState()) {
            case NO_FILE: {
                button.setBackground(Background.EMPTY);
                button.setGraphic(new ImageView(new Image("/res/no-file.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    mainStageSetDisabled(true);
                    Controllers.setEncryptController((EncryptController) StageLoader.loadWindow(
                            getClass().getResource("/crypter/gui/encryptsettings/Encrypt.fxml"),
                            item.getCryptedFile().getCrypt().getName(), null)
                    );
                });
                break;
            }
            case NO_MARK: {
                button.setBackground(Background.EMPTY);
                button.setGraphic(new ImageView(new Image("/res/no-mark.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    removeItem(item);
                });
                //777пока без марки не дешифруем
                break;
            }
            case SUCCESS_DEC: {
                button.setBackground(Background.EMPTY);
                button.setGraphic(new ImageView(new Image("/res/success.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    if (Settings.isSHOW_ENCRYPT_CONFIRMATION_DIALOG()) {
                        Optional<ButtonType> action = AlertFactory.showOkCancelWithOpt(
                                AlertType.WARNING,
                                "Подтверждение", "Проверьте файл!", "Все ок!", "Отмена расшифровки",
                                Settings.SHOW_DECRYPT_CONFIRMATION_DIALOG());
                        if (action.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                            removeItem(item);
                        } else {
                            //шифруем обратно, если накосячили
                            CryptedFile tempFile = cryptSystemL.encryptFile(item.getCryptedFile().getCrypt(), item.getAbsPath(), item.getCryptedFile().getKey());
                            item.setState(new CryptedFile(tempFile.getFile(), tempFile.getKey(), tempFile.getCrypt(), States.DEFAULT, tempFile.getDate()));
                            filesTableL.refresh();
                        }
                    } else {
                        removeItem(item);
                    }
                });
                break;
            }
            case SUCCESS_ENC: {
                button.setBackground(Background.EMPTY);
                button.setGraphic(new ImageView(new Image("/res/success.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    removeItem(item);
                });
                break;
            }
            case WRONG_KEY: {
                button.setBackground(Background.EMPTY);
                button.setGraphic(new ImageView(new Image("/res/wrong-key.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    Optional<ButtonType> action = AlertFactory.showOkCanсel("Неверный ключ", "Файл был зашифрован другим ключом. \n"
                            + "Хотите расшифровать с помощью вашего ключа?", "Да", "Нет");
                    if (action.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                        CryptedFile tempFile = cryptSystemL.decryptFile(item.getCryptedFile().getCrypt(), item.getAbsPath(), item.getCryptedFile().getKey(), true);
                        item.setState(tempFile);
                        filesTableL.refresh();
                    } else {
                        mainStageSetDisabled(true);
                        Controllers.setDecryptController((DecryptController) StageLoader.loadWindow(
                                getClass().getResource("/crypter/gui/decryptsettings/Decrypt.fxml"),
                                item.getCryptedFile().getCrypt().getName(), null)
                        );
                    }
                });
                break;
            }
            case DEFAULT: {
                button.setBackground(Background.EMPTY);
                button.setGraphic(new ImageView(new Image("/res/remove-file.png", 16, 16, true, true)));
                button.setOnAction((ActionEvent event) -> {
                    removeItem(item);
                });
                break;
            }
        }
        return button;
    }

    public static CryptFactory getCryptSystem() {
        return cryptSystemL;
    }

    public static String[] getFilesToEncryptPath() {
        return filesAPathL;
    }

    public static TableView getFilesTab() {
        return filesTableL;
    }


    public CryptCredentionals getCryptData() {
        return cryptData;
    }

    public void setCryptData(CryptTypes type, String filePath, String key) {
        cryptData = new CryptCredentionals(type, filePath, key);
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void setExecutor(ExecutorService executor) {
        CryptController.executor = executor;
    }

    public void execEncryptTask() {
        ObservableList<FileItem> list = this.fileHistory.getItems();
        for (Object item : getFilesTab().getItems()) {
            FileItem fileItem = (FileItem) item;
            EncryptTask encryptTask = new EncryptTask(fileItem);
            executor.execute(encryptTask);
            encryptTask.setOnSucceeded(e -> {
                if (!fileHistory.getItems().contains(fileItem)) {
                    if (fileItem.getCryptedFile().getState() == States.SUCCESS_ENC) {
                        fileHistory.getItems().add(0, fileItem);
                        saveHistoryList.add(fileItem.getCryptedFile());
                    }
                }
            });
        }
        refreshFileBrowser();
    }

    public void execDecryptTask() {
        for (Object item : getFilesTab().getItems()) {
            FileItem fileItem = (FileItem) item;
            DecryptTask decryptTask = new DecryptTask(fileItem);
            executor.execute(decryptTask);
            decryptTask.setOnSucceeded(e -> {
                if (fileHistory.getItems().contains(fileItem)) {
                    if (fileItem.getCryptedFile().getState() == States.SUCCESS_DEC) {
                        int index = fileHistory.getItems().indexOf(fileItem);
                        fileHistory.getItems().remove(index);
                    }
                }
            });
        }
        refreshFileBrowser();
    }

    //задача шифрования
    class EncryptTask extends Task {

        public EncryptTask(FileItem file) {
            this.file = file;
        }
        FileItem file;

        @Override
        protected Integer call() throws Exception {

            System.out.println(file.getAbsPath() + " шифруется");
            file.setStatusImage();
            CryptController.getFilesTab().refresh();
            CryptedFile tempFile = CryptController.getCryptSystem().encryptFile(
                    cryptData.getCryptType(),
                    file.getAbsPath(),
                    cryptData.getKey()
            );
            file.setState(tempFile);
            CryptController.getFilesTab().refresh();
            return null;
        }
    }

    class DecryptTask extends Task {

        public DecryptTask(FileItem file) {
            this.file = file;
        }
        FileItem file;

        @Override
        protected Integer call() throws Exception {
            System.out.println(file.getAbsPath() + " расшифровывется");
            file.setStatusImage();
            CryptController.getFilesTab().refresh();
            CryptedFile tempFile = CryptController.getCryptSystem().decryptFile(
                    cryptData.getCryptType(),
                    file.getAbsPath(),
                    cryptData.getKey()
            );
            file.setState(tempFile);
            CryptController.getFilesTab().refresh();
            return null;
        }
    }

    public void saveFileHistory() {
        try {
            FileOutputStream fileOut = new FileOutputStream("src/res/file-list.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(saveHistoryList);
            out.close();
            fileOut.close();
            System.out.println("\nСписок зашифрованных файлов сохранен\n");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileListCellFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileListCellFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readFileHistory() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("src/res/file-list.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            saveHistoryList = (ArrayList) objectInputStream.readObject();
            fileInputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileListCellFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileListCellFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
