package crypts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;

public class RSA2Crypt {

    private KeyPair keyPair;
    private KeyPairGenerator keyPairGenerator;

    public RSA2Crypt() throws NoSuchAlgorithmException, NoSuchPaddingException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    }

    public byte[] encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        keyPair = keyPairGenerator.generateKeyPair();
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] byteCipher = cipher.doFinal(plainText.getBytes());
        return byteCipher;
    }

    public String decrypt(byte[] cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] byteText = decryptCipher.doFinal(cipherText);
        String plainText = new String(byteText);
        System.out.println(plainText);
        return plainText;
    }

    public byte[] encryptFile(String fileName) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
        Cipher cipher = Cipher.getInstance("RSA");
        keyPair = keyPairGenerator.generateKeyPair();
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] byteCipher = cipher.doFinal(fileArray);
        System.out.println(byteCipher);
        return byteCipher;
    }
}
