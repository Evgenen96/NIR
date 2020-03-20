package crypts;

import interfaces.EncryptedText;
import interfaces.Encryption;
import javaapplication2.MyAlphabet;

public class CodewordCrypt implements Encryption{

    private MyAlphabet a; //английский исходный алфавит
    private final CryptTypes CRYPTID = CryptTypes.CODEWORD; 
    
    public CodewordCrypt() {
        a = new MyAlphabet();
    }

    @Override
    public EncryptedText encrypt(String plainText, String key) {
        EncryptedText eText = new EncryptedText();
        MyAlphabet b = new MyAlphabet(a); //алфавит подстановки
        b.insertCodeword(key); //вставка слова вначале нового алфавита
        String enText = "";
        for (int i = 0; i < plainText.length(); i++) {
            enText = enText + b.getChar(a.getInt(plainText.charAt(i)));
        }
        eText.setText(enText);
        return eText;
    }

    @Override
    public String decrypt(EncryptedText eText, String key) {
        MyAlphabet b = new MyAlphabet(a);
        String enText = eText.getText();
        b.insertCodeword(key);
        String deText = "";
        for (int i = 0; i < enText.length(); i++) {
            deText = deText + b.getChar(a.getInt(enText.charAt(i)));
        }

        return deText;
    }

    @Override
    public byte[] encryptFile(byte[] fileArray, String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] decryptFile(byte[] fileArray, String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CryptTypes getCryptID() {
        return CRYPTID;
    }
    
    

}
