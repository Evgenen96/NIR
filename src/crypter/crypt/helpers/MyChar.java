
package crypter.crypt.helpers;

//класс модификация примитивного типа char
public class MyChar {
    public char symb;

    public MyChar(char symb) {
        this.symb = symb;
    }
    
    //порядковый номер буквы в алфавите
    public int getAlphInt() {
        //если латиница
        if (this.symb > (int)'A' && this.symb < (int)'Z') {
            return (int)this.symb - (int)'A';
        }
        if (this.symb > (int)'a' && this.symb < (int)'z') {
            return (int)this.symb - (int)'a';
        }
        //кириллица
        if (this.symb > (int)'А' && this.symb < (int)'Я') {
            return (int)this.symb - (int)'А';
        }
        if (this.symb > (int)'а' && this.symb < (int)'я') {
            return (int)this.symb - (int)'а';
        }
        return -1;
    }
    
    public int whatLangauge() {
        if (this.symb > (int)'A' && this.symb < (int)'Z') {
            return 1;
        }
        if (this.symb > (int)'a' && this.symb < (int)'z') {
            return 1;
        }
         if (this.symb > (int)'А' && this.symb < (int)'Я') {
            return 2;
        }
        if (this.symb > (int)'а' && this.symb < (int)'я') {
            return 2;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "symb=" + symb;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.symb;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MyChar other = (MyChar) obj;
        if (this.symb != other.symb) {
            return false;
        }
        return true;
    }

    public char getSymb() {
        return symb;
    }
    
    
    
    
    
    
}
