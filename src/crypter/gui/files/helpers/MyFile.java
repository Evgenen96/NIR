package crypter.gui.files.helpers;

import java.io.File;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;

public class MyFile {

    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleStringProperty space;
    private SimpleStringProperty status;
    private String absPath;

    public MyFile(File file) {
        String extension;
        String fileName = file.getName();
        if (file.getName().contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
            fileName = fileName.substring(0, fileName.length() - extension.length());
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
        div = fileSpace / 1024;
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
        status = new SimpleStringProperty("unk");
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

    public String getStatus() {
        return status.get();
    }

    public String getAbsPath() {
        return absPath;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public void setStatus(String status) {
        this.status = new SimpleStringProperty(status);
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
