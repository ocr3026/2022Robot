package ocr3026.util.Autonomous;

import ocr3026.util.RobotAutonomous;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class RightAutonomous implements RobotAutonomous {
	boolean centerTargetFinished = false;
	boolean distanceTargetFinished = false;
	boolean visionFinished = false;

	@Override
	public void init() {
		intakeSolenoid.set(Value.kForward);
		kickup.set(Value.kReverse);
		vision.setVisionMode();
	}

	@Override
	public void periodic() {
		if (!visionFinished) {
			if (!centerTargetFinished) {
				centerTargetFinished = vision.centerTarget();
			} else {
				centerTargetFinished = true;
			}

			if (!distanceTargetFinished) {
				distanceTargetFinished = vision.goToSweetSpot();
			} else {
				distanceTargetFinished = true;
			}
		} else {
			visionFinished = true;
			timer.start();
		}

		if (!timer.hasElapsed(3)) {
			if (gyroscope.getYaw() > 35 && gyroscope.getYaw() < 25) {
				drivetrain.MecanumRobotCentric(0, 0, gyroscoperotation.calculate(gyroscope.getYaw(), 30));
			} else {
				drivetrain.MecanumRobotCentric(0, 0, 0);
			}
		} else if (timer.hasElapsed(3) && !timer.hasElapsed(8)) {
		}
		if (frontLeftMecanum.getEncoder().getPosition() < 1.027176) {
			intakeSolenoid.set(Value.kReverse);
			intake.set(ControlMode.PercentOutput, 0.5);
			drivetrain.MecanumRobotCentric(.5, 0, 0);
		} else if (frontLeftMecanum.getEncoder().getPosition() > 1.027176) {
			drivetrain.MecanumRobotCentric(0, 0, 0);
			intake.set(ControlMode.PercentOutput, 0);
			intakeSolenoid.set(Value.kForward);
			kickup.set(Value.kForward);
		} else if (timer.hasElapsed(8) && !timer.hasElapsed(10)) {
			if (gyroscope.getYaw() > 185 && gyroscope.getYaw() < 175) {
				drivetrain.MecanumRobotCentric(0, 0, gyroscoperotation.calculate(gyroscope.getYaw(), 180));
			} else {
				drivetrain.MecanumRobotCentric(0, 0, 0);
			}
		} else {
			centerTargetFinished = false;
			distanceTargetFinished = false;
			visionFinished = false;
		}
	}
}
