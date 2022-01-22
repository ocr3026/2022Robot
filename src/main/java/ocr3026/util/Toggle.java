package ocr3026.util;

public class Toggle {
    public boolean value = false;

    public Toggle() {}

    public  Toggle(boolean startingValue) {
        value = startingValue;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean newvalue) {
        value = newvalue;
    }

    public boolean toggleValue() {
        value = !value;
        return value;
    }
}
