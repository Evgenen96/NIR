package crypter.crypt.helpers;

import java.io.File;
import java.io.Serializable;

public class CryptedFile implements Serializable {

    private File file;
    private String date;
    private transient String key;
    private transient CryptTypes crypt;
    private transient States state;
    
    public CryptedFile(File file) {
        this.file = file;
        this.date = date;
        this.key = null;
        this.crypt = null;
        this.state = null;
    }

    public CryptedFile(File file, String key, CryptTypes crypt, States state, String date) {
        this.file = file;
        this.key = key;
        this.crypt = crypt;
        this.state = state;
        this.date = date;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CryptTypes getCrypt() {
        return crypt;
    }

    public void setCrypt(CryptTypes crypt) {
        this.crypt = crypt;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    

}
