package ocr3026.util;

import edu.wpi.first.math.controller.PIDController;

public class Vision {
	public Limelight limelight = new Limelight();
	private MecanumTankDrive drive;
	private PIDController rotationPID = new PIDController(0.02, 0, 0);
	private PIDController distancePID = new PIDController(0.02, 0, 0);
	private double flywheelSpeed = 0;

	private boolean visionOn = true;
	public static final double sweetSpot = 4;

	final double a = 0.000006, b = -0.004276, c = 0.564841;

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
			limelight.setCamMode(Limelight.camMode.VISION);
			limelight.setLedMode(Limelight.ledMode.OFF);
		} else {
			limelight.setCamMode(Limelight.camMode.VISION);
			limelight.setLedMode(Limelight.ledMode.PIPELINE);
		}
		visionOn = !visionOn;
	}

	public boolean centerTarget(double forward) {
		if (limelight.getTargetX() < -0.1 || limelight.getTargetX() > 0.1) {
			System.out.println(OCRMath.Clamp(rotationPID.calculate(limelight.getTargetX(), 0), -0.25, 0.25));
			drive.MecanumRobotCentric(0, 0, OCRMath.Clamp(rotationPID.calculate(limelight.getTargetX(), 0), -0.25, 0.25), false);
			return false;
		} else {
			drive.MecanumRobotCentric(forward, 0, 0, false);
			return true;
		}
	}

	public boolean centerTarget() {
		if (limelight.getTargetX() < -0.1 || limelight.getTargetX() > 0.1) {
			drive.MecanumRobotCentric(0, 0, OCRMath.Clamp(rotationPID.calculate(limelight.getTargetX(), 0), -0.25, 0.25), false);
			return false;
		} else {
			drive.MecanumRobotCentric(0, 0, 0);
			return true;
		}
	}

	public boolean goToSweetSpot() {
		if (limelight.getTargetY() < (sweetSpot - 0.01) || limelight.getTargetY() > (sweetSpot + 0.01)) {
			drive.MecanumRobotCentric(distancePID.calculate(limelight.getTargetY(), sweetSpot), 0, 0);
			return false;
		} else {
			return true;
		}
	}

	public double getFlywheelSpeed()  {
		flywheelSpeed = (limelight.getTargetY() * limelight.getTargetY() * a) + (limelight.getTargetY() * b) + c;
		return OCRMath.Clamp(flywheelSpeed, 0.0, 1.0);
	}

	public double getBarValue() {
		return OCRMath.Clamp(limelight.getTargetY() - sweetSpot, -5, 5);
	}

	public Limelight getLimelight() {
		return limelight;
	}
}
