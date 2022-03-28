// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.kauailabs.navx.frc.AHRS;

import ocr3026.util.Toggle;
import ocr3026.Deadband;
import ocr3026.util.Dashboard;
import ocr3026.util.OCRMath;
import ocr3026.util.MecanumTankDrive;
import ocr3026.util.RobotAutonomous;
import ocr3026.util.Autonomous.*;
import ocr3026.util.Vision;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creatiforwardSpeed
 */
public class Robot extends TimedRobot {
  private static final String middleAuto = "middle";
  private static final String leftAuto = "left";
  private static final String rightAuto = "right";
  private String m_autoSelected;

  private RobotAutonomous autonomous;

  private Toggle fieldtoggle = new Toggle();

  public static Joystick joystick = new Joystick(0);
  Joystick steer = new Joystick(1);
  XboxController xbox = new XboxController(2);

  public static AHRS gyroscope = new AHRS();

  public static CANSparkMax frontLeftMecanum = new CANSparkMax(7, MotorType.kBrushless);
  public static CANSparkMax frontRightMecanum = new CANSparkMax(3, MotorType.kBrushless);
  public static CANSparkMax backLeftMecanum = new CANSparkMax(6, MotorType.kBrushless);
  public static CANSparkMax backRightMecanum = new CANSparkMax(4, MotorType.kBrushless);
  public static CANSparkMax leftTank = new CANSparkMax(5, MotorType.kBrushless);
  public static CANSparkMax rightTank = new CANSparkMax(2, MotorType.kBrushless);

  public static RelativeEncoder encoder = frontLeftMecanum.getEncoder();

  public static PIDController gyroscoperotation = new PIDController(1, 0, 0);

  static DoubleSolenoid tankSolenoid = new DoubleSolenoid(1, PneumaticsModuleType.CTREPCM, 3, 2);

  public static MecanumTankDrive drivetrain = new MecanumTankDrive(frontLeftMecanum, backLeftMecanum, leftTank,
      frontRightMecanum, backRightMecanum, rightTank, tankSolenoid);

  public static CANSparkMax flywheel = new CANSparkMax(37, MotorType.kBrushless);

  public static CANSparkMax intake = new CANSparkMax(11, MotorType.kBrushless);
  public static DoubleSolenoid kickup = new DoubleSolenoid(1, PneumaticsModuleType.CTREPCM, 1, 0);
  public static DoubleSolenoid intakeSolenoid = new DoubleSolenoid(1, PneumaticsModuleType.CTREPCM, 5, 4);
  Compressor compressor = new Compressor(1, PneumaticsModuleType.CTREPCM);

  DigitalInput ballLoaded = new DigitalInput(1);
  DigitalInput ballIntake = new DigitalInput(2);

  /*
  WPI_TalonSRX leftClimber = new WPI_TalonSRX(15);
  WPI_TalonSRX rightClimber = new WPI_TalonSRX(14);
  MotorControllerGroup climber = new MotorControllerGroup(leftClimber, rightClimber);
  */

  CANSparkMax leftClimber = new CANSparkMax(10, MotorType.kBrushless);
  CANSparkMax rightClimber = new CANSparkMax(9, MotorType.kBrushless);
  MotorControllerGroup climbers = new MotorControllerGroup(leftClimber, rightClimber);

  CANSparkMax climberAngle = new CANSparkMax(8, MotorType.kBrushless);
  final double angleSpeed = 0.35;

  public static Vision vision = new Vision(drivetrain);

  public static Timer teleopTimer = new Timer();

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    drivetrain.setDeadband(0.15d);
    encoder.setPositionConversionFactor(19.1008833338);

    frontRightMecanum.setInverted(false);
    frontLeftMecanum.setInverted(true);
    backRightMecanum.setInverted(false);
    backLeftMecanum.setInverted(true);
    leftTank.setInverted(true);
    rightTank.setInverted(false);

