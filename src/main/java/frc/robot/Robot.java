// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.cameraserver.CameraServer;

import com.kauailabs.navx.frc.AHRS;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import ocr3026.util.Toggle;
import ocr3026.util.Deadband;

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

  Toggle limitsOverridden = new Toggle(false);

  Joystick joystick = new Joystick(0);
  Joystick steer = new Joystick(1);
  XboxController xbox = new XboxController(2);

  AHRS gyroscope = new AHRS();

  WPI_VictorSPX frontLeftTank = new WPI_VictorSPX(28);
  WPI_TalonSRX frontRightTank = new WPI_TalonSRX(15);
  WPI_VictorSPX backLeftTank = new WPI_VictorSPX(27);
  WPI_TalonSRX backRightTank = new WPI_TalonSRX(11);
  MotorControllerGroup left = new MotorControllerGroup(frontLeftTank, backLeftTank);
  MotorControllerGroup right = new MotorControllerGroup(frontRightTank, backRightTank);
  DifferentialDrive drivetrain = new DifferentialDrive(left, right);

  WPI_TalonSRX leftClimber = new WPI_TalonSRX(22);
  WPI_TalonSRX rightClimber = new WPI_TalonSRX(16);
  WPI_TalonSRX leftInnerClimber = new WPI_TalonSRX(18);
  WPI_TalonSRX rightInnerClimber = new WPI_TalonSRX(21);
  WPI_TalonSRX angleScrew = new WPI_TalonSRX(12);

  MotorControllerGroup innerClimbers = new MotorControllerGroup(leftInnerClimber, rightInnerClimber);
  MotorControllerGroup climbers = new MotorControllerGroup(leftClimber, rightClimber);

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    leftInnerClimber.setNeutralMode(NeutralMode.Brake);
    rightInnerClimber.setNeutralMode(NeutralMode.Brake);

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
    System.out.println(angleScrew.getSelectedSensorPosition());
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
    if (joystick.getRawButtonPressed(7)) {
      leftClimber.setSelectedSensorPosition(0);
      rightClimber.setSelectedSensorPosition(0);
      leftInnerClimber.setSelectedSensorPosition(0);
      rightClimber.setSelectedSensorPosition(0);
      angleScrew.setSelectedSensorPosition(0);
    }

    if(joystick.getRawButtonPressed(8)) {
      System.out.println("Limits toggled to" + limitsOverridden.swap());
    }

    drivetrain.arcadeDrive(steer.getX(), joystick.getY(), true);

    if (!limitsOverridden.isOn()) {
      if (leftInnerClimber.getSelectedSensorPosition() > 4096 * 220) {
        if (xbox.getLeftY() < 0) {
          leftInnerClimber.set(Deadband.deadband(xbox.getLeftY(), 0.1) * 0.5);
        
        } else if (xbox.getRightY() < 0) {
          rightInnerClimber.set(Deadband.deadband(xbox.getRightY(), 0.1) * 0.5);
        } else {
          innerClimbers.set(0);
        }
      } else if (leftInnerClimber.getSelectedSensorPosition() <= 4096 * 0) {
        if (xbox.getLeftY() > 0) {
          leftInnerClimber.set(Deadband.deadband(xbox.getLeftY(), 0.1) * 0.5);
        
        } else if (xbox.getRightY() > 0) {
          rightInnerClimber.set(Deadband.deadband(xbox.getRightY(), 0.1) * 0.6);
        }
        else {
          innerClimbers.set(0);
        }
      } else {
        leftInnerClimber.set(Deadband.deadband(xbox.getLeftY(), 0.1) * 0.5);
        rightInnerClimber.set(Deadband.deadband(xbox.getRightY(), 0.1) * 0.6);
      }

      if (angleScrew.getSelectedSensorPosition() > 361489) {
        if (xbox.getPOV() == 18 0) {
            angleScrew.set((-joystick.getRawAxis(3) + 1) / -2);
        } else {
          angleScrew.set(0);
        }
      } else if (angleScrew.getSelectedSensorPosition() < -651244) {
          if (xbox.getPOV() == 0) {
            angleScrew.set((-joystick.getRawAxis(3) + 1) / 2);
          }
          else {
            angleScrew.set(0);
          }
      } else {
        if (xbox.getPOV() == 0) {
          angleScrew.set((-joystick.getRawAxis(3) + 1) / 2);
        } else if (xbox.getPOV() == 180) {
          angleScrew.set((-joystick.getRawAxis(3) + 1) / -2);
        }
        else {
          angleScrew.set(0);
        }
      }

      if (leftClimber.getSelectedSensorPosition() < -30000 * 12) {
        if (steer.getRawButton(3)) {
          climbers.set((-steer.getRawAxis(2) + 1) / -2);
        } else {
          climbers.set(0);
        }
      } else if (leftClimber.getSelectedSensorPosition() > 0) {
        if (steer.getRawButton(2)) {
          climbers.set((-steer.getRawAxis(2) + 1) / 2);
        } else {
          climbers.set(0);
        }
      } else {
        if (steer.getRawButton(2)) {
          climbers.set((-steer.getRawAxis(2) + 1) / 2);
        } else if (steer.getRawButton(3)) {
          climbers.set((-steer.getRawAxis(2) + 1) / -2);
        } else {
          climbers.set(0);
        }
      }
    } else {
      leftInnerClimber.set(Deadband.deadband(xbox.getLeftY(), 0.1) * 0.5);
      rightInnerClimber.set(Deadband.deadband(xbox.getRightY(), 0.1) * 0.6);

      if (xbox.getPOV() == 0) {
        angleScrew.set((-joystick.getRawAxis(3) + 1) / 2);
      } else if (xbox.getPOV() == 180) {
        angleScrew.set((-joystick.getRawAxis(3) + 1) / -2);
      }
      else {
        angleScrew.set(0);
      }
      

      if (steer.getRawButton(2)) {
        climbers.set((-steer.getRawAxis(2) + 1) / 2);
      } else if (steer.getRawButton(3)) {
        climbers.set((-steer.getRawAxis(2) + 1) / -2);
      } else {
        climbers.set(0);
      }
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
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
