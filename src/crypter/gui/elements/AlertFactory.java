package crypter.gui.elements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import crypter.gui.settings.Settings;
import java.awt.SystemTray;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AlertFactory {

    public static Optional<ButtonType> showOkCanсel(String title, String content,
            String btOkName, String btCancelName) {
        ButtonType btOK = new ButtonType(btOkName, ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancel = new ButtonType(btCancelName, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.CONFIRMATION, content, btOK, btCancel);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        styleAlert(alert);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showYesNo(String title, String content) {
        ButtonType btNo = new ButtonType("Да", ButtonBar.ButtonData.YES);
        ButtonType btYes = new ButtonType("Нет", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(AlertType.CONFIRMATION, content, btNo, btYes);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        styleAlert(alert);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showOkCancelWithOpt(AlertType type,
            String title, String content,
            String btOkName, String btCancelName, int option) {
        ButtonType btOK = new ButtonType(btOkName, ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancel = new ButtonType(btCancelName, ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(type);
        alert.getDialogPane().applyCss();
        Node graphic = alert.getDialogPane().getGraphic();
        
        alert.setDialogPane(new DialogPane() {
            @Override
            protected Node createDetailsButton() {
                CheckBox optOut = new CheckBox();
                optOut.setText("Не показывать это окно снова");
                optOut.setOnAction(e -> {
                    Settings.setOption(option, !optOut.isSelected());
                });
                return optOut;
            }
        });
        alert.getDialogPane().getButtonTypes().add(btOK);
        alert.getDialogPane().getButtonTypes().add(btCancel);
        alert.getDialogPane().setContentText(content);

        alert.getDialogPane().setExpandableContent(new Group());
        alert.getDialogPane().setExpanded(true);

        alert.getDialogPane().setGraphic(graphic);
        alert.setTitle(title);
        return alert.showAndWait();
    }

    public static void showErrorMessage(Exception ex, String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error occured");
        alert.setHeaderText(title);
        alert.setContentText(content);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public static void showMaterialDialog(StackPane root, Node nodeToBeBlurred, List<JFXButton> controls, String header, String body) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        if (controls.isEmpty()) {
            controls.add(new JFXButton("Okay"));
        }
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().add("dialog-button");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                dialog.close();
            });
        });

        dialogLayout.setHeading(new Label(header));
        dialogLayout.setBody(new Label(body));
        dialogLayout.setActions(controls);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            nodeToBeBlurred.setEffect(null);
        });
        nodeToBeBlurred.setEffect(blur);
    }

    public static void showTrayMessage(String title, String message) {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            //BufferedImage image = ImageIO.read(AlertStage.class.getResource(LibraryAssistantUtil.ICON_IMAGE_LOC));
            // TrayIcon trayIcon = new TrayIcon(image, "Library Assistant");
            // trayIcon.setImageAutoSize(true);
            // trayIcon.setToolTip("Library Assistant");
            //tray.add(trayIcon);
            // trayIcon.displayMessage(title, message, MessageType.INFO);
            // tray.remove(trayIcon);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private static void styleAlert(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        DialogPane dialogPane = alert.getDialogPane();
       
    }
}
