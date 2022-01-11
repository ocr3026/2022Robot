package ocr3026.util;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;



public class Limelight {
    public enum ledMode{
        PIPELINE, OFF, BLINK, ON
    }

    public enum camMode {
        VISION, DRIVER
    }

    private NetworkTable limelight;

    public Limelight() {
        limelight = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public Limelight(String tableName) {
        limelight = NetworkTableInstance.getDefault().getTable(tableName);
    }

    public boolean checkForTarget() {
        return limelight.getEntry("tv").getBoolean(false);
    }

    public double getTargetX() {
        return limelight.getEntry("tx").getDouble(0);
    }

    public double getTargetY() {
        return limelight.getEntry("ty").getDouble(0);
    }

    public double getTargetArea() {
        return limelight.getEntry("ta").getDouble(0);
    }

    public double getTargetSkew() {
        return limelight.getEntry("ts").getDouble(0);
    }

    public double getLimelightLatency() {
        return limelight.getEntry("tl").getDouble(0);
    }

    public short getPipeline() {
        return limelight.getEntry("getpipe").getNumber(0).shortValue();
    }

    public void setLedMode(ledMode mode) {
        limelight.getEntry("ledMode").setNumber(mode.ordinal());
    }

    public void setCamMode(camMode mode) {
        limelight.getEntry("camMode").setNumber(mode.ordinal());
    }

    public void setPipeline(short pipeline) {
        limelight.getEntry("pipeline").setNumber(pipeline);
    }
}
