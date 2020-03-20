package crypts;

import interfaces.EncryptedText;
import interfaces.Encryption;

public class CesarCrypt implements Encryption {

    @Override
    public EncryptedText encrypt(String plainText, String key) {
        EncryptedText eText = new EncryptedText();
        String enText = "";
        int[] arrange = getColArrange(key);
        for (int row = 0; row < key.length(); row++) {
            for (int col = 0; col <= plainText.length() / key.length(); col++) {
                if (col * key.length() + arrange[row] % key.length() >= plainText.length()) {
                    enText = enText + " ";
                } else {
                    enText = enText + plainText.charAt(col * key.length() + arrange[row] % key.length());
                }
            }
        }
        eText.setText(enText);
        return eText;
    }

    @Override
    public String decrypt(EncryptedText eText, String key) {
        String deText = "";
        String enText = eText.getText();
        int[] arrange = getColArrange(key);
        int[] newArrange = new int[arrange.length];
        for (int i = 0; i < arrange.length; i++) {
            newArrange[arrange[i]] = i;
        }
        for (int row = 0; row < enText.length() / key.length(); row++) {
            for (int col = 0; col < key.length(); col++) {
                if (newArrange[col] * enText.length() / key.length() + row % enText.length() / key.length() >= enText.length()) {

                } else {
                    deText = deText + enText.charAt(newArrange[col] * enText.length() / key.length() + row % (enText.length() / key.length()));
                }
            }
        }
        return deText;
    }

    @Override
    public byte[] encryptFile(byte[] fileArray, String key) {
        byte[] enFile = new byte[key.length() * (1 + fileArray.length / key.length())];
        int[] arrange = getColArrange(key);
        int pos = 0;
        for (int row = 0; row < key.length(); row++) {
            for (int col = 0; col <= fileArray.length / key.length(); col++) {
                if (col * key.length() + arrange[row] % key.length() >= fileArray.length) {
                    pos++;
                } else {
                    enFile[pos++] = fileArray[col * key.length() + arrange[row] % key.length()];
                }
            }
        }
        return enFile;
    }

    @Override
    public byte[] decryptFile(byte[] fileArray, String key) {
        byte[] deFile = new byte[fileArray.length];
        int[] arrange = getColArrange(key);
        int[] newArrange = new int[arrange.length];
        for (int i = 0; i < arrange.length; i++) {
            newArrange[arrange[i]] = i;
        }
        int pos = 0;
        for (int row = 0; row < fileArray.length / key.length(); row++) {
            for (int col = 0; col < key.length(); col++) {
                if (newArrange[col] * fileArray.length / key.length() + row % fileArray.length / key.length() >= fileArray.length) {
                    pos++;
                } else {
                    deFile[pos++] = fileArray[(newArrange[col] * fileArray.length / key.length() + row % (fileArray.length / key.length()))];
                }
            }
        }

        return deFile;
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
