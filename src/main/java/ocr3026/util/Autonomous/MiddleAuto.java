package ocr3026.util.Autonomous;

import frc.robot.Robot;
import ocr3026.util.MecanumTankDrive;
import ocr3026.util.RobotAutonomous;

public class MiddleAuto extends RobotAutonomous {
	private final MecanumTankDrive drivetrain = Robot.drivetrain;

	public MiddleAuto() {
		addStep(() -> {
			drivetrain.MecanumRobotCentric(0.1, 0, 0, false);
		}, () -> {
			return !timer.hasElapsed(10);
		});
	}
}