    frontLeftMecanum.setIdleMode(IdleMode.kBrake);
    backLeftMecanum.setIdleMode(IdleMode.kBrake);
    frontRightMecanum.setIdleMode(IdleMode.kBrake);
    backRightMecanum.setIdleMode(IdleMode.kBrake);
    leftTank.setIdleMode(IdleMode.kBrake);
    rightTank.setIdleMode(IdleMode.kBrake);

    leftClimber.setInverted(true);

    leftClimber.setIdleMode(IdleMode.kBrake);
    rightClimber.setIdleMode(IdleMode.kBrake);
    climberAngle.setIdleMode(IdleMode.kBrake);

    intake.setIdleMode(IdleMode.kBrake);
    flywheel.setIdleMode(IdleMode.kBrake);

    kickup.set(Value.kForward);
    intakeSolenoid.set(Value.kForward);

    CameraServer.startAutomaticCapture();
    String[] autos = { "shooting Auto", "Middle Right Auto", "Middle Left Auto Middle Ball", "Middle Left Auto Left Ball", "Left Auto", "Right Auto"};
    SmartDashboard.putStringArray("Auto List", autos);
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
    Dashboard.updateDriverStationValues();
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
    m_autoSelected = SmartDashboard.getString("Auto Selector", "shooting Auto" );
    System.out.println("Auto selected: " + m_autoSelected);
    switch (m_autoSelected) {
      case "shooting Auto" :
        autonomous = new shootingAuto();
        break;
      case  "Middle Right Auto" :
        autonomous = new MiddleRightAuto();
        break;
      case "Middle Left Auto Middle Ball" :
        autonomous = new MiddleLeftAutoMiddleBall();
        break;
      case "Middle Left Auto Left Ball" :
        autonomous = new MiddleLeftAutoLeftBall();
        break;
      case "Left Auto" :
        autonomous = new LeftAuto();
        break;
      case "Right Auto" :
        autonomous = new RightAuto();
        break;
      default :
        autonomous = new shootingAuto();
        break;
    }
    autonomous.init();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    compressor.enableDigital();
    autonomous.periodic();
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    autonomous = null;
    System.gc();
    teleopTimer.reset();
    teleopTimer.start();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (joystick.getRawButtonPressed(13)) {
      leftClimber.getEncoder().setPosition(0);
      rightClimber.getEncoder().setPosition(0);
      climberAngle.getEncoder().setPosition(0);
    }
    /*
    if(teleopTimer.hasElapsed(105)) {
      compressor.disable();
    } else {
      compressor.enableDigital();
    }
    */

    compressor.enableDigital();

    if (joystick.getRawButtonPressed(12)) {
      gyroscope.zeroYaw();
    }

    if (joystick.getRawButtonPressed(2)) {
      fieldtoggle.swap();
    }

    if (xbox.getLeftTriggerAxis() > 0.9) {
      vision.setVisionMode();
      vision.centerTarget();
    } else {
      vision.setVisionMode();
      if (joystick.getRawButton(1)) {
        drivetrain.TankDrive(joystick.getY(), -steer.getX());
      } else if (fieldtoggle.isOn()) {
        drivetrain.MecanumFieldCentric(joystick.getY(), -joystick.getX(), -steer.getX(), gyroscope.getYaw());
      } else {
        drivetrain.MecanumRobotCentric(joystick.getY(), -joystick.getX(), -steer.getX());
      }
    }
    if (xbox.getRightTriggerAxis() > 0.9) {
      flywheel.set(0.60);
    } else {
      flywheel.set(0);
    }

    if (xbox.getXButton()) {
      kickup.set(Value.kReverse);
    } else {
      kickup.set(Value.kForward);
    }

    if (joystick.getRawButtonPressed(5)) {
      intakeSolenoid.toggle();
    }

