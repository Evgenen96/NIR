package crypter.gui.elements;

import crypter.gui.files.helpers.FileItem;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class FileListCellFactory {

    public FileListCellFactory(ListView listView) {

        listView.setCellFactory(lv -> {
            ListCell<FileItem> cell = new ListCell<FileItem>() {
                @Override
                public void updateItem(FileItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                    } else {
                        String upp = item.getName();
                        String low = item.getAbsPath().substring(0, item.getAbsPath().lastIndexOf('\\') + 1);
                        setGraphic(new CellContent(upp, low).getPopupContent());
                    }
                }

            };
            return cell;
        });

    }
}

class CellContent {

    private Node popupContent;
    private Label upperRow;
    private Label lowerRow;

    public CellContent(String upp, String low) {
        upperRow = new Label(upp);
        lowerRow = new Label(low);
        upperRow.setStyle("-fx-font-size: 1.0em ; -fx-font-weight: bold;");
        lowerRow.setStyle("-fx-font-size: 0.8em; ");
        popupContent = new VBox(0, upperRow, lowerRow);
    }

    public Node getPopupContent() {
        return popupContent;
    }

}
