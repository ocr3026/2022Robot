package ocr3026.util;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.controller.PIDController;

public class Vision {
	private Limelight limelight = new Limelight();
	private MecanumTankDrive drive;
	private PIDController rotationPID = new PIDController(1, 0.5, 0.5);
	private PIDController distancePID = new PIDController(1, 0.5, 0.5);

	private boolean visionOn = true;
	private double sweetSpot = 0.3;

	public Vision(MecanumTankDrive drivetrain) {
		limelight.setCamMode(Limelight.camMode.VISION);
		limelight.setLedMode(Limelight.ledMode.PIPELINE);
		drive = drivetrain;
	}

	public void setDriverMode() {
		limelight.setCamMode(Limelight.camMode.DRIVER);
		limelight.setLedMode(Limelight.ledMode.OFF);
		visionOn = false;
	}

	public void setVisionMode() {
		limelight.setCamMode(Limelight.camMode.VISION);
		limelight.setLedMode(Limelight.ledMode.ON);
		visionOn = true;
	}

	public boolean isVisionOn() {
		return visionOn;
	}

	public void toggleVision() {
		if (visionOn) {
			limelight.setCamMode(Limelight.camMode.DRIVER);
			limelight.setLedMode(Limelight.ledMode.OFF);
		} else {
			limelight.setCamMode(Limelight.camMode.VISION);
			limelight.setLedMode(Limelight.ledMode.PIPELINE);
		}
		visionOn = !visionOn;
	}

	public boolean centerTarget(double forward) {
		if (limelight.getTargetX() < 0.1 || limelight.getTargetX() > 0.1) {
			drive.MecanumRobotCentric(0, 0, rotationPID.calculate(limelight.getTargetX(), 0));
			return false;
		} else {
			drive.MecanumRobotCentric(forward, 0, 0);
			return true;
		}
	}

	public boolean centerTarget() {
		if (limelight.getTargetX() < 0.1 || limelight.getTargetX() > 0.1) {
			drive.MecanumRobotCentric(0, 0, rotationPID.calculate(limelight.getTargetX(), 0));
			return false;
		} else {
			drive.MecanumRobotCentric(0, 0, 0);
			return true;
		}
	}

	public boolean goToSweetSpot() {
		if (limelight.getTargetArea() < (sweetSpot - 0.01) || limelight.getTargetArea() > (sweetSpot + 0.01)) {
			drive.MecanumRobotCentric(distancePID.calculate(limelight.getTargetArea(), sweetSpot), 0, 0);
			return false;
		} else {
			return true;
		}
	}

	public double getBarValue() {
		return limelight.getTargetArea();
	}

	public Limelight getLimelight() {
		return limelight;
	}
}