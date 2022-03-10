package ocr3026.util;

import com.revrobotics.RelativeEncoder;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.Robot;

public interface RobotAutonomous {
  public static final AHRS gyroscope = Robot.gyroscope;
  public static final PIDController gyroscoperotation = Robot.gyroscoperotation;
  public static final MecanumTankDrive drivetrain = Robot.drivetrain;
  public static final DoubleSolenoid kickup = Robot.kickup;
  public static final DoubleSolenoid intakeSolenoid = Robot.intakeSolenoid;
  public static final VictorSPX intake = Robot.intake;
  public static final Vision vision = Robot.vision;
  public static final Timer timer = new Timer();
  public static final RelativeEncoder encoder = Robot.encoder;
  //TODO Add vision to this
  public void init();
  public void periodic();
}
