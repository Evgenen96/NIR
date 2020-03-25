package crypter.gui.settings;

public class Settings {

    private static boolean SHOW_DECRYPT_CONFIRMATION_DIALOG = true;
    private static boolean SHOW_ENCRYPT_CONFIRMATION_DIALOG = true;

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
        }
    }

    public static int SHOW_DECRYPT_CONFIRMATION_DIALOG() {
        return 1;
    }

    public static int SHOW_ENCRYPT_CONFIRMATION_DIALOG() {
        return 2;
    }

    public static boolean isSHOW_DECRYPT_CONFIRMATION_DIALOG() {
        return SHOW_DECRYPT_CONFIRMATION_DIALOG;
    }

    public static boolean isSHOW_ENCRYPT_CONFIRMATION_DIALOG() {
        return SHOW_ENCRYPT_CONFIRMATION_DIALOG;
    }

}
