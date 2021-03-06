package crypts;

import interfaces.EncryptedText;
import interfaces.Encryption;

public class GammaCrypt implements Encryption {

    private final CryptTypes CRYPTID = CryptTypes.GAMMA;

    @Override
    public EncryptedText encrypt(String plainText, String key) {
        EncryptedText eText = new EncryptedText();
        RandomLemer R = new RandomLemer(transfromKey(key));
        byte[] arr = plainText.getBytes();
        byte[] enText = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            enText[i] = (byte) (arr[i] ^ R.next());
        }
        eText.setText(enText);
        return eText;
    }

    @Override
    public String decrypt(EncryptedText eText, String key) {
        byte[] enText = eText.getByteText();
        RandomLemer R = new RandomLemer(transfromKey(key));
        byte[] deText = new byte[enText.length];
        for (int i = 0; i < enText.length; i++) {
            deText[i] = (byte) (enText[i] ^ R.next());
        }
        return new String(deText);
    }

    @Override
    public byte[] encryptFile(byte[] fileArray, String key) {
        RandomLemer R = new RandomLemer(transfromKey(key));
        byte[] byteCipher = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteCipher[i] = (byte) (fileArray[i] ^ R.next());
        }
        return byteCipher;
    }

    @Override
    public byte[] decryptFile(byte[] fileArray, String key) {
        RandomLemer R = new RandomLemer(transfromKey(key));
        byte[] byteFile = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteFile[i] = (byte) (fileArray[i] ^ R.next());
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
    
    public int transfromKey(String key){
        int intKey;
        try {
            intKey = Integer.valueOf(key);
        } catch (NumberFormatException e) {
            intKey = 0;
            for (int i = 0; i < key.length(); i++) {
                intKey += Math.abs((int)key.charAt(i));
            }
        }
        return intKey;
    }

}
//реализцаия генератора псевдослучайных чисел методом Лемера

class RandomLemer {

    private final static int A = 3, B = 2, C = 101;
    private int x;

    public RandomLemer(int key) {
        x = key;
    }

    //возвращает псевдослучайное целое число от 0 до 65535 включая
    public int next() {
        x = (A * x + B) % C;
        return x;
    }

    //возвращает псевдослучайное дробное число 0 до 1 не включая 1
    public double nextDouble() {
        return (double) next() / C;
    }

    //возвращает псевдослучайное целое число от 0 до bound не включая bound
    public int nextInt(int bound) {
        return (int) (nextDouble() * bound);
    }

}
