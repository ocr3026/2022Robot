package ocr3026.util.Autonomous;

import ocr3026.util.RobotAutonomous;

public class MiddleAutonomous implements RobotAutonomous {
	@Override
	public void init() {

	}

	@Override
	public void periodic() {
		//TODO first autoaim instance
		if ()
		if(gyroscope.getYaw() > 35 && gyroscope.getYaw() < 25) {
			drivetrain.MecanumRobotCentric(0, 0, gyroscoperotation.calculate(gyroscope.getYaw(), 30));
		}
	}
}
