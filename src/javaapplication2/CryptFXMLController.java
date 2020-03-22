package javaapplication2;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CryptFXMLController {

    @FXML
    private TreeView<String> treeviewFileBrowse;
    @FXML
    private VBox vBox1;
    @FXML
    void initialize() {
        vBox1.setVgrow(treeviewFileBrowse,Priority.ALWAYS);
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
}
