
package javaapplication2;

import java.util.ArrayList;
import java.util.Collection;

public class CharArray extends ArrayList {

    public CharArray() {
        super();
    }

    
    public CharArray(Collection c) {
        super(c);
    }
    
    public int getIndexOfChar(char ch) {
        for (int i = 0; i < this.size(); i++) {
            if (ch == (char)this.get(i)) {
                return i;
            }
        }
        return -1;
    }
}
