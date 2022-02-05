package ocr3026.util;

import ocr3026.util.Limelight;
import ocr3026.util.Limelight.camMode;
import ocr3026.util.Limelight.ledMode;
import ocr3026.util.MecanumTankDrive;

import edu.wpi.first.math.controller.PIDController;

public class Vision {
    private Limelight limelight = new Limelight();
    private MecanumTankDrive drive;
    private PIDController rotationPID = new PIDController(1, 0.5, 0.5);
    private PIDController distancePID = new PIDController(1, 0.5, 0.5);

    private boolean visionOn = true;
    private double sweetSpot = 0.5;

    public Vision(MecanumTankDrive drivetrain) {
        limelight.setCamMode(camMode.VISION);
        limelight.setLedMode(ledMode.PIPELINE);
        drive = drivetrain;
    }

    public void setDriverMode() {
        limelight.setCamMode(camMode.DRIVER);
        limelight.setLedMode(ledMode.OFF);
        visionOn = false;
    }

    public void setVisionMode() {
        limelight.setCamMode(camMode.VISION);
        limelight.setLedMode(ledMode.ON);
        visionOn = true;
    }

    public boolean isVisionOn() {
        return visionOn;
    }

    public void toggleVision() {
        if(visionOn) {
            limelight.setCamMode(camMode.DRIVER);
            limelight.setLedMode(ledMode.OFF);
        } else {
            limelight.setCamMode(camMode.VISION);
            limelight.setLedMode(ledMode.PIPELINE);
        }
        visionOn = !visionOn;
    }

    public void centerTarget() {
        if(limelight.getTargetX() < 0.1 || limelight.getTargetX() > 0.1) {
            drive.MecanumRobotCentric(0, 0, 0);
        } else {
            drive.MecanumRobotCentric(0, 0, rotationPID.calculate(limelight.getTargetX(), 0));
        }
    }

    public void goToSweetSpot() {
        if(limelight.getTargetArea() < (sweetSpot - 0.01) || limelight.getTargetArea() > (sweetSpot+ 0.01));
    }

    public double getBarValue() {
        return limelight.getTargetArea();
    }

    public Limelight getLimelight() {
        return limelight;
    }
}
