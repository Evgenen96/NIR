package javaapplication2;

import crypts.CesarCrypt;
import crypts.CodewordCrypt;
import crypts.CryptTypes;
import static crypts.CryptTypes.CODEWORD;
import crypts.GammaCrypt;
import crypts.RSA2Crypt;
import crypts.SimpleCrypt;
import crypts.VernameCrypt;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Crypt {

    public CodewordCrypt codewordCrypt;
    public VernameCrypt vernameCrypt;
    public SimpleCrypt simpleCrypt;
    public CesarCrypt cesarCrypt;
    public RSA2Crypt rsaCrypt;
    public GammaCrypt gammaCrypt;

    public Crypt() throws NoSuchAlgorithmException, NoSuchPaddingException {
        codewordCrypt = new CodewordCrypt();
        vernameCrypt = new VernameCrypt();
        simpleCrypt = new SimpleCrypt();
        cesarCrypt = new CesarCrypt();
        rsaCrypt = new RSA2Crypt();
        gammaCrypt = new GammaCrypt();
    }

    public byte[] encryptText(CryptTypes cryptType, String text, String key) throws UnsupportedEncodingException {
        
        byte[] bytePlainText = text.getBytes();
        byte[] encryptedText = null;
        switch (cryptType) {
            case CODEWORD: {

                break;
            }
            case SIMPLE: {
                encryptedText = simpleCrypt.encryptFile(bytePlainText, key);
                break;
            }
            case VERNAME: {
                encryptedText = vernameCrypt.encryptFile(bytePlainText, key);
                break;
            }
            case CESAR: {
                encryptedText = cesarCrypt.encryptFile(bytePlainText, key);
                break;
            }
            case RSA: {
                try {
                    encryptedText = rsaCrypt.encryptFile(bytePlainText);
                    break;
                } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
                    Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case GAMMA: {
                encryptedText = gammaCrypt.encryptFile(bytePlainText, key);
                break;
            }
        }
        return encryptedText;
    }

    public String decryptText(CryptTypes cryptType, byte[] encryptedText, String key) throws UnsupportedEncodingException {
        byte[] decryptedText = null;
        switch (cryptType) {
            case CODEWORD: {

                break;
            }
            case SIMPLE: {
                decryptedText = simpleCrypt.decryptFile(encryptedText, key);
                break;
            }
            case VERNAME: {
                decryptedText = vernameCrypt.decryptFile(encryptedText, key);
                break;
            }
            case CESAR: {
                decryptedText = cesarCrypt.decryptFile(encryptedText, key);
                break;
            }
            case RSA: {
                try {
                    decryptedText = rsaCrypt.decryptFile(encryptedText);
                    break;
                } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
                    Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case GAMMA: {
                decryptedText = gammaCrypt.decryptFile(encryptedText, key);
                break;
            }
        }
        return new String(decryptedText);
    }

    public void encryptFile(CryptTypes cryptType, String fileName, String key) throws FileNotFoundException {
        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
            byte[] encryptedFile = null;
            switch (cryptType) {
                case CODEWORD: {

                    break;
                }
                case SIMPLE: {
                    encryptedFile = simpleCrypt.encryptFile(fileArray, key);
                    break;
                }
                case VERNAME: {
                    encryptedFile = vernameCrypt.encryptFile(fileArray, key);
                    break;
                }
                case CESAR: {
                    encryptedFile = cesarCrypt.encryptFile(fileArray, key);
                    break;
                }
                case RSA: {
                    encryptedFile = rsaCrypt.encryptFile(fileArray);
                    break;
                }
                case GAMMA: {
                    encryptedFile = gammaCrypt.encryptFile(fileArray, key);
                    break;
                }
            }

            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(encryptedFile);
            fos.close();
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | IOException | NoSuchPaddingException | BadPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void decryptFile(CryptTypes cryptType, String fileName, String key) throws FileNotFoundException {
        try {
            byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
            byte[] decryptedFile = null;
            switch (cryptType) {
                case CODEWORD: {

                    break;
                }
                case SIMPLE: {
                    decryptedFile = simpleCrypt.decryptFile(fileArray, key);
                    break;
                }
                case VERNAME: {
                    decryptedFile = vernameCrypt.decryptFile(fileArray, key);
                    break;
                }
                case CESAR: {
                    decryptedFile = cesarCrypt.decryptFile(fileArray, key);
                    break;
                }
                case RSA: {
                    decryptedFile = rsaCrypt.decryptFile(fileArray);
                    break;
                }
                case GAMMA: {
                    decryptedFile = gammaCrypt.decryptFile(fileArray, key);
                    break;
                }
            }

            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(decryptedFile);
            fos.close();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
