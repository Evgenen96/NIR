package javaapplication2;

import crypts.CesarCrypt;
import crypts.CodewordCrypt;
import crypts.CryptTypes;
import crypts.GammaCrypt;
import crypts.RSA2Crypt;
import crypts.SimpleCrypt;
import crypts.VernameCrypt;
import interfaces.EncryptedText;
import interfaces.Encryption;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;

public class Crypt {

    public CodewordCrypt codewordCrypt;
    public VernameCrypt vernameCrypt;
    public SimpleCrypt simpleCrypt;
    public CesarCrypt cesarCrypt;
    public RSA2Crypt rsaCrypt;
    public GammaCrypt gammaCrypt;

    private int fileID = 12345;

    public Crypt() throws NoSuchAlgorithmException, NoSuchPaddingException {
        codewordCrypt = new CodewordCrypt();
        vernameCrypt = new VernameCrypt();
        simpleCrypt = new SimpleCrypt();
        cesarCrypt = new CesarCrypt();
        rsaCrypt = new RSA2Crypt();
        gammaCrypt = new GammaCrypt();
    }

    public Encryption getCrypt(CryptTypes cryptType) {
        Encryption toReturn = null;
        switch (cryptType) {
            case CODEWORD: {
                toReturn = codewordCrypt;
                break;
            }
            case SIMPLE: {
                toReturn = simpleCrypt;
                break;
            }
            case VERNAME: {
                toReturn = vernameCrypt;
                break;
            }
            case CESAR: {
                toReturn = cesarCrypt;
                break;
            }
            case RSA: {
                toReturn = rsaCrypt;
                break;
            }
            case GAMMA: {
                toReturn = gammaCrypt;
                break;
            }
            default:
                throw new IllegalArgumentException("Wrong encryption type:" + cryptType);
        }
        return toReturn;
    }

    public EncryptedText encryptText(CryptTypes cryptType, String text, String key) {
        return this.getCrypt(cryptType).encrypt(text, key);
    }

    public String decryptText(CryptTypes cryptType, EncryptedText eText, String key) {
        return this.getCrypt(cryptType).decrypt(eText, key);
    }

    public void encryptFile(CryptTypes cryptType, String fileName, String key) throws FileNotFoundException {
        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
            byte[] encryptedFile = null;

            encryptedFile = this.getCrypt(cryptType).encryptFile(fileArray, key);

//            //индексирование файла
//            String fileID = String.valueOf(this.fileID);
//            byte[] byteFileID = fileID.getBytes();
//            byte[] indexedEnFile = new byte[encryptedFile.length + byteFileID.length + 1];
//            //первый байт - количество байтов на индекс
//            indexedEnFile[0] = String.valueOf(byteFileID.length).getBytes()[0];
//            //запись индекса
//            for (int i = 1; i <= byteFileID.length; i++) {
//                indexedEnFile[i] = byteFileID[i - 1];
//            }
//            int zeroPos = 0;
//            //запись файла
//            for (int i = byteFileID.length + 1; i < indexedEnFile.length; i++) {
//                indexedEnFile[i] = encryptedFile[zeroPos++];
//            }

            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                fos.write(encryptedFile);
            }
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void decryptFile(CryptTypes cryptType, String fileName, String key) throws FileNotFoundException {
        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
            byte[] decryptedFile = null;

//            //извлечение индекса
//            int IDLength = Integer.valueOf(new String(new byte[]{fileArray[0]}));
//            int fileID = Integer.valueOf(new String(Arrays.copyOfRange(fileArray, 1, IDLength + 1)));
//            byte[] clearFileArray = Arrays.copyOfRange(fileArray, IDLength + 1, fileArray.length);

            decryptedFile = this.getCrypt(cryptType).decryptFile(fileArray, key);

            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(decryptedFile);
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
