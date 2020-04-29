    package crypter.crypt.ciphers;

import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.helpers.EncryptedText;
import crypter.crypt.helpers.Encryption;

public class VernameCrypt implements Encryption {

    private final CryptTypes CRYPTID = CryptTypes.VERNAME;

    @Override
    public EncryptedText encrypt(String plainText, String key) {
        EncryptedText eText = new EncryptedText();
        byte[] byteText = plainText.getBytes();
        byte[] byteKey = key.getBytes();
        byte[] enText = new byte[byteText.length];
        for (int i = 0; i < byteText.length; i++) {
            enText[i] = (byte) (byteText[i] ^ byteKey[i % key.length()]);
        }
        eText.setText(enText);
        return eText;
    }

    @Override
    public String decrypt(EncryptedText eText, String key) {
        byte[] enText = eText.getByteText();
        byte[] byteKey = key.getBytes();
        byte[] deText = new byte[enText.length];
        for (int i = 0; i < enText.length; i++) {
            deText[i] = (byte) (enText[i] ^ byteKey[i % key.length()]);
        }
        return new String(deText);
    }

    @Override
    public byte[] encryptFile(byte[] fileArray, String key) {
        byte[] byteKey = key.getBytes();
        byte[] byteCipher = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteCipher[i] = (byte) (fileArray[i] ^ byteKey[i % key.length()]);
        }
        return byteCipher;
    }

    @Override
    public byte[] decryptFile(byte[] fileArray, String key) {
        byte[] byteKey = key.getBytes();
        byte[] byteFile = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteFile[i] = (byte) (fileArray[i] ^ byteKey[i % key.length()]);
        }
        return byteFile;
    }

    @Override
    public CryptTypes getCryptID() {
        return CRYPTID;
    }

    @Override
    public boolean isKeyCorrect(String key) {
        return true;
    }

}
