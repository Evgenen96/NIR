package interfaces;

import crypts.CryptTypes;

public interface Encryption {

    public EncryptedText encrypt(String line, String key);

    public String decrypt(EncryptedText line, String key);
    
    public byte[] encryptFile(byte[] fileArray, String key);
    
    public byte[] decryptFile(byte[] fileArray, String key);
    
    public CryptTypes getCryptID();
}
