package javaapplication2;

import com.sun.crypto.provider.RSACipher;
import crypts.CesarCrypt;
import crypts.CodewordCrypt;
import crypts.GammaCrypt;
import crypts.RSA2Crypt;
import crypts.RSACrypt;
import crypts.SimpleCrypt;
import crypts.VernameCrypt;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class JavaApplication2 {

    public static void main(String[] args) {
        try {

// char n = (char)(p^s);
// CodewordCrypt cc1 = new CodewordCrypt();
 //VernameCrypt cc2 = new VernameCrypt();
 //GammaCrypt cc2 = new GammaCrypt();
 SimpleCrypt cc2 = new SimpleCrypt();
            RSA2Crypt rsa = new RSA2Crypt();
            cc2.encryptFile("src/res/file1.docx", "21");
            cc2.decryptFile("src/res/file1.docx", "21");
            //cc2.decrypt(cc2.encrypt("rabota", "key"),"key");
            //rsa.encryptFile("src/res/alena.jpg");
//CesarCrypt cc = new CesarCrypt();
// cc.decrypt(cc.encrypt("WorkIsDone", "15"),"15");
//cc2.decrypt(cc2.encrypt("WorkIsDonedgtr", "3124"),"3124");
//cc1.decrypt("dceh", "drow");  
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvalidKeyException ex) {
//            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalBlockSizeException ex) {
//            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (BadPaddingException ex) {
//            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
