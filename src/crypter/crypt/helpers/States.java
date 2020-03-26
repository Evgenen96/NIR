package crypter.crypt.helpers;

public enum States {
    NO_FILE,
    NO_MARK,
    WRONG_KEY,
    SUCCESS_DEC,
    DEFAULT,
    SUCCESS_ENC,
    PROCESSING;

    private static final String[] TYPES = {
        "Файл не доступен. \nНажмите, чтобы попробовать снова.",
        "Данный файл не был зашифрован",
        "Ключ шифрования введен неверно",
        "Успешно",
        "Убрать",
        "Успешно",
        "В процессе"};

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