    if (joystick.getRawButton(4)) {
      intake.set(-0.75);
    } else if (joystick.getRawButton(3)) {
      intake.set(0.75);
    } else {
      intake.set(0);
    }

    if(steer.getRawButton(2) || steer.getRawButton(3) || steer.getRawButton(8) || steer.getRawButton(9) || steer.getRawButton(6) || steer.getRawButton(7) || steer.getRawButton(10) || steer.getRawButton(11) || steer.getRawButton(5) || steer.getRawButton(4)){
      if (steer.getRawButton(3)) {
        climbers.set(1);
      } else if (steer.getRawButton(2)) {
        climbers.set(-1);
      } else if (steer.getRawButton(6)) {
        leftClimber.set(1);
        rightClimber.set(0);
      }  else if (steer.getRawButton(7)) {
        leftClimber.set(-1);
        rightClimber.set(0);
      } else if (steer.getRawButton(11)) {
        rightClimber.set(1);
        leftClimber.set(0);
      } else if (steer.getRawButton(10)) {
        rightClimber.set(-1);
        leftClimber.set(0);
      } else {
        climbers.set(0);
      }

      if (steer.getRawButton(9) || steer.getRawButton(5)) {
        climberAngle.set(angleSpeed);
      } else if (steer.getRawButton(8) || steer.getRawButton(4)) {
        climberAngle.set(-angleSpeed);
      } else {
        climberAngle.set(0);
      }
    } else {
      climbers.set(-Deadband.deadband(xbox.getLeftY(), 0.05));

      climberAngle.set(Deadband.deadband(xbox.getRightY(), 0.05));
    }

    /*
    if (innerLeftClimber.getEncoder().getPosition() > 42 * 220) {
      if (xbox.getLeftY() < 0) {
        innerClimbers.set(ocr3026.Deadband.deadband(xbox.getLeftY(), 0.1) * 0.25);
      } else {
        innerClimbers.set(0);
      }
    } else if (innerLeftClimber.getEncoder().getPosition() <= 42 * 0) {
      if (xbox.getLeftY() > 0) {
        innerClimbers.set(ocr3026.Deadband.deadband(xbox.getLeftY(), 0.1) * 0.25);
      } else {
        innerClimbers.set(0);
      }
    } else {
      innerClimbers.set(ocr3026.Deadband.deadband(xbox.getLeftY(), 0.1) * 0.25);
    }

    if (angleScrew.getEncoder().getPosition() > 42 * 20) {
      if (xbox.getRightY() < 0) {
        angleScrew.set(ocr3026.Deadband.deadband(xbox.getRightY(), 0.1) * 0.25);
      } else {
        angleScrew.set(0);
      }
    } else if (angleScrew.getEncoder().getPosition() < 0) {
      if (xbox.getRightY() > 0) {
        angleScrew.set(ocr3026.Deadband.deadband(xbox.getRightY(), 0.1) * 0.25);
      } else {
        angleScrew.set(0);
      }
    } else {
      angleScrew.set(ocr3026.Deadband.deadband(xbox.getRightY(), 0.1) * 0.25);
    }

    if (leftClimber.getSelectedSensorPosition() < -30000 * 12) {
      if (steer.getRawButton(3)) {
        climber.set((-steer.getRawAxis(2) + 1) / -2);
      } else {
        climber.set(0);
      }
    } else if (leftClimber.getSelectedSensorPosition() > 0) {
      if (steer.getRawButton(2)) {
        climber.set((-steer.getRawAxis(2) + 1) / 2);
      } else {
        climber.set(0);
      }
    } else {
      if (steer.getRawButton(2)) {
        climber.set((-steer.getRawAxis(2) + 1) / 2);
      } else if (steer.getRawButton(3)) {
        climber.set((-steer.getRawAxis(2) + 1) / -2);
      } else {
        climber.set(0);
      }
    }
    */
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    fieldtoggle.setToggle(false);
    compressor.disable();
    autonomous = null;
    System.gc();
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



