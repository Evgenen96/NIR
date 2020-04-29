package crypter.gui.elements;

import crypter.gui.files.helpers.FileItem;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
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
                        String date = item.getCryptedFile().getDate();
                        CellContent cellContent = new CellContent(upp, low, date);
                        setGraphic(cellContent);
                    }
                }
            };
            return cell;
        });
    }
}

class CellContent extends Group {

    public CellContent(String upp, String low, String date) {
        Label upperRow = new Label(upp);
        Label lowerRow = new Label(low);
        Label dateLabel = new Label("Зашифрован " + date);
        upperRow.setStyle("-fx-font-size: 1.0em ; -fx-font-weight: bold;");
        lowerRow.setStyle("-fx-font-size: 0.8em; ");
        dateLabel.setStyle("-fx-font-size: 1.0em ; -fx-font-weight: bold;");
        VBox leftBox = new VBox(0, upperRow, lowerRow);
        HBox box = new HBox(5, leftBox, dateLabel);
        box.setAlignment(Pos.CENTER);
        this.getChildren().add(box);
    }
}
