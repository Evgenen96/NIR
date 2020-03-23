package crypter.crypt;

import crypter.crypt.Crypt;
import crypter.crypt.helpers.CryptTypes;
import crypter.crypt.ciphers.VernameCrypt;
import crypter.crypt.helpers.EncryptedText;
import java.io.BufferedReader;
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

//класс работы с метаданными шифрования файлов
public class FileMeta {

    private final static String MARK = "ciphered";
    private final static String INDEX_MARK = "key";
    private final static String SECRET_PATH = "src/res/pas.txt";
    private static int IDcounter = 0;
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
    private final static VernameCrypt SECRET_CRYPT = new VernameCrypt();
    private final static String SECRET_KEY = "2";

    //поля метки
    private Date date;
    private int fileID;
    private boolean cipherSave;
    private int markLength;

    public FileMeta() {
        cipherSave = false;
        markLength = 0;
    }

    public FileMeta(boolean cipherSave) {
        extractSecretKey();
        this.cipherSave = cipherSave;
        if (cipherSave) {
            fileID = IDcounter++;
        }
        markLength = 0;
    }

    //сбор метки и вставка их в файл
    public byte[] addMark(byte[] fileArray) {
        String day = String.valueOf(LocalDateTime.now().getDayOfMonth());
        String month = String.valueOf((LocalDateTime.now().getMonth().getValue()));
        String year = String.valueOf(LocalDateTime.now().getYear());
        String hour = String.valueOf(LocalDateTime.now().getHour());
        String minute = String.valueOf(LocalDateTime.now().getMinute());
        String second = String.valueOf(LocalDateTime.now().getSecond());
        byte[] byteMark;
        String stringMark;

        //если нужно сохранить информацию о шифровании
        if (cipherSave) {
            stringMark = day + "_" + month + "_" + year + "_" + hour + "_" + minute + "_" + second
                    + "_" + INDEX_MARK + String.valueOf(fileID);
            stringMark = MARK + String.valueOf(stringMark.length()) + stringMark;
            byteMark = stringMark.getBytes();
        } else {
            stringMark = day + "_" + month + "_" + year + "_" + hour + "_" + minute + "_" + second;
            stringMark = MARK + String.valueOf(stringMark.length()) + stringMark;
            byteMark = stringMark.getBytes();
        }

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

            //если в файле сохранен шифр
            if (fullMark.contains(INDEX_MARK)) {
                cipherSave = true;
                String dateString = fullMark.substring(0, fullMark.indexOf('k') - 1);
                String indexFile = fullMark.substring(fullMark.indexOf('y') + 1, fullMark.length());
                fileID = Integer.valueOf(indexFile);
                try {
                    date = DATE_FORMAT.parse(dateString);
                } catch (ParseException ex) {
                    Logger.getLogger(FileMeta.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                cipherSave = false;
                try {
                    date = DATE_FORMAT.parse(fullMark);
                } catch (ParseException ex) {
                    Logger.getLogger(FileMeta.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    //извлечение данных из секретного файла
    public String[] extractSecretKey() {
        try {
            int idLength = String.valueOf(fileID).length();
            BufferedReader br = new BufferedReader(new FileReader(SECRET_PATH));
            String line = br.readLine();
            String prevLine = null;
            line = br.readLine();
            while (line != null) {
                System.out.println(line);
                if (line.substring(0, idLength).equals(String.valueOf(fileID))) {
                    break;
                }
                prevLine = line;
                line = br.readLine();
            }
            br.close();
            String[] data;
            if (IDcounter == 0) {
                data = prevLine.split(";;;");
                IDcounter = Integer.valueOf(data[0]) + 1;
                System.out.println(IDcounter);
            } else if (line != null) {
                data = line.split(";;;");
            } else {
                return null;
            }
            //расшифровка ключа
            data[2] = SECRET_CRYPT.decrypt(new EncryptedText(data[2]), SECRET_KEY);
            return data;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //сохранения ключа шифрования в секретный файл
    public void saveSecretKey(CryptTypes type, String key) {
        try {
            FileWriter fos;
            fos = new FileWriter(SECRET_PATH, true);
            fos.write("\n" + this.fileID + ";;;" + type + ";;;"
                    + new String(SECRET_CRYPT.encrypt(key, SECRET_KEY).getByteText()));
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean isCipherSaved() {
        return cipherSave;
    }

}
