//package javaapplication2;
//
//import crypts.CryptTypes;
//import interfaces.EncryptedText;
//import java.io.FileNotFoundException;
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javafx.application.Application;
//import static javafx.application.Application.launch;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.stage.Stage;
//import javax.crypto.NoSuchPaddingException;
//
//public class JavaApplication2 extends Application {
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("cryptFXML.fxml"));
//        Scene scene = new Scene(root);
//        stage.setTitle("Debigulator FX 1.0");
//        //stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("com/huguesjohnson/debigulatorfx/res/application-x-executable.png")));
//        stage.setScene(scene);
//        stage.show();
//    }
//
////    public static void main(String[] args) {
////        launch(args);
////        try {
////            Crypt c = new Crypt();
////            c.encryptFile(CryptTypes.CESAR, "src/res/ab.jpg", "thysaa", true);
////            c.decryptFile(CryptTypes.CESAR, "src/res/ab.jpg", "thysaa");
////            EncryptedText s = c.encryptText(CryptTypes.RSA, "work is dne!", "bomba");
////            String d = c.decryptText(CryptTypes.RSA, s, "bomba");
////            System.out.println(d);
////        } catch (NoSuchAlgorithmException | NoSuchPaddingException | FileNotFoundException ex) {
////            Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
////        }
////    }
//}
