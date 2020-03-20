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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
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
    private String secretFilePath = "src/res/pas.txt";

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

    public void encryptFile(CryptTypes cryptType, String fileName, String key, boolean saveEncryption) throws FileNotFoundException {
        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
            byte[] encryptedFile;
            System.out.println(fileArray.length);

            //шифрование
            encryptedFile = this.getCrypt(cryptType).encryptFile(fileArray, key);
            System.out.println(encryptedFile.length);

            //сохранить информацию о шифровании
            if (saveEncryption) {           
                encryptedFile = saveKeyForFile(encryptedFile, key, cryptType);
            }
            //запись
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

            if (cryptType == null) {
                //извлечение индекса
                int IDLength = Integer.valueOf(new String(new byte[]{fileArray[0]}));
                int fileID = Integer.valueOf(new String(Arrays.copyOfRange(fileArray, 1, IDLength + 1)));
                String[] idLine = extractKey(fileID);
                if (idLine == null) {
                    System.out.println("Ключ к файлу не найден");
                    return;
                } else {
                    cryptType = CryptTypes.valueOf(idLine[1]);
                    key = idLine[2];
                    byte[] clearFileArray = Arrays.copyOfRange(fileArray, IDLength + 1, fileArray.length);
                    System.out.println(fileArray.length);
                    fileArray = Arrays.copyOf(clearFileArray, clearFileArray.length);
                    System.out.println(fileArray.length);
                    System.out.println(clearFileArray.length);
                    System.out.println("");
                }

            }
            decryptedFile = this.getCrypt(cryptType).decryptFile(fileArray, key);

            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(decryptedFile);
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] saveKeyForFile(byte[] byteFile, String key, CryptTypes crypt) {
        //индексирование файла
        String fileID = String.valueOf(this.fileID);
        byte[] byteFileID = fileID.getBytes();
        byte[] indexedEnFile = new byte[byteFile.length + byteFileID.length + 1];
        //первый байт - количество байтов на индекс
        indexedEnFile[0] = String.valueOf(byteFileID.length).getBytes()[0];
        //запись индекса
        for (int i = 1; i <= byteFileID.length; i++) {
            indexedEnFile[i] = byteFileID[i - 1];
        }
        int zeroPos = 0;
        //запись файла
        for (int i = byteFileID.length + 1; i < indexedEnFile.length; i++) {
            indexedEnFile[i] = byteFile[zeroPos++];
        }

        //сохранения ключа шифрования в секретный файл
        try {
            FileWriter fos;
            fos = new FileWriter(secretFilePath, true);
            fos.write(this.fileID + ";;;" + this.getCrypt(crypt).getCryptID() + ";;;" + key + "\n");
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fileID++;
        return indexedEnFile;
    }

    public String[] extractKey(int fileID) {
        try {
            int idLength = String.valueOf(fileID).length();
            BufferedReader br = new BufferedReader(new FileReader(secretFilePath));
            String line = br.readLine();
            while (line != null) {
                if (line.substring(0, idLength).equals(String.valueOf(fileID))) {
                    break;
                }
                line = br.readLine();
            }
            br.close();
            return line.split(";;;");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
