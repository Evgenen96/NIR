package crypter.crypt.helpers;

public class CryptCredentionals {

    private String key;
    private String filePath;
    private CryptTypes cryptType;

    public CryptCredentionals() {
    }

    public CryptCredentionals(CryptTypes cryptType, String filePath, String key) {
        this.key = key;
        this.filePath = filePath;
        this.cryptType = cryptType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public CryptTypes getCryptType() {
        return cryptType;
    }

    public void setCryptType(CryptTypes cryptType) {
        this.cryptType = cryptType;
    }

}
