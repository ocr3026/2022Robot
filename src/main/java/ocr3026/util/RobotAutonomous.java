package ocr3026.util;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Timer;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Robot;

public interface RobotAutonomous {
  public static final AHRS gyroscope = Robot.gyroscope;
  public static final PIDController gyroscoperotation = Robot.gyroscoperotation;
  public static final MecanumTankDrive drivetrain = Robot.drivetrain;
  public static final CANSparkMax frontLeftMecanum = Robot.frontLeftMecanum;
  public static final DoubleSolenoid kickup = Robot.kickup;
  public static final DoubleSolenoid intakeSolenoid = Robot.intakeSolenoid;
  public static final VictorSPX intake = Robot.intake;
  Timer timer = new Timer();
  //TODO Add vision to this
  public void init();
  public void periodic();
}
