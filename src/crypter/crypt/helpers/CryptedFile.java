package crypter.crypt.helpers;

import java.io.File;

public class CryptedFile {

    private File file;
    private String key;
    private CryptTypes crypt;
    private States state;

    public CryptedFile(File file, String key, CryptTypes crypt, States state) {
        this.file = file;
        this.key = key;
        this.crypt = crypt;
        this.state = state;
        System.out.println(state.getDescription());
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

}
