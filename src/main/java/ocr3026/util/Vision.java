package ocr3026.util;

import edu.wpi.first.math.controller.PIDController;

public class Vision {
	public Limelight limelight = new Limelight();
	private MecanumTankDrive drive;
	private PIDController rotationPID = new PIDController(0.02, 0, 0);
	private PIDController distancePID = new PIDController(0.02, 0, 0);

	private boolean visionOn = true;
	public static final double sweetSpot = 4;

	public Vision(MecanumTankDrive drivetrain) {
		limelight.setCamMode(Limelight.camMode.VISION);
		limelight.setLedMode(Limelight.ledMode.PIPELINE);
		drive = drivetrain;
		limelight.setPipeline(0);
	}

	public void setDriverMode() {
		limelight.setCamMode(Limelight.camMode.VISION);
		limelight.setLedMode(Limelight.ledMode.OFF);
		visionOn = false;
	}

	public void setVisionMode() {
		limelight.setCamMode(Limelight.camMode.VISION);
		limelight.setLedMode(Limelight.ledMode.PIPELINE);
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
		if (limelight.getTargetX() < -0.1 || limelight.getTargetX() > 0.1) {
			System.out.println(Math.Clamp(rotationPID.calculate(limelight.getTargetX(), 0), -0.25, 0.25));
			drive.MecanumRobotCentric(0, 0, Math.Clamp(rotationPID.calculate(limelight.getTargetX(), 0), -0.25, 0.25), false);
			return false;
		} else {
			drive.MecanumRobotCentric(forward, 0, 0, false);
			return true;
		}
	}

	public boolean centerTarget() {
		if (limelight.getTargetX() < -0.1 || limelight.getTargetX() > 0.1) {
			drive.MecanumRobotCentric(0, 0, Math.Clamp(rotationPID.calculate(limelight.getTargetX(), 0), -0.25, 0.25), false);
			return false;
		} else {
			drive.MecanumRobotCentric(0, 0, 0);
			return true;
		}
	}

	public boolean goToSweetSpot() {
		if (limelight.getBoundingBoxVertical() < (sweetSpot - 0.01) || limelight.getBoundingBoxVertical() > (sweetSpot + 0.01)) {
			drive.MecanumRobotCentric(distancePID.calculate(limelight.getTargetY(), sweetSpot), 0, 0);
			return false;
		} else {
			return true;
		}
	}

	public double getBarValue() {
		return Math.Clamp(limelight.getTargetY() - sweetSpot, -5, 5);
	}

	public Limelight getLimelight() {
		return limelight;
	}
}
