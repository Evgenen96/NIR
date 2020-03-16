package crypts;

import interfaces.Encryption;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleCrypt implements Encryption {

    @Override
    public String encrypt(String plainText, String key) {
        String cipherText = "";
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
                cipherText = cipherText + plainText.charAt(keyArr.length * blockNumber + keyArr[offset]);
            }

        }
        return cipherText;
    }

    @Override
    public String decrypt(String cipherText, String key) {
        String plainText = "";
        String temp = "";
        int[] keyArr = new int[key.length()];

        for (int i = 0; i < keyArr.length; i++) {
            temp += key.charAt(i);
            keyArr[i] = Integer.valueOf(temp) - 1;
            temp = "";
        }

        char[] newLineArr = new char[cipherText.length()];
        int lastBlockFix = 0; //когда неидеальные блоки
        for (int blockNumber = 0; blockNumber <= cipherText.length() / key.length(); blockNumber++) {
            for (int enOffset = 0; enOffset < key.length(); enOffset++) {
                if (key.length() * blockNumber + keyArr[enOffset] >= cipherText.length()) {
                    lastBlockFix++;
                    continue;
                }
                if (key.length() * blockNumber + enOffset - lastBlockFix >= cipherText.length()) {
                    break;
                }
                newLineArr[key.length() * blockNumber + keyArr[enOffset]] = cipherText.charAt(key.length() * blockNumber + enOffset - lastBlockFix);
            }
        }
        plainText = String.valueOf(newLineArr);
        return plainText;
    }

    public void encryptFile(String fileName, String key) throws IOException {
        byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
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
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(byteCipher);
        fos.close();
    }

    public void decryptFile(String fileName, String key) throws IOException {
        byte[] fileArray = Files.readAllBytes(Paths.get(fileName));
        byte[] byteCipher = new byte[fileArray.length];
        String temp = "";
        int[] keyArr = new int[key.length()];

        for (int i = 0; i < keyArr.length; i++) {
            temp += key.charAt(i);
            keyArr[i] = Integer.valueOf(temp) - 1;
            temp = "";
        }

        int lastBlockFix = 0; //когда неидеальные блоки
        for (int blockNumber = 0; blockNumber <= fileArray.length / key.length(); blockNumber++) {
            for (int enOffset = 0; enOffset < key.length(); enOffset++) {
                if (key.length() * blockNumber + keyArr[enOffset] >= fileArray.length) {
                    lastBlockFix++;
                    continue;
                }
                if (key.length() * blockNumber + enOffset - lastBlockFix >= fileArray.length) {
                    break;
                }
                byteCipher[key.length() * blockNumber + keyArr[enOffset]] = fileArray[key.length() * blockNumber + enOffset - lastBlockFix];
            }
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(byteCipher);
        fos.close();
    }
}
