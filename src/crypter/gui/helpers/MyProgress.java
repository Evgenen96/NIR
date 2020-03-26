package crypter.gui.helpers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MyProgress {

    private static DoubleProperty number;

    public final static double getNumber() {
        if (number != null) {
            return number.get();
        } else {
            return 0;
        }
    }

    public final static void setNumber(double inumber) {
        if (number == null) {
            number = new SimpleDoubleProperty(0);
        }
        number.set(inumber);
    }

    public final static DoubleProperty numberProperty() {
        if (number == null) {
            number = new SimpleDoubleProperty(0);
        }
        return number;
    }

}
