package crypts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VernameCrypt {

    public byte[] encrypt(String plainText, String key) {
        byte[] byteText = plainText.getBytes();
        byte[] byteKey = key.getBytes();
        byte[] byteCipher = new byte[plainText.length()];
        for (int i = 0; i < plainText.length(); i++) {
            byteCipher[i] = (byte) (byteText[i] ^ byteKey[i % key.length()]);
        }
        return byteCipher;
    }

    public String decrypt(byte[] cipherText, String key) {
        byte[] byteKey = key.getBytes();
        byte[] bytePlainText = new byte[cipherText.length];
        for (int i = 0; i < cipherText.length; i++) {
            bytePlainText[i] = (byte) (cipherText[i] ^ byteKey[i % key.length()]);
        }
        String plainText = new String(bytePlainText);
        return plainText;
    }

    public byte[] encryptFile(byte[] fileArray, String key) {
        byte[] byteKey = key.getBytes();
        byte[] byteCipher = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteCipher[i] = (byte) (fileArray[i] ^ byteKey[i % key.length()]);
        }
        return byteCipher;
    }

    public byte[] decryptFile(byte[] fileArray, String key) {
        byte[] byteKey = key.getBytes();
        byte[] byteFile = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteFile[i] = (byte) (fileArray[i] ^ byteKey[i % key.length()]);
        }
        return byteFile;
    }

}
