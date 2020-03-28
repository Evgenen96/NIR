package crypter.gui.elements;

import crypter.gui.files.helpers.FileItem;
import javafx.animation.FadeTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;

public class PopUpFiles extends Popup {

    private Node popupContent;
    private Label titleLabel;
    private Label detailsLabel;
    private FadeTransition fadeOut;

    public PopUpFiles(ListView node) {
        titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 1.2em ; -fx-font-weight: bold;");
        detailsLabel = new Label();
        popupContent = new VBox(20,titleLabel, detailsLabel);
        popupContent.setStyle("-fx-background-color: -fx-background; "
                + "-fx-background: lightskyblue");

        this.getContent().add(popupContent);
//        fadeOut = new FadeTransition(Duration.millis(500), popupContent);
//        fadeOut.setFromValue(1.0);
//        fadeOut.setToValue(0.0);
//        fadeOut.setOnFinished(e -> this.hide());
        node.setCellFactory(lv -> {
            ListCell<FileItem> cell = new ListCell<FileItem>() {
                @Override
                public void updateItem(FileItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        //setText(item.getName());
                        titleLabel.setText(item.getName());
                        detailsLabel.setText(item.getAbsPath().substring(0,item.getAbsPath().lastIndexOf('\\') + 1));
                        setGraphic(popupContent);
 
                    }
                }
            };
//            cell.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
//                if (isNowHovered && !cell.isEmpty()) {
//                    showPopup(cell);
//                } else {
//                    hidePopup();
//                }
//            });

            return cell;
        });

    }

    private void showPopup(ListCell<FileItem> cell) {
        fadeOut.stop();
        popupContent.setOpacity(1.0);
        Bounds bounds = cell.localToScreen(cell.getBoundsInLocal());
        this.show(cell, bounds.getMaxX() - 500, bounds.getMinY());
        FileItem item = cell.getItem();
        //titleLabel.setText(item.getName());
        detailsLabel.setText(item.getAbsPath() + item.getSpace());
    }

    private void hidePopup() {
        fadeOut.playFromStart();
    }

}
