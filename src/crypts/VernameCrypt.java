package crypts;

import interfaces.Encryption;

public class VernameCrypt implements Encryption {

    @Override
    public String encrypt(String plainText, String key) {
        byte[] byteText = plainText.getBytes();
        byte[] byteKey = key.getBytes();
        byte[] byteCipher = new byte[plainText.length()];
        for (int i = 0; i < plainText.length(); i++) {
            byteCipher[i] = (byte) (byteText[i] ^ byteKey[i % key.length()]);
        }

        String cipherText = new String(byteCipher);
        System.out.println(cipherText);
        return cipherText;
    }

    @Override
    public String decrypt(String cipherText, String key) {
        byte[] byteCipher = cipherText.getBytes();
        byte[] byteKey = key.getBytes();
        byte[] bytePlainText = new byte[cipherText.length()];
        for (int i = 0; i < cipherText.length(); i++) {
            bytePlainText[i] = (byte) (byteCipher[i] ^ byteKey[i % key.length()]);
        }

        String plainText = new String(bytePlainText);
        System.out.println(plainText);
       return plainText;
    }
}
