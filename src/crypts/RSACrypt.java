package crypts;

import interfaces.Encryption;
import java.util.Random;


public class RSACrypt implements Encryption {

    private int P;
    private int Q;
    private int N = 21;
    private long privateKey = 17;
    private long publicKey = 5;
    

    @Override
    public String encrypt(String line, String key) {
        //generateKey();
        double temp;
        String cipherText = "";
        for (int i = 0; i < line.length(); i++) {
            temp =  ( (Math.pow((double)line.charAt(i),(double)publicKey) % N));
            cipherText = cipherText + (char) temp;
        }
        System.out.println(cipherText);
        return cipherText;
    }

    @Override
    public String decrypt(String line, String key) {
        double temp;
        String cipherText = "";
        for (int i = 0; i < line.length(); i++) {
            temp =  ( (Math.pow((double)line.charAt(i),(double)privateKey) % N));
            cipherText = cipherText + (char) temp;
        }
        System.out.println(cipherText);
        return cipherText;
    }

    private void generateKey() {
        generatePQ();
        N = P * Q;
        int fN = (P - 1) * (Q - 1);
        //System.out.println(fN);
        int publicKey;
        int privateKey;
        Random r = new Random();
        
//открытый ключ
        while (true) {
            publicKey = r.nextInt(fN - 2) + 2;
            int a = fN;
            int b = publicKey;
            while ((a != 0) && (b != 0)) {
                if (a > b) {
                    a = a % b;
                } else {
                    b = b % a;
                }
            }
            if (a + b == 1) {
                break;
            }
        }
        this.publicKey = publicKey;
        System.out.println(publicKey);
        int n = 1; //ищем закрытый ключ
        while (true) {
            if ((publicKey * n) % fN == 1) {
                privateKey = n;
                break;
            } else {
                n++;
            }
        }
        this.privateKey = privateKey;
        System.out.println(privateKey);

    }

    private void generatePQ() {
        Random r = new Random();
        int current =  r.nextInt(17)+ 3;
        int div = 2;
        while (current / 2 >= div) {
            if (current % div == 0) {
                current++;
                div = 2;
            } else {
                div++;
            }
        }
        P = current;

        current = r.nextInt(17) + 3;
        
        if (P == current) {
            current++;
        }
        div = 2;
        while (current / 2 >= div) {
            if (current % div == 0) {
                current++;
                div = 2;
            } else {
                div++;
            }
        }
        Q = current;

    }

}
