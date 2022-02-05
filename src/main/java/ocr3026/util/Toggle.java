package ocr3026.util;

public class Toggle {
    private boolean on = false;

    public Toggle() {}

    public Toggle(boolean startingValue) {
        on = startingValue;
    }

    public boolean isOn() {
        return on;
    }

    public void setToggle(boolean newValue) {
        on = newValue;
    }

    //if on, turn off and if off, turn on
    public boolean swap() {
        on = !on;
        return on;
    }
}
