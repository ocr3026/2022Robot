package ocr3026.util;

import ocr3026.util.Limelight;
import ocr3026.util.Limelight.camMode;
import ocr3026.util.Limelight.ledMode;

public class Vision {
    private Limelight limelight = new Limelight();

    private boolean visionOn = true;

    public Vision() {
        limelight.setCamMode(camMode.VISION);
        limelight.setLedMode(ledMode.PIPELINE);
    }

    public void setDriverMode() {
        
    }
}
