package crypter.gui.settings;

public class Settings {

    private static boolean SHOW_DECRYPT_CONFIRMATION_DIALOG = true;
    private static boolean SHOW_ENCRYPT_CONFIRMATION_DIALOG = true;
    private static boolean CLEAR_SELECTED_FILES_LIST_AFTER_WORK = false;
    private static boolean CHANGE_FILE_EXTENSION_AFTER_WORK = true;

    public static void setOption(int option, boolean value) {
        switch (option) {
            case 1: {
                SHOW_DECRYPT_CONFIRMATION_DIALOG = value;
                break;
            }
            case 2: {
                SHOW_ENCRYPT_CONFIRMATION_DIALOG = value;
                break;
            }
            case 3: {
                CLEAR_SELECTED_FILES_LIST_AFTER_WORK = value;
                break;
            }
            case 4: {
                CHANGE_FILE_EXTENSION_AFTER_WORK = value;
                break;
            }
        }
    }

    public static int SHOW_DECRYPT_CONFIRMATION_DIALOG() {
        return 1;
    }

    public static int SHOW_ENCRYPT_CONFIRMATION_DIALOG() {
        return 2;
    }

    public static int CLEAR_SELECTED_FILES_LIST_AFTER_WORK() {
        return 3;
    }

    public static int CHANGE_FILE_EXTENSION_AFTER_WORK() {
        return 4;
    }

    public static boolean isSHOW_DECRYPT_CONFIRMATION_DIALOG() {
        return SHOW_DECRYPT_CONFIRMATION_DIALOG;
    }

    public static boolean isSHOW_ENCRYPT_CONFIRMATION_DIALOG() {
        return SHOW_ENCRYPT_CONFIRMATION_DIALOG;
    }

    public static boolean isCLEAR_SELECTED_FILES_LIST_AFTER_WORK() {
        return CLEAR_SELECTED_FILES_LIST_AFTER_WORK;
    }

    public static boolean isCHANGE_FILE_EXTENSION_AFTER_WORK() {
        return CHANGE_FILE_EXTENSION_AFTER_WORK;
    }

}
