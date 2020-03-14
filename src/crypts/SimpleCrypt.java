package crypts;

import interfaces.Encryption;       

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

        System.out.println(cipherText);
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
        System.out.println(plainText);
        return plainText;
    }

}
