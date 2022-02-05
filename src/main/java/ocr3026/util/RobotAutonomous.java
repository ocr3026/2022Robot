package ocr3026.util;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Robot;

public interface RobotAutonomous {
  public static final AHRS gyroscope = Robot.gyroscope;
  public static final PIDController gyroscoperotation = Robot.gyroscoperotation;
  public static final MecanumTankDrive drivetrain = Robot.drivetrain;
  Timer timer = new Timer();
  //TODO Add vision to this
  public void init();
  public void periodic();
}
