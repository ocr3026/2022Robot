package ocr3026.util;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Robot;

public class Dashboard {
	private static final Limelight limelight = Robot.vision.limelight;
	private static final Vision vision = Robot.vision;
	private static final AHRS gyro = Robot.gyroscope;
	private static final NetworkTable dashboard = NetworkTableInstance.getDefault().getTable("SmartDashboard");
	private static Number gyroValues[] = new Number[3];

	public static void updateDriverStationValues() {
		gyroValues[0] = gyro.getYaw();
		gyroValues[1] = gyro.getPitch();
		gyroValues[2] = gyro.getRoll();
		dashboard.getEntry("gyro").setNumberArray(gyroValues);

		dashboard.getEntry("sweetspot").setNumber(vision.getBarValue());
		dashboard.getEntry("vangle").setNumber(limelight.getTargetX());
	}
}
