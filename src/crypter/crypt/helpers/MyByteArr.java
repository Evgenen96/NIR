
package crypter.crypt.helpers;

public class MyByteArr {
    public static void show(byte[] s) {
        for (int i = 0; i < s.length; i++) {
            byte b = s[i];
            System.out.print(b+ " ");
            
        }
        System.out.println("");
    }
}
