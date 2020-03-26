package crypter.gui.files.helpers;

import crypter.crypt.helpers.CryptedFile;
import crypter.gui.files.CryptController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;


//класс-строка в таблице списка выбранных файлов
public class MyFile {

    private static CryptController crypt;

    //поля таблицы
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleStringProperty space;
    private Button status;
    private Button relove;

    private String absPath;
    private CryptedFile file;
    private static FileInputStream fis;
    private static Image i;

  
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

        absPath = file.getAbsolutePath();
        try {
            fis = new FileInputStream("src/res/processing.gif");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        i = new Image(fis, 16, 16, true, true);
    }

    public static CryptController getCrypt() {
        return crypt;
    }

    public void setStatusImage() {
        status = new Button();
        status.setBackground(Background.EMPTY);
        status.setGraphic(new ImageView(i));
    }

    //апдейт записи в таблице
    public void setState(CryptedFile cryptedFile) {
        this.file = cryptedFile;

        if (file.getFile() == null) {
            relove = crypt.getButton(this);
            return;
        }
        status = crypt.getButton(this);
        String extension;
        String fileName = file.getFile().getName();
        if (file.getFile().getName().contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
            fileName = fileName.substring(0, fileName.length() - extension.length() - 1);
        } else {
            extension = "";
        }
        name = new SimpleStringProperty(fileName);
        type = new SimpleStringProperty(extension);

        //подсчет размера
        double fileSpace = file.getFile().length();
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
        absPath = file.getFile().getAbsolutePath();
    }

    public static void setCrypt(CryptController crypt) {
        MyFile.crypt = crypt;
    }

    public CryptedFile getCryptInfo() {
        return file;
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

    public String getAbsPath() {
        return absPath;
    }

    public Button getStatus() {
        return status;
    }

    public Button getRelove() {
        return relove;
    }

    public void setStatus(Button status) {
        this.status = status;
    }

    public void setRelove(Button relove) {
        this.relove = relove;
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

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

}
