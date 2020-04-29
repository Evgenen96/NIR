package crypter.crypt;

import crypter.crypt.ciphers.CesarCrypt;
import crypter.crypt.ciphers.CodewordCrypt;
import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.ciphers.GammaCrypt;
import crypter.crypt.ciphers.RSA2Crypt;
import crypter.crypt.ciphers.SimpleCrypt;
import crypter.crypt.ciphers.VernameCrypt;
import crypter.crypt.helpers.CryptedFile;
import crypter.crypt.helpers.EncryptedText;
import crypter.crypt.helpers.Encryption;
import crypter.crypt.helpers.States;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;

public class CryptFactory {

    //шифры
    private CodewordCrypt codewordCrypt;
    private VernameCrypt vernameCrypt;
    private SimpleCrypt simpleCrypt;
    private CesarCrypt cesarCrypt;
    private RSA2Crypt rsaCrypt;
    private GammaCrypt gammaCrypt;

    private SimpleDateFormat formatter;

    public CryptFactory() {
        formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        codewordCrypt = new CodewordCrypt();
        vernameCrypt = new VernameCrypt();
        simpleCrypt = new SimpleCrypt();
        cesarCrypt = new CesarCrypt();
        try {
            rsaCrypt = new RSA2Crypt();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(CryptFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        gammaCrypt = new GammaCrypt();
        FileMetaMaker.initalize();

    }

    //получение экземпляра класс нужного шифра
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

    public CryptedFile encryptFile(CryptTypes cryptType, String fileAbsPath, String key) {

        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileAbsPath));
            byte[] encryptedFile = null;

            encryptedFile = this.getCrypt(cryptType).encryptFile(fileArray, key);

            //добавление метки, что файл зашифрован 
            FileMetaMaker cMark = new FileMetaMaker();
            byte[] markedEnFile = cMark.addMark(encryptedFile, fileAbsPath);

            //сохранения хэша шифрования в секретный файл
            cMark.saveSecretKey(cryptType, key);

            //запись в файл
            try (FileOutputStream fos = new FileOutputStream(fileAbsPath)) {
                fos.write(markedEnFile);
            }
            File enFile = new File(cMark.changeExtension(fileAbsPath));
            String date = formatter.format(new Date(System.currentTimeMillis()));
            System.out.println(fileAbsPath + " был зашифрован методом " + cryptType.getName());
            return new CryptedFile(enFile, key, cryptType, States.SUCCESS_ENC, date);
        } catch (IOException ex) {
            return new CryptedFile(null, key, cryptType, States.NO_FILE, null);
        }
    }

    public CryptedFile decryptFile(CryptTypes cryptType, String fileAbsPath, String key) {
        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileAbsPath));
            byte[] decryptedFile;

            //проверка наличия метки метки
            FileMetaMaker cMark = new FileMetaMaker();
            if (cMark.readMark(fileArray)) {
                fileArray = Arrays.copyOfRange(fileArray, cMark.getMarkLength() + 10, fileArray.length);
            } else {
                return new CryptedFile(null, key, cryptType, States.NO_MARK, null);
            }

            //извлечение индекса
            if (!cMark.checkSecretKey(key)) {
                return new CryptedFile(null, key, cryptType, States.WRONG_KEY, null);
            }

            //расшифровка
            decryptedFile = this.getCrypt(cryptType).decryptFile(fileArray, key);

            //запись
            FileOutputStream fos = new FileOutputStream(fileAbsPath);
            fos.write(decryptedFile);
            fos.close();

            File deFile = new File(cMark.changeExtension(fileAbsPath));
            String date = formatter.format(new Date(System.currentTimeMillis()));
            System.out.println(fileAbsPath + " был расшифрован методом " + cryptType.getName());
            return new CryptedFile(deFile, key, cryptType, States.SUCCESS_DEC, date);

        } catch (IOException ex) {
            return new CryptedFile(null, key, cryptType, States.NO_FILE, null);
        }
    }

    //расшифровка без проверок
    public CryptedFile decryptFile(CryptTypes cryptType, String fileAbsPath, String key, boolean isForced) {
        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileAbsPath));
            byte[] decryptedFile;

            //проверка наличия метки метки
            FileMetaMaker cMark = new FileMetaMaker();
            if (!isForced) {
                if (cMark.readMark(fileArray)) {
                    fileArray = Arrays.copyOfRange(fileArray, cMark.getMarkLength() + 10, fileArray.length);
                } else {
                    return new CryptedFile(null, key, cryptType, States.NO_MARK, null);
                }
            }

            //извлечение индекса
            if (!cMark.checkSecretKey(key) && !isForced) {
                return new CryptedFile(null, key, cryptType, States.WRONG_KEY, null);
            }

            //расшифровка
            decryptedFile = this.getCrypt(cryptType).decryptFile(fileArray, key);

            //запись
            FileOutputStream fos = new FileOutputStream(fileAbsPath);
            fos.write(decryptedFile);
            fos.close();

            File deFile = new File(cMark.changeExtension(fileAbsPath));
            String date = formatter.format(new Date(System.currentTimeMillis()));
            System.out.println(fileAbsPath + " был расшифрован методом  " + cryptType.getName() + "без проверок");
            return new CryptedFile(deFile, key, cryptType, States.SUCCESS_DEC, date);

        } catch (IOException ex) {
            return new CryptedFile(null, key, cryptType, States.NO_FILE, null);
        }
    }

}
