package crypts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GammaCrypt {

    public byte[] encrypt(String plainText, String key) {
        RandomLemer R = new RandomLemer(Integer.valueOf(key));
        byte[] arr = plainText.getBytes();
        byte[] cipherText = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            cipherText[i] = (byte) (arr[i] ^ R.next());
        }
        return cipherText;
    }

    public String decrypt(byte[] cipherText, String key) {
        RandomLemer R = new RandomLemer(Integer.valueOf(key));
        byte[] plainText = new byte[cipherText.length];
        for (int i = 0; i < cipherText.length; i++) {
            plainText[i] = (byte) (cipherText[i] ^ R.next());
        }
        System.out.println(new String(plainText));
        return new String(plainText);
    }

    public byte[] encryptFile(byte[] fileArray, String key) {
        RandomLemer R = new RandomLemer(Integer.valueOf(key));
        byte[] byteCipher = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteCipher[i] = (byte) (fileArray[i] ^ R.next());
        }
        return byteCipher;
    }

    public byte[] decryptFile(byte[] fileArray, String key) {
        RandomLemer R = new RandomLemer(Integer.valueOf(key));
        byte[] byteFile = new byte[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            byteFile[i] = (byte) (fileArray[i] ^ R.next());
        }
        return byteFile;
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
