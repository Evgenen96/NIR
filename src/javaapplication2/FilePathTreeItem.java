
package javaapplication2;

import java.io.File;
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
                    children.add(new FilePathTreeItem(childFile));
                }
                return (children);
            }
        }
        return FXCollections.emptyObservableList();
    }

}
