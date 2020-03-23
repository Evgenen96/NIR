package crypter.gui.files;

import crypter.crypt.Crypt;
import crypter.crypt.helpers.CryptTypes;
import crypter.gui.encryptsettings.EncryptSettingsFXMLController;
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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CryptFXMLController {

    @FXML
    private TreeView<String> treeviewFileBrowse;
    @FXML
    private TableView<MyFile> tableSelectedFiles;
    @FXML
    private TableColumn colFileName;
    @FXML
    private TableColumn colFileType;
    @FXML
    private TableColumn colFileSpace;
    @FXML
    private TableColumn colFileStatus;
    @FXML
    private TextField textSelFileInfo;
    @FXML
    private TextField textFileInfo;
    @FXML
    private ChoiceBox choiceCryptType;
    @FXML
    private VBox vBox1;

    private static Crypt cryptSystem;
    private static CryptTypes type;
    private static String[] filePaths;
    private static TableView filesTab;

    @FXML
    void initialize() {
        cryptSystem = new Crypt();

        //инициализация дерева
        vBox1.setVgrow(treeviewFileBrowse, Priority.ALWAYS);
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

        //инициализация таблицы
        this.tableSelectedFiles.setPlaceholder(new Label("Список пуст"));
        this.colFileName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        this.colFileType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        this.colFileSpace.setCellValueFactory(new PropertyValueFactory<>("Space"));
        this.colFileStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
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
    
    

}
