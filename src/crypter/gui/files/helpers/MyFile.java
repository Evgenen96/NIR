package crypter.gui.files.helpers;

import crypter.crypt.helpers.States;
import crypter.gui.elements.ButtonMaker;
import crypter.gui.files.CryptFXMLController;
import java.io.File;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class MyFile {

    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleStringProperty space;
    private States state;
    private String absPath;
    private Button status;

    public MyFile(File file) {
        String extension;
        String fileName = file.getName();
        if (file.getName().contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
            fileName = fileName.substring(0, fileName.length() - extension.length() - 1);
        } else {
            extension = "";
        }
        name = new SimpleStringProperty(fileName);
        type = new SimpleStringProperty(extension);

        //подсчет размера
        double fileSpace = file.length();
        double div;
        int count = 0;
        String size = "";
        div = fileSpace / 10;
        while (div > 1024) {
            div = fileSpace / 1024;
            count++;
            fileSpace = div;
        }
        switch (count) {
            case 0: {
                size = " Б";
                break;
            }
            case 1: {
                size = " КБ";
                break;
            }
            case 2: {
                size = " МБ";
                break;
            }
            case 3: {
                size = " ГБ";
                break;
            }
        }
        space = new SimpleStringProperty(String.format("%.2f", fileSpace) + size);
        state = States.NORMAL;
 
        absPath = file.getAbsolutePath();
    }

    public MyFile(String absPath) {

    }

    public String getName() {
        return name.get();
    }

    public String getType() {
        return type.get();
    }

    public String getSpace() {
        return space.get();
    }

    public States getLog() {
        return state;
    }

    public String getAbsPath() {
        return absPath;
    }

    public Button getStatus() {
        return status;
    }

    public void setStatus(Button status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public void setLog(States log, File file) {
        this.state = log;
        
        status = CryptFXMLController.getButton(log, this);
//        switch (log) {
//            case NO_FILE: {
//                status = CryptFXMLController.getButton(log,this);
//                break;
//            }
//            case NO_MARK: {
//                status = CryptFXMLController.getButton(log,this);
//                break;
//            }
//            case SUCCESS_DEC: {
//                status = CryptFXMLController.getButton(log,this);
//                break;
//            }
//            case WRONG_KEY: {
//                status = CryptFXMLController.getButton(log,this);
//                break;
//            }
//            case NORMAL: {
//                status = CryptFXMLController.getButton(log,this);
//                break;
//            }
//        }
        if (file == null) {
            return;
        }
        String extension;
        String fileName = file.getName();
        if (file.getName().contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
            fileName = fileName.substring(0, fileName.length() - extension.length() - 1);
        } else {
            extension = "";
        }
        name = new SimpleStringProperty(fileName);
        type = new SimpleStringProperty(extension);

        //подсчет размера
        double fileSpace = file.length();
        double div;
        int count = 0;
        String size = "";
        div = fileSpace / 10;
        while (div > 1024) {
            div = fileSpace / 1024;
            count++;
            fileSpace = div;
        }
        switch (count) {
            case 0: {
                size = " Б";
                break;
            }
            case 1: {
                size = " КБ";
                break;
            }
            case 2: {
                size = " МБ";
                break;
            }
            case 3: {
                size = " ГБ";
                break;
            }
        }
        space = new SimpleStringProperty(String.format("%.2f", fileSpace) + size);
        absPath = file.getAbsolutePath();
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
        final MyFile other = (MyFile) obj;
        if (!Objects.equals(this.absPath, other.absPath)) {
            return false;
        }
        return true;
    }

}
