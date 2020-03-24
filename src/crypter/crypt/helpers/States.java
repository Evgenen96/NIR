package crypter.crypt.helpers;

public enum States {
    NO_FILE,
    NO_MARK,
    WRONG_KEY,
    SUCCESS_DEC,
    NORMAL,
    SUCCESS_ENC;

    private static final String[] TYPES = {
        "Файл занят другой программой или был перемещен",
        "Данный файл не был зашифрован",
        "Ключ щифрования введен неверно",
        "Успешно",
        "Default",
        "Успешно"};

    public static String getName(States type) {
        return TYPES[type.ordinal()];
    }

    public static States getErrorType(String type) {
        for (int i = 0; i < TYPES.length; i++) {
            if (type.equals(TYPES[i])) {
                return States.values()[i];
            }
        }
        return null;
    }

    public String getDescription() {
        return States.getName(this);
    }

}
