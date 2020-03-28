package crypter.gui.files.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FilePathTreeItem extends TreeItem<String> {

    public static Image folderCollapseImage = new Image(ClassLoader.getSystemResourceAsStream("res/folder.png"));
    public static Image folderExpandImage = new Image(ClassLoader.getSystemResourceAsStream("res/folder-open.png"));
    public static Image fileImage = new Image(ClassLoader.getSystemResourceAsStream("res/text-x-generic.png"));

    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private File file;

    private static ArrayList<FilePathTreeItem> foldersList = new ArrayList<>();

    public File getFile() {
        return (this.file);
    }
    private String absolutePath;

    public String getAbsolutePath() {
        return (this.absolutePath);
    }
    private final boolean isDirectory;

    public boolean isDirectory() {
        return (this.isDirectory);
    }

    public FilePathTreeItem(File file) {
        super(file.toString());
        this.file = file;
        this.absolutePath = file.getAbsolutePath();
        this.isDirectory = file.isDirectory();

        if (this.isDirectory) {
            this.setGraphic(new ImageView(folderCollapseImage));
            //add event handlers
            this.addEventHandler(TreeItem.branchCollapsedEvent(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    FilePathTreeItem source = (FilePathTreeItem) e.getSource();
                    if (!source.isExpanded()) {
                        foldersList.remove(source);
                        ImageView iv = (ImageView) source.getGraphic();
                        iv.setImage(folderCollapseImage);
                    }
                }
            });
            this.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    FilePathTreeItem source = (FilePathTreeItem) e.getSource();
                    if (source.isExpanded()) {
                        if (!foldersList.contains(source)) {
                            foldersList.add(source);
                        }
                        ImageView iv = (ImageView) source.getGraphic();
                        iv.setImage(folderExpandImage);
                    }
                }
            });
        } else {
            this.setGraphic(new ImageView(fileImage));
        }

        //set the value (which is what is displayed in the tree)
        String fullPath = file.getAbsolutePath();
        if (!fullPath.endsWith(File.separator)) {
            String value = file.toString();
            int indexOf = value.lastIndexOf(File.separator);
            if (indexOf > 0) {
                this.setValue(value.substring(indexOf + 1));
            } else {
                this.setValue(value);
            }
        }
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;
            super.getChildren().setAll(buildChildren(this));
        }
        return (super.getChildren());
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            isLeaf = this.file.isFile();
        }
        return (isLeaf);
    }

    private ObservableList<FilePathTreeItem> buildChildren(FilePathTreeItem treeItem) {
        File f = treeItem.getFile();
        if ((f != null) && (f.isDirectory())) {
            File[] files = f.listFiles();
            if (files != null) {
                ObservableList<FilePathTreeItem> children = FXCollections.observableArrayList();
                for (File childFile : files) {
                    FilePathTreeItem childItem = new FilePathTreeItem(childFile);
                    if (foldersList.contains(childItem)) {
                        childItem.setExpanded(true);
                    }
                    children.add(childItem);
                }
                return (children);
            }
        }
        return FXCollections.emptyObservableList();
    }

    public static ArrayList<FilePathTreeItem> getExpandedFolders() {
        return foldersList;
    }

    public void refreshFolderList() {
        foldersList.clear();
    }
    
    public static boolean isFolderExpanded(String folder) {
        FilePathTreeItem temp = new FilePathTreeItem(new File(folder)) ;
        if (foldersList.contains(temp)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FilePathTreeItem other = (FilePathTreeItem) obj;
        if (!Objects.equals(this.absolutePath, other.absolutePath)) {
            return false;
        }
        return true;
    }

}
