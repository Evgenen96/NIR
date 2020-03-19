package crypts;

import interfaces.Encryption;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CesarCrypt implements Encryption {

    @Override
    public String encrypt(String plainText, String key) {
        String cipherText = "";
        int[] arrange = getColArrange(key);
        for (int row = 0; row < key.length(); row++) {
            for (int col = 0; col <= plainText.length() / key.length(); col++) {
                if (col * key.length() + arrange[row] % key.length() >= plainText.length()) {
                    cipherText = cipherText + " ";
                } else {
                    cipherText = cipherText + plainText.charAt(col * key.length() + arrange[row] % key.length());
                }
            }
        }
        return cipherText;
    }

    @Override
    public String decrypt(String cipherText, String key) {
        String plainText = "";
        int[] arrange = getColArrange(key);
        int[] newArrange = new int[arrange.length];
        for (int i = 0; i < arrange.length; i++) {
            newArrange[arrange[i]] = i;
        }
        for (int row = 0; row < cipherText.length() / key.length(); row++) {
            for (int col = 0; col < key.length(); col++) {
                if (newArrange[col] * cipherText.length() / key.length() + row % cipherText.length() / key.length() >= cipherText.length()) {

                } else {
                    plainText = plainText + cipherText.charAt(newArrange[col] * cipherText.length() / key.length() + row % (cipherText.length() / key.length()));
                }
            }
        }
        return plainText;
    }

    public byte[] encryptFile(byte[] fileArray, String key) {
        byte[] byteCipher = new byte[fileArray.length];
        int[] arrange = getColArrange(key);
        int pos = 0;
        for (int row = 0; row < key.length(); row++) {
            for (int col = 0; col <= fileArray.length / key.length(); col++) {
                if (col * key.length() + arrange[row] % key.length() >= fileArray.length) {

                } else {
                    byteCipher[pos++] = fileArray[col * key.length() + arrange[row] % key.length()];
                }
            }
        }
        return byteCipher;
    }

    public byte[] decryptFile(byte[] fileArray, String key) {
        byte[] byteFile = new byte[fileArray.length];
        int[] arrange = getColArrange(key);
        int[] newArrange = new int[arrange.length];
        for (int i = 0; i < arrange.length; i++) {
            newArrange[arrange[i]] = i;
        }
        int pos = 0;
        for (int row = 0; row < fileArray.length / key.length(); row++) {
            for (int col = 0; col < key.length(); col++) {
                if (newArrange[col] * fileArray.length / key.length() + row % fileArray.length / key.length() >= fileArray.length) {

                } else {
                    byteFile[pos++] = fileArray[(newArrange[col] * fileArray.length / key.length() + row % (fileArray.length / key.length()))];
                }
            }
        }
        return byteFile;
    }

    private int[] getColArrange(String key) {
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
            keyArr[maxPos] = (char) (0);
            posArr[maxPos] = rangForKey--;
        }
        int[] newPosArr = new int[posArr.length];
        for (int i = 0; i < posArr.length; i++) {
            newPosArr[posArr[i]] = i;
        }
        return newPosArr;
    }
}
