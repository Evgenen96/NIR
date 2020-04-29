
package crypter.gui.main;

import crypter.gui.decryptsettings.DecryptController;
import crypter.gui.encryptsettings.EncryptController;
import crypter.gui.files.CryptController;

public class Controllers {
    private static CryptController cryptController;
    private static DecryptController decryptController;
    private static EncryptController encryptController;

    public static CryptController getCryptController() {
        return cryptController;
    }

    public static void setCryptController(CryptController cryptController) {
        Controllers.cryptController = cryptController;
    }

    public static DecryptController getDecryptController() {
        return decryptController;
    }

    public static void setDecryptController(DecryptController decryptController) {
        Controllers.decryptController = decryptController;
    }

    public static EncryptController getEncryptController() {
        return encryptController;
    }

    public static void setEncryptController(EncryptController encryptController) {
        Controllers.encryptController = encryptController;
    }
    
    
}
