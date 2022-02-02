// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Compressor;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.kauailabs.navx.frc.AHRS;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import ocr3026.util.Limelight;
import ocr3026.util.Toggle;
import ocr3026.util.Limelight.camMode;
import ocr3026.util.Limelight.ledMode;
import ocr3026.util.MecanumTankDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String middleAuto = "middle";
  private static final String leftAuto = "left";
  private static final String rightAuto = "right";
  private String m_autoSelected;

  private Limelight limelight;

  private Toggle fieldtoggle = new Toggle();

  Joystick joystick = new Joystick(0);
  Joystick steer = new Joystick(1);
  XboxController xbox = new XboxController(2);

  AHRS gyroscope = new AHRS();

  WPI_VictorSPX frontLeftTank = new WPI_VictorSPX(29);
  WPI_VictorSPX frontRightTank = new WPI_VictorSPX(6);
  WPI_VictorSPX backLeftTank = new WPI_VictorSPX(22);
  WPI_VictorSPX backRightTank = new WPI_VictorSPX(18);
  MotorControllerGroup left = new MotorControllerGroup(frontLeftTank, backLeftTank);
  MotorControllerGroup right = new MotorControllerGroup(frontRightTank, backRightTank);
  DifferentialDrive drivetrain = new DifferentialDrive(left, right);
  Solenoid leftTankSolenoid = new Solenoid(1, PneumaticsModuleType.CTREPCM, 0);
  Solenoid rightTankSolenoid = new Solenoid(1, PneumaticsModuleType.CTREPCM, 1);
  Compressor compressor = new Compressor(1, PneumaticsModuleType.CTREPCM);

  WPI_TalonSRX leftClimber = new WPI_TalonSRX(100);
  WPI_TalonSRX rightClimber = new WPI_TalonSRX(101);
  WPI_TalonSRX leftShortClimber = new WPI_TalonSRX(100);
  WPI_TalonSRX rightShortClimber = new WPI_TalonSRX(101);
  MotorControllerGroup shortClimber = new MotorControllerGroup(leftShortClimber, rightShortClimber);
  MotorControllerGroup climber = new MotorControllerGroup(leftClimber, rightClimber);
  DoubleSolenoid climberSolenoid = new DoubleSolenoid(1, PneumaticsModuleType.CTREPCM, 3, 4);

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    drivetrain.setDeadband(0.15d);

    CameraServer.startAutomaticCapture();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and
   * test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * ' * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case middleAuto:
        break;

      case leftAuto:
        break;
      case rightAuto:
          break;
        }
    }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    

    if (xbox.getAButton()) {
      shortClimber.set(.25);
    }
    else if (xbox.getBButton()) {
      shortClimber.set(-.25);
    }
    else {
      shortClimber.set(0);
    }
    if (xbox.getYButton() ) {
      climber.set(1);
    } else if (xbox.getXButton()) {
      climber.set(-1);
    } else if (xbox.getRightTriggerAxis() > .9) {
        leftClimber.set(ControlMode.Position, 4096);
        rightClimber.set(ControlMode.Position, 4096);
    } else if (xbox.getLeftTriggerAxis() > 0.9) {
      leftShortClimber.set(ControlMode.Position, 4096);
      rightShortClimber.set(ControlMode.Position, 4096);
    }
    else {
      climber.set(0);
    }
    if (xbox.getLeftBumperPressed()) {
      climberSolenoid.set(Value.kForward);
    } else if (xbox.getRightBumperPressed()) {
      climberSolenoid.set(Value.kReverse);
    } else {
      climberSolenoid.set(Value.kOff);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    fieldtoggle.setValue(false);
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
}
