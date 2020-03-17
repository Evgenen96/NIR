package crypts;

import java.io.FileOutputStream;
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
import javax.crypto.KeyGenerator;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RSA2Crypt {

    private KeyPair keyPair; 
    private KeyPairGenerator keyPairGenerator;
    private KeyGenerator generator;
    private SecretKey secKey;

    public RSA2Crypt() throws NoSuchAlgorithmException, NoSuchPaddingException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        generator = KeyGenerator.getInstance("AES");
        generator.init(128);
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
        return plainText;
    }

    public byte[] encryptFile(String fileName) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException {

        //генерация симметричного ключа
        secKey = generator.generateKey();

        //шифрование файла
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
        byte[] byteCipher = aesCipher.doFinal(fileArray);
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(byteCipher);
        fos.close();

        //генерация ключевой пары
        keyPair = keyPairGenerator.generateKeyPair();

        //шифрование ключа
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PUBLIC_KEY, keyPair.getPublic());
        byte[] encryptedKey = cipher.doFinal(secKey.getEncoded());

        //передача ключа
        return encryptedKey;
    }

    public void decryptFile(String fileName, byte[] encryptedKey) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException {

        //расшифровка ключа
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PRIVATE_KEY, keyPair.getPrivate());
        byte[] decryptedKey = cipher.doFinal(encryptedKey);
        SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");

        //расшифровка файла
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
        byte[] byteCipher = aesCipher.doFinal(fileArray);

        //запись в файл
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(byteCipher);
        fos.close();
    }
}
