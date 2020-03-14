package crypts;

import interfaces.Encryption;


public class CesarCrypt implements Encryption {


    public CesarCrypt() {
    }

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
        System.out.println(cipherText);
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
        System.out.println(plainText);
        return plainText;
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
            keyArr[maxPos] = (char)(0);
            posArr[maxPos] = rangForKey--;
        }
        int[] newPosArr = new int[posArr.length];
        for (int i = 0; i < posArr.length; i++) {
            newPosArr[posArr[i]] = i;
        }
        for (int i = 0; i < newPosArr.length; i++) {
            System.out.println(newPosArr[i]);
            
        }
        return newPosArr;
    }
}
