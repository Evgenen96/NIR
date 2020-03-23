package crypter.crypt.ciphers;

import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.helpers.EncryptedText;
import crypter.crypt.helpers.Encryption;

public class SimpleCrypt implements Encryption {

    private final CryptTypes CRYPTID = CryptTypes.SIMPLE;

    @Override
    public EncryptedText encrypt(String plainText, String key) {

        EncryptedText eText = new EncryptedText();
        String enText = "";
        int[] keyArr = transformKey(key);

        for (int blockNumber = 0; blockNumber <= (plainText.length() / key.length()); blockNumber++) {
            for (int offset = 0; offset < key.length(); offset++) {
                if ((keyArr.length * blockNumber + keyArr[offset]) >= plainText.length()) {
                    continue;
                }
                enText = enText + plainText.charAt(keyArr.length * blockNumber + keyArr[offset]);
            }

        }
        eText.setText(enText);
        return eText;
    }

    @Override
    public String decrypt(EncryptedText eText, String key) {
        String enText = eText.getText();
        String deText;
        int[] keyArr = transformKey(key);

        char[] newLineArr = new char[enText.length()];
        int lastBlockFix = 0; //когда неидеальные блоки
        for (int blockNumber = 0; blockNumber <= enText.length() / key.length(); blockNumber++) {
            for (int enOffset = 0; enOffset < key.length(); enOffset++) {
                if (key.length() * blockNumber + keyArr[enOffset] >= enText.length()) {
                    lastBlockFix++;
                    continue;
                }
                if (key.length() * blockNumber + enOffset - lastBlockFix >= enText.length()) {
                    break;
                }
                newLineArr[key.length() * blockNumber + keyArr[enOffset]] = enText.charAt(key.length() * blockNumber + enOffset - lastBlockFix);
            }
        }
        deText = String.valueOf(newLineArr);
        return deText;
    }

    @Override
    public byte[] encryptFile(byte[] fileArray, String key) {

        byte[] byteCipher = new byte[fileArray.length];
        int[] keyArr = transformKey(key);

        int pos = 0;
        for (int blockNumber = 0; blockNumber <= (fileArray.length / key.length()); blockNumber++) {
            for (int offset = 0; offset < key.length(); offset++) {
                if ((keyArr.length * blockNumber + keyArr[offset]) >= fileArray.length) {
                    continue;
                }
                byteCipher[pos++] = fileArray[keyArr.length * blockNumber + keyArr[offset]];
            }
        }
        return byteCipher;
    }

    @Override
    public byte[] decryptFile(byte[] fileArray, String key) {
        byte[] byteFile = new byte[fileArray.length];
        int[] keyArr = transformKey(key);

        int lastBlockFix = 0; //когда неидеальные блоки
        for (int blockNumber = 0; blockNumber <= (fileArray.length / key.length()); blockNumber++) {
            for (int enOffset = 0; enOffset < key.length(); enOffset++) {
                if (key.length() * blockNumber + keyArr[enOffset] >= fileArray.length) {
                    lastBlockFix++;
                    continue;
                }
                if (key.length() * blockNumber + enOffset - lastBlockFix >= fileArray.length) {
                    break;
                }
                byteFile[key.length() * blockNumber + keyArr[enOffset]] = fileArray[key.length() * blockNumber + enOffset - lastBlockFix];
            }
        }

        return byteFile;
    }

    private int[] transformKey(String key) {
        char[] keyArr = key.toLowerCase().toCharArray();
        int[] posArr = new int[keyArr.length];
        int rangForKey = keyArr.length - 1;
        while (rangForKey >= 0) {
            int max = -1;
            int maxPos = -1;
            for (int i = 0; i < keyArr.length; i++) {
                if (max <= (int) keyArr[i]) {
                    max = keyArr[i];
                    maxPos = i;
                }
            }
            keyArr[maxPos] = ' ';
            posArr[maxPos] = rangForKey--;
        }
        return posArr;
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
