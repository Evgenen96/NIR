package crypts;

import interfaces.Encryption;
import javaapplication2.MyAlphabet;

public class CodewordCrypt  {
    
    private MyAlphabet a; //английский исходный алфавит

    public CodewordCrypt() {
        a = new MyAlphabet();
    }

    public String encrypt(String plainText, String key) {
        MyAlphabet b = new MyAlphabet(a); //алфавит подстановки
        b.insertCodeword(key); //вставка слова вначале нового алфавита
        String cipherText = "";
        for (int i = 0; i < plainText.length(); i++) {
            cipherText = cipherText + b.getChar(a.getInt(plainText.charAt(i)));
        }
        
        System.out.println(cipherText);
        return cipherText;
    }
    
    public String decrypt(String cipherText, String key) {
        MyAlphabet b = new MyAlphabet(a);
        b.insertCodeword(key);
        String plainText = "";
        for (int i = 0; i < cipherText.length(); i++) {
            plainText = plainText + b.getChar(a.getInt(cipherText.charAt(i)));
        }
        
        System.out.println(plainText);
        return plainText;
    }
    
}
