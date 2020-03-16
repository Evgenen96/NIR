package crypts;

import interfaces.Encryption;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

        //String cipherText = new String(byteCipher);
        //System.out.println(cipherText);
        return byteCipher;
    }

    public String decrypt(byte[] cipherText, String key) {
        byte[] byteKey = key.getBytes();
        byte[] bytePlainText = new byte[cipherText.length];
        for (int i = 0; i < cipherText.length; i++) {
            bytePlainText[i] = (byte) (cipherText[i] ^ byteKey[i % key.length()]);
        }
        String plainText = new String(bytePlainText);
        System.out.println(plainText);
       return plainText;
    }
    
    public byte[] encryptFile(String fileName, String key) throws IOException {
        byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
        byte[] byteKey = key.getBytes();
        byte[] byteCipher = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteCipher[i] = (byte) (fileArray[i] ^ byteKey[i % key.length()]);
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(byteCipher);
        fos.close();
        return byteCipher;
    }
    
    public byte[] decryptFile(String fileName, String key) throws IOException {
        byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
        byte[] byteKey = key.getBytes();
        byte[] byteCipher = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteCipher[i] = (byte) (fileArray[i] ^ byteKey[i % key.length()]);
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(byteCipher);
        fos.close();
        return byteCipher;
    }
    
}
