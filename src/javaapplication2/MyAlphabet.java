package javaapplication2;

//класс-алфавит
//синглтон???
import java.util.ArrayList;

public class MyAlphabet {

    public ArrayList<MyChar> alph;

    public MyAlphabet() {
        alph = new ArrayList<>();
        char firstSymb = 'A';
        do {
            alph.add(new MyChar(firstSymb));
        } while (firstSymb++ != 'Z');
        firstSymb = 'a';
        do {
            alph.add(new MyChar(firstSymb));
        } while (firstSymb++ != 'z');
        firstSymb = 'А';
        do {
            alph.add(new MyChar(firstSymb));
        } while (firstSymb++ != 'Я');
        firstSymb = 'а';
        do {
            alph.add(new MyChar(firstSymb));
        } while (firstSymb++ != 'я');
        firstSymb = '0';
        do {
            alph.add(new MyChar(firstSymb));
        } while (firstSymb++ != '9');
    }

    public void insertCodeword(String codeword) {
        for (int i = 0; i < codeword.length(); i++) {
            alph.remove(new MyChar(codeword.charAt(i)));
            alph.add(i, new MyChar(codeword.charAt(i)));
        }
    }

    public MyAlphabet(MyAlphabet a) {
        this.alph = a.alph;
    }

    public int getInt(char symb) {
        return alph.indexOf(new MyChar(symb));
    }

    public char getChar(int index) {
        return alph.get(index).getSymb();
    }

}
