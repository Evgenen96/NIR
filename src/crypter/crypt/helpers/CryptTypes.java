package crypter.crypt.helpers;

public enum CryptTypes {
    CODEWORD,
    GAMMA,
    RSA,
    SIMPLE,
    VERNAME,
    VERTICAL;
    private static final String[] TYPES = {"Слоган", "Гаммирование", "RSA",
        "Простая перестановка", "Шифр Вернама", "Вертикальная перестановка"};

    public static String getName(CryptTypes type) {
        return TYPES[type.ordinal()];
    }

    public static CryptTypes getCryptType(String type) {
        for (int i = 0; i < TYPES.length; i++) {
            if (type.equals(TYPES[i])) {
                return CryptTypes.values()[i];
            }
        }
        return null;
    }
 
    
    public String getName() {
        return CryptTypes.getName(this);
    }
    
    

}
