package interfaces;

//некоторые методы шифруют в байт-код, другие в строку
import java.util.Arrays;

//данный класс служит для работы с шифрованным текстом 
public class EncryptedText {

    private byte[] byteText;
    private String text;

    public EncryptedText() {
        byteText = null;
        text = null;
    }

    public EncryptedText(String text) {
        this.text = text;
        byteText = text.getBytes();
    }

    public void setText(String text) {
        this.text = text;
        this.byteText = null;
    }

    public void setText(byte[] byteText) {
        this.text = null;
        this.byteText = Arrays.copyOf(byteText, byteText.length);
    }

    public byte[] getByteText() {
        return byteText;
    }

    public String getText() {
        return text;
    }
}
