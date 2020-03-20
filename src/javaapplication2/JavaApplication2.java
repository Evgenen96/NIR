package javaapplication2;

import crypts.CryptTypes;
import interfaces.EncryptedText;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;

public class JavaApplication2 {

    public static void main(String[] args) {
        try {
            Crypt c = new Crypt();
            c.encryptFile(CryptTypes.CESAR, "src/res/ab.jpg", "thysaa", true);
            c.decryptFile(null, "src/res/ab.jpg", "thddysaa");
            EncryptedText s = c.encryptText(CryptTypes.CESAR, "sssss", "bomba");
            String d  = c.decryptText(CryptTypes.CESAR, s, "bomba");
            
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | FileNotFoundException ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
