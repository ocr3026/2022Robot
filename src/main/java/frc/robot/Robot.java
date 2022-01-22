// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.cameraserver.CameraServer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.kauailabs.navx.frc.AHRS;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import ocr3026.util.Limelight;
import ocr3026.util.Toggle;
import ocr3026.util.Limelight.camMode;
import ocr3026.util.Limelight.ledMode;
import ocr3026.util.MecanumTankDrive;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Limelight limelight;

  private Toggle fieldtoggle = new Toggle();

  Joystick joystick = new Joystick(0);
  Joystick steer = new Joystick(1);
  XboxController xbox = new XboxController(2);

  AHRS gyroscope = new AHRS();

  WPI_VictorSPX frontLeftMecanum = new WPI_VictorSPX(29);
  WPI_VictorSPX frontRightMecanum = new WPI_VictorSPX(6);
  WPI_VictorSPX backLeftMecanum = new WPI_VictorSPX(22);
  WPI_VictorSPX backRightMecanum = new WPI_VictorSPX(18);
  CANSparkMax leftTank = new CANSparkMax(50, MotorType.kBrushless);
  CANSparkMax rightTank = new CANSparkMax(51, MotorType.kBrushless);

  Solenoid leftTankSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
  Solenoid rightTankSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 1);

  MecanumTankDrive drivetrain = new MecanumTankDrive(frontLeftMecanum, backLeftMecanum, leftTank, leftTankSolenoid, frontRightMecanum, backRightMecanum, rightTank, rightTankSolenoid);
  
  PIDController visionRotationController = new PIDController(1, 1, 1);
  PIDController visionDistanceController = new PIDController(1, 1, 1);
  boolean visionStage = false;
  double visionSweetArea = 0.25;

  CANSparkMax flywheel = new CANSparkMax(37, MotorType.kBrushless);

  WPI_VictorSPX intake = new WPI_VictorSPX(53);
  WPI_VictorSPX load = new  WPI_VictorSPX(21);
  WPI_VictorSPX kickup = new  WPI_VictorSPX(31);

  boolean ballLoaded = false;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    limelight = new Limelight();

    drivetrain.setDeadband(0.15d);
    
    frontRightMecanum.setInverted(true);
    backRightMecanum.setInverted(true);
    frontLeftMecanum.setNeutralMode(NeutralMode.Brake);
    backLeftMecanum.setNeutralMode(NeutralMode.Brake);
    frontRightMecanum.setNeutralMode(NeutralMode.Brake);
    backRightMecanum.setNeutralMode(NeutralMode.Brake);
  
    CameraServer.startAutomaticCapture();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
'   * SmartDashboard integrated updating.
   */
 @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    ballLoaded = true;
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code herespeed
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (joystick.getRawButtonPressed(12)) {
      gyroscope.zeroYaw();
    }

    if (joystick.getRawButtonPressed(2)) {
      fieldtoggle.toggleValue();
    }

    if (xbox.getLeftTriggerAxis() > 0.9) {
      // Vision
      limelight.setCamMode(camMode.VISION);
      limelight.setLedMode(ledMode.PIPELINE);

      drivetrain.MecanumRobotCentric(0, 0, 0);

      // Phase 1: Line up rotation

      if (visionStage == false) {
        if (-0.1 < limelight.getTargetX() && limelight.getTargetX() < 0.1) {
          visionStage = true;
        } else {
          drivetrain.MecanumRobotCentric(0, 0, visionRotationController.calculate(limelight.getTargetX()));
        }
      }

      // Phase 2: Get to sweet spot distance

      if (visionStage == true) {
        if (limelight.getTargetArea() == visionSweetArea) {
          visionStage = false;
        } else {
          drivetrain.MecanumRobotCentric(0, visionDistanceController.calculate(limelight.getTargetArea(), visionSweetArea), 0);
        }
      }
    } else {
      // Driver
      limelight.setCamMode(camMode.DRIVER);
      limelight.setLedMode(ledMode.OFF);
      
      if (joystick.getRawButton(1)) {
        drivetrain.TankDrive(joystick.getY(), steer.getX());
      } else if (fieldtoggle.getValue()) {
        drivetrain.MecanumFieldCentric(joystick.getY(), -joystick.getX(), steer.getX(), gyroscope.getYaw());
      } else {
        drivetrain.MecanumRobotCentric(joystick.getY(), -joystick.getX(), steer.getX());
      }
      
    }

    if (xbox.getYButtonPressed()) {
      ballLoaded = false;
    }

    if (xbox.getAButton()) {
      flywheel.set(-1);
      kickup.set(1);
    } else {
      flywheel.set(0);
      kickup.set(0);
    }

    if (xbox.getAButtonPressed()) {
      ballLoaded = false;
    }

    if (xbox.getBButton()){
      if (ballLoaded == false) {
        intake.set(1);
        load.set(1);
      } else {
        intake.set(1);
      }
    } else {
      intake.set(0);
      load.set(0);
    }
  
    if(xbox.getBButtonPressed()) {
      ballLoaded = true;
    }
  }
  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    fieldtoggle.setValue(false);
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
