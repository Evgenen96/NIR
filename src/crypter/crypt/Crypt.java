package crypter.crypt;

import crypter.crypt.ciphers.CesarCrypt;
import crypter.crypt.ciphers.CodewordCrypt;
import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.ciphers.GammaCrypt;
import crypter.crypt.ciphers.RSA2Crypt;
import crypter.crypt.ciphers.SimpleCrypt;
import crypter.crypt.ciphers.VernameCrypt;
import crypter.crypt.helpers.EncryptedText;
import crypter.crypt.helpers.Encryption;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;

public class Crypt {

    //шифры
    public CodewordCrypt codewordCrypt;
    public VernameCrypt vernameCrypt;
    public SimpleCrypt simpleCrypt;
    public CesarCrypt cesarCrypt;
    public RSA2Crypt rsaCrypt;
    public GammaCrypt gammaCrypt;

    public Crypt() {
        codewordCrypt = new CodewordCrypt();
        vernameCrypt = new VernameCrypt();
        simpleCrypt = new SimpleCrypt();
        cesarCrypt = new CesarCrypt();
        try {
            rsaCrypt = new RSA2Crypt();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            case VERTICAL: {
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

    public boolean encryptFile(CryptTypes cryptType, String fileName, String key, boolean saveEncryption) {
        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
            byte[] encryptedFile;

            //шифрование
            encryptedFile = this.getCrypt(cryptType).encryptFile(fileArray, key);

            //добавление метки, что файл зашифрован 
            FileMeta cMark = new FileMeta(saveEncryption);
            byte[] markedEnFile = cMark.addMark(encryptedFile);

            //сохранения шифрования в секретный файл
            if (saveEncryption) {
                cMark.saveSecretKey(cryptType, key);
            }

            //запись в файл
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                fos.write(markedEnFile);
            }
        } catch (IOException ex) {
            System.out.println("Ошибка при записи в файл " + fileName);
            return false;
        }
        return true;
    }

    public boolean decryptFile(CryptTypes cryptType, String fileName, String key) {
        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
            byte[] decryptedFile;

            //проверка наличия метки метки
            FileMeta cMark = new FileMeta();
            if (cMark.readMark(fileArray)) {
                fileArray = Arrays.copyOfRange(fileArray, cMark.getMarkLength() + 10, fileArray.length);
            } else {
                System.out.println("Файл не был зашифрован");
                return false;
            }

            //извлечение индекса если есть
            if (cMark.isCipherSaved()) {
                String[] idLine = cMark.extractSecretKey();
                if (idLine != null) {
                    cryptType = CryptTypes.valueOf(idLine[1]);
                    key = idLine[2];
                } else {
                    System.out.println("Ключ к файлу не найден");
                    return false;
                }
            }
            //расшифровка
            decryptedFile = this.getCrypt(cryptType).decryptFile(fileArray, key);

            //запись
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(decryptedFile);
            fos.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
