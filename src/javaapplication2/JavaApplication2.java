
package javaapplication2;

import crypts.CesarCrypt;
import crypts.CodewordCrypt;
import crypts.SimpleCrypt;
import crypts.VernameCrypt;

public class JavaApplication2 {
    public static void main(String[] args) {
        char p = 'H';

       // char n = (char)(p^s);
       // CodewordCrypt cc1 = new CodewordCrypt();
       // VernameCrypt cc2 = new VernameCrypt();
      // SimpleCrypt cc2 = new SimpleCrypt();

       CesarCrypt cc = new CesarCrypt();
        cc.decrypt(cc.encrypt("Абрамов Илья Сергеевич", "Дядина"),"Дядина");
        //cc2.decrypt(cc2.encrypt("WorkIsDonedgtr", "3124"),"3124");
        //cc1.decrypt("dceh", "drow");  
        
    }
    
}