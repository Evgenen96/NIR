package crypts;

import interfaces.EncryptedText;
import interfaces.Encryption;

public class SimpleCrypt implements Encryption {

    
    private final CryptTypes CRYPTID = CryptTypes.SIMPLE;
    
    @Override
    public EncryptedText encrypt(String plainText, String key) {
        EncryptedText eText = new EncryptedText();
        String enText = "";
        String temp = "";
        int[] keyArr = new int[key.length()];

        for (int i = 0; i < keyArr.length; i++) {
            temp += key.charAt(i);
            keyArr[i] = Integer.valueOf(temp) - 1;
            temp = "";
        }

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
        String temp = "";
        int[] keyArr = new int[key.length()];

        for (int i = 0; i < keyArr.length; i++) {
            temp += key.charAt(i);
            keyArr[i] = Integer.valueOf(temp) - 1;
            temp = "";
        }

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
        String temp = "";
        int[] keyArr = new int[key.length()];

        for (int i = 0; i < keyArr.length; i++) {
            temp += key.charAt(i);
            keyArr[i] = Integer.valueOf(temp) - 1;
            temp = "";
        }

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
        String temp = "";
        int[] keyArr = new int[key.length()];

        for (int i = 0; i < keyArr.length; i++) {
            temp += key.charAt(i);
            keyArr[i] = Integer.valueOf(temp) - 1;
            temp = "";
        }

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
    
    @Override
    public CryptTypes getCryptID() {
        return CRYPTID;
    }
}
