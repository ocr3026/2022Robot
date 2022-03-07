package ocr3026.util;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import java.lang.Math;
import com.kauailabs.navx.frc.AHRS;

public class MecanumTankDrive {
  private final AHRS gyro = Robot.gyroscope;
  private final CANSparkMax frontLeft, rearLeft, tankLeft, frontRight, rearRight, tankRight;
  private final DoubleSolenoid solenoid;

  private final PIDController strafePID = new PIDController(1, 1, 1);
  private final double steerMultiplier = 1;

  private double targetRotation = 0;
  private double deadband = 0.1;
  private boolean squareInputs = true;
  private double error = 0;

  public double deadband(double value) {
    if (value > deadband) {
      return (value - deadband) / (1 - deadband);
    } else if (value < -deadband) {
      return (value + deadband) / (1 - deadband);
    } else {
      return 0;
    }
  }

  public MecanumTankDrive(CANSparkMax frontLeft, CANSparkMax rearLeft, CANSparkMax tankLeft, CANSparkMax frontRight,
      CANSparkMax rearRight, CANSparkMax tankRight, DoubleSolenoid solenoid) {
    this.frontLeft = frontLeft;
    this.rearLeft = rearLeft;
    this.tankLeft = tankLeft;
    this.frontRight = frontRight;
    this.rearRight = rearRight;
    this.tankRight = tankRight;

    this.solenoid = solenoid;
  }

  public void MecanumRobotCentric(double forwardSpeed, double rightSpeed, double rotationSpeed) {
    solenoid.set(DoubleSolenoid.Value.kReverse);
    targetRotation += rotationSpeed * steerMultiplier;
    
    forwardSpeed = deadband(forwardSpeed);
    rightSpeed = deadband(rightSpeed);
    rotationSpeed = deadband(rotationSpeed);

    if (squareInputs) {
      forwardSpeed = forwardSpeed * Math.abs(forwardSpeed);
      rightSpeed = rightSpeed * Math.abs(rightSpeed);
      rotationSpeed = rotationSpeed * Math.abs(rotationSpeed);
    }

    MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(forwardSpeed, rightSpeed, /* strafePID.calculate(gyro.getYaw() + 180, targetRotation + 180 % 360) */ rotationSpeed, 0);

    frontLeft.set(speeds.frontLeft);
    tankLeft.set(speeds.rearLeft);
    rearLeft.set(speeds.rearLeft);
    frontRight.set(speeds.frontRight);
    tankRight.set(speeds.rearRight);
    rearRight.set(speeds.rearRight);
  }

  public void MecanumFieldCentric(double forwardSpeed, double rightSpeed, double rotationSpeed, double gyroAngle) {
    solenoid.set(DoubleSolenoid.Value.kReverse);
    targetRotation += rotationSpeed * steerMultiplier;

    forwardSpeed = deadband(forwardSpeed);
    rightSpeed = deadband(rightSpeed);
    rotationSpeed = deadband(rotationSpeed);

    if (squareInputs) {
      forwardSpeed = forwardSpeed * Math.abs(forwardSpeed);
      rightSpeed = rightSpeed * Math.abs(rightSpeed);
      rotationSpeed = rotationSpeed * Math.abs(rotationSpeed);
    }

    MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(forwardSpeed, rightSpeed, /* strafePID.calculate(gyro.getYaw() + 180, targetRotation + 180 % 360)*/ rotationSpeed, gyroAngle);

    frontLeft.set(speeds.frontLeft);
    tankLeft.set(speeds.rearLeft);
    rearLeft.set(speeds.rearLeft);
    frontRight.set(speeds.frontRight);
    tankRight.set(speeds.rearRight);
    rearRight.set(speeds.rearRight);
  }

  public void TankDrive(double forwardSpeed, double rotationSpeed) {
    solenoid.set(DoubleSolenoid.Value.kForward);

    forwardSpeed = deadband(forwardSpeed);
    rotationSpeed = deadband(rotationSpeed);

    if (squareInputs) {
      forwardSpeed = forwardSpeed * Math.abs(forwardSpeed);
      rotationSpeed = rotationSpeed * Math.abs(rotationSpeed);
    }

    DifferentialDrive.WheelSpeeds speeds = DifferentialDrive.arcadeDriveIK(forwardSpeed, rotationSpeed, false);

    frontLeft.set(speeds.left);
    rearLeft.set(speeds.left);
    tankLeft.set(speeds.left);
    frontRight.set(speeds.right);
    rearRight.set(speeds.right);
    tankRight.set(speeds.right);

    targetRotation = 0;
  }

  public void setDeadband(double value) {
    deadband = value;
  }

  public double getDeadband() {
    return deadband;
  }

  public void setSquareInputs(boolean value) {
    squareInputs = value;
  }

  public boolean getSquaredInputs() {
    return squareInputs;
  }
}
