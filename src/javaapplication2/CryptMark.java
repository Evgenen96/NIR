package javaapplication2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CryptMark {

    private final String MARK = "ciphered";
    private final String INDEX_MARK = "key";
    private int fileID = -1000;
    private Date date;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
    private int markLength = 0;

    public CryptMark() {
    }

    public byte[] getByteMark(byte[] fileArray) {
        byte[] byteMark;
        String day = String.valueOf(LocalDateTime.now().getDayOfMonth());
        String month = String.valueOf((LocalDateTime.now().getMonth().getValue()));
        String year = String.valueOf(LocalDateTime.now().getYear());
        String hour = String.valueOf(LocalDateTime.now().getHour());
        String minute = String.valueOf(LocalDateTime.now().getMinute());
        String second = String.valueOf(LocalDateTime.now().getSecond());
        String fullMark;

        if (fileID == -1000) {
            fullMark = day + "_" + month + "_" + year + "_" + hour + "_" + minute + "_" + second;
            fullMark = MARK + String.valueOf(fullMark.length()) + fullMark;
            byteMark = fullMark.getBytes();
        } else {
            fullMark = day + "_" + month + "_" + year + "_" + hour + "_" + minute + "_" + second
                    + "_" + INDEX_MARK + String.valueOf(fileID);
            fullMark = MARK + String.valueOf(fullMark.length()) + fullMark;
            byteMark = fullMark.getBytes();
        }
        byte[] markedEnFile = new byte[fileArray.length + byteMark.length];
        for (int i = 0; i < byteMark.length; i++) {
            markedEnFile[i] = byteMark[i];
        }
        for (int i = 0; i < fileArray.length; i++) {
            markedEnFile[i + byteMark.length] = fileArray[i];
        }
        return markedEnFile;
    }

    public boolean readMark(byte[] fileArray) {
        if (new String(Arrays.copyOf(fileArray, MARK.length())).contains(MARK)) {
            markLength = Integer.valueOf(new String(fileArray, MARK.length(), 2));
            String fullMark = new String(Arrays.copyOfRange(fileArray, MARK.length() + 2, MARK.length() + 2 + markLength));
            if (fullMark.contains(INDEX_MARK)) {
                String dateString = fullMark.substring(0, fullMark.indexOf('k') - 1);
                String indexFile = fullMark.substring(fullMark.indexOf('y') + 1, fullMark.length());
                fileID = Integer.valueOf(indexFile);
                try {
                    date = dateFormat.parse(dateString);
                } catch (ParseException ex) {
                    Logger.getLogger(CryptMark.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    date = dateFormat.parse(fullMark);
                } catch (ParseException ex) {
                    Logger.getLogger(CryptMark.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return true;
        } else {
            return false;
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
