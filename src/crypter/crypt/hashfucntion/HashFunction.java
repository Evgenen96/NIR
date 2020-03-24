package crypter.crypt.hashfucntion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashFunction {

    static MessageDigest digest;

    public HashFunction() {
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Ошибка создания SHA-256");
        }
    }
    
    public static byte[] getHash(String key) {
        if (digest == null) {
            HashFunction h = new HashFunction();
        }
        return digest.digest(key.getBytes());
    }
    
    public static String getHexHash(String key) {
        return bytesToHex(getHash(key));
    }
    
    private static String bytesToHex(byte[] hash) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
    String hex = Integer.toHexString(0xff & hash[i]);
    if(hex.length() == 1) hexString.append('0');
        hexString.append(hex);
    }
    return hexString.toString();
}

}
