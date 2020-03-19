package javaapplication2;

import crypts.CryptTypes;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;

public class JavaApplication2 {

    public static void main(String[] args) {
        try {
            Crypt c = new Crypt();
            //System.out.println(new String("работа не волк".getBytes()));
            byte[] s = c.encryptText(CryptTypes.CESAR, "абрамов илья сергеевич", "дяди");
            String d = c.decryptText(CryptTypes.CESAR, s, "дяди");
            System.out.println(d);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
