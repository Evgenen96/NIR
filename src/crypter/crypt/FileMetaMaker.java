package crypter.crypt;

import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.ciphers.VernameCrypt;
import crypter.crypt.hashfucntion.HashFunction;
import crypter.crypt.helpers.EncryptedText;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

//класс работы с метаданными шифрования файлов
public class FileMetaMaker {

    private final static String MARK = "ciphered";
    private final static String INDEX_MARK = "key";
    private final static String SECRET_PATH = "src/res/pas.txt";
    private static int IDcounter = 0;
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
    public final static String CIPHERED_FILE_FORMAT = ".crpt";
    private final static int DEFAULT_ID = 10000;

    //поля метки
    private Date date;
    private int fileID;
    private int markLength;
    private String fileExtension;

    public FileMetaMaker() {
        markLength = 0;
        fileID = IDcounter;
    }

    public static void initalize() {
        FileMetaMaker temp = new FileMetaMaker();
        temp.checkSecretKey(null);
    }

    //сбор метки и вставка их в файл
    public byte[] addMark(byte[] fileArray, String filePath) {
        String day = String.valueOf(LocalDateTime.now().getDayOfMonth());
        String month = String.valueOf((LocalDateTime.now().getMonth().getValue()));
        String year = String.valueOf(LocalDateTime.now().getYear());
        String hour = String.valueOf(LocalDateTime.now().getHour());
        String minute = String.valueOf(LocalDateTime.now().getMinute());
        String second = String.valueOf(LocalDateTime.now().getSecond());
        fileExtension = filePath.substring(filePath.lastIndexOf('.'), filePath.length());
        byte[] byteMark;
        String stringMark;

        //сохранить информацию о шифровании
        stringMark = fileExtension + ";;" + day + "_" + month + "_" + year + "_" + hour + "_" + minute + "_" + second
                + "_" + INDEX_MARK + String.valueOf(fileID);
        stringMark = MARK + String.valueOf(stringMark.length()) + stringMark;
        byteMark = stringMark.getBytes();

        //запись метки в байт массив файла
        byte[] markedEnFile = new byte[fileArray.length + byteMark.length];
        for (int i = 0; i < byteMark.length; i++) {
            markedEnFile[i] = byteMark[i];
        }
        for (int i = 0; i < fileArray.length; i++) {
            markedEnFile[i + byteMark.length] = fileArray[i];
        }
        return markedEnFile;
    }

    //извлечение метаданных из байт массива файла
    public boolean readMark(byte[] fileArray) {
        if (new String(Arrays.copyOf(fileArray, MARK.length())).contains(MARK)) {
            markLength = Integer.valueOf(new String(fileArray, MARK.length(), 2));
            String fullMark = new String(Arrays.copyOfRange(fileArray, MARK.length() + 2, MARK.length() + 2 + markLength));

            int separator = fullMark.indexOf(";;");
            fileExtension = fullMark.substring(0, separator);
            String dateString = fullMark.substring(separator, fullMark.lastIndexOf('k') - 1);
            String indexFile = fullMark.substring(fullMark.lastIndexOf('y') + 1, fullMark.length());
            fileID = Integer.valueOf(indexFile);
            try {
                date = DATE_FORMAT.parse(dateString);
            } catch (ParseException ex) {
            }
            return true;
        } else {
            return false;
        }
    }

    //извлечение данных из секретного файла
    public boolean checkSecretKey(String key) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(SECRET_PATH));
            String line = null;
            String prevLine = null;
            line = br.readLine();
            while (line != null) {
                if (line.substring(0, line.indexOf(";")).equals(String.valueOf(fileID))) {
                    break;
                }
                prevLine = line;
                line = br.readLine();
            }
            br.close();
            String[] data = null;
            //для инициализации ID
            if (IDcounter == 0) {
                if (prevLine == null) {
                    IDcounter = DEFAULT_ID;
                    System.out.println(IDcounter);
                    return false;
                } else {
                    data = prevLine.split(";;;");
                    IDcounter = Integer.valueOf(data[0]) + 1;
                    return false;
                }
            } else if (line != null) {
                data = line.split(";;;");
            } else {
                //если не нашел такого ID в секретном файле
                return true;
            }
            //проверка ключа
            if (data[2].equals(HashFunction.getHexHash(key))) {
                return true;
            } else {
                return false;
            }

        } catch (PatternSyntaxException | IOException ex) {
            System.out.println("Ошибка при чтении секретного файла");
        }
        return false;
    }

    //сохранения ключа шифрования в секретный файл
    public void saveSecretKey(CryptTypes type, String key) {
        try {
            FileWriter fos;
            fos = new FileWriter(SECRET_PATH, true);
            fos.write(this.fileID + ";;;" + type + ";;;"
                    + HashFunction.getHexHash(key) + "\n");
            fos.close();
            IDcounter++;
        } catch (IOException ex) {
            ///777 создание нового файла при потере старого
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String changeExtension(String filePath) {
        String fileExt = filePath.substring(filePath.lastIndexOf('.'), filePath.length());
        if (fileExt.equals(CIPHERED_FILE_FORMAT)) {
            File file1 = new File(filePath);
            String decryptedFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + fileExtension;
            File file2 = new File(decryptedFilePath);
            file1.renameTo(file2);
            return decryptedFilePath;
        } else {
            File file1 = new File(filePath);
            String encryptedFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + CIPHERED_FILE_FORMAT;
            File file2 = new File(encryptedFilePath);
            file1.renameTo(file2);
            return encryptedFilePath;
        }
    }

    public int getFileID() {
        return fileID;
    }

    public Date getDate() {
        return date;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public int getMarkLength() {
        return markLength;
    }

}
