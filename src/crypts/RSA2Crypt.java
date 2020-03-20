package crypts;

import interfaces.EncryptedText;
import interfaces.Encryption;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RSA2Crypt implements Encryption{

    private KeyPair keyPair;
    private KeyPairGenerator keyPairGenerator;
    private KeyGenerator generator;
    private SecretKey secKey;
    private byte[] encryptedKey;

    public RSA2Crypt() throws NoSuchAlgorithmException, NoSuchPaddingException {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        generator = KeyGenerator.getInstance("AES");
        generator.init(128);
    }

    @Override
    public EncryptedText encrypt(String plainText, String key) {
        try {
            EncryptedText eText = new EncryptedText();
            Cipher cipher = Cipher.getInstance("RSA");
            keyPair = keyPairGenerator.generateKeyPair();
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] enText = cipher.doFinal(plainText.getBytes());
            eText.setText(enText);
            return eText;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RSA2Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String decrypt(EncryptedText eText, String key)  {
        try {
            byte[] enText = eText.getByteText();
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] deText = decryptCipher.doFinal(enText);
            return new String(deText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(RSA2Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public byte[] encryptFile(byte[] fileArray, String key)  {

        try {
            //генерация симметричного ключа
            secKey = generator.generateKey();
            
            //шифрование файла
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
            byte[] byteCipher = aesCipher.doFinal(fileArray);
            
            //генерация ключевой пары
            keyPair = keyPairGenerator.generateKeyPair();
            
            //шифрование ключа
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.PUBLIC_KEY, keyPair.getPublic());
            byte[] encryptedKey = cipher.doFinal(secKey.getEncoded());
            for (int i = 0; i < encryptedKey.length; i++) {
                System.out.print(encryptedKey[i] + " ");
            }
            
            //передача ключа
            this.encryptedKey = encryptedKey;
            return byteCipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RSA2Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public byte[] decryptFile(byte[] fileArray, String key)  {

        try {
            //расшифровка ключа
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.PRIVATE_KEY, keyPair.getPrivate());
            byte[] decryptedKey = cipher.doFinal(encryptedKey);
            SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
            
            //расшифровка файла
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] byteFile = aesCipher.doFinal(fileArray);
            return byteFile;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RSA2Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
