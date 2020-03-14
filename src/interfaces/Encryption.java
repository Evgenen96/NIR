package interfaces;

public interface Encryption {

    public String encrypt(String line, String key);

    public String decrypt(String line, String key);
}
