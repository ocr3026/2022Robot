package ocr3026.util;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class MecanumTankDrive {
  private final MotorController frontLeft, rearLeft, tankLeft, frontRight, rearRight, tankRight;
  private final Solenoid leftSolenoid, rightSolenoid;
  private double deadband = 0;

  public double deadband(double value) {
    if(value > deadband) {
      return (value - deadband) / (1 - deadband);
    } else if(value < -deadband) {
      return (value + deadband) / (1 - deadband);
    } else {
      return 0;
    }
  }

  public MecanumTankDrive(MotorController frontLeft, MotorController rearLeft, MotorController tankLeft, Solenoid leftSolenoid, MotorController frontRight, MotorController rearRight, MotorController tankRight, Solenoid rightSolenoid) {
    this.frontLeft = frontLeft;
    this.rearLeft = rearLeft;
    this.tankLeft = tankLeft;
    this.leftSolenoid = leftSolenoid;
    this.frontRight = frontRight;
    this.rearRight = rearRight;
    this.tankRight = tankRight;
    this.rightSolenoid = rightSolenoid;
  }
  
  public void MecanumRobotCentric(double forwardSpeed, double rightSpeed, double rotationSpeed) {
    leftSolenoid.set(false);
    rightSolenoid.set(false);

    forwardSpeed = deadband(forwardSpeed);
    rightSpeed = deadband(rightSpeed);
    rotationSpeed = deadband(rotationSpeed);

    forwardSpeed = forwardSpeed * Math.abs(forwardSpeed);
    rightSpeed = rightSpeed * Math.abs(rightSpeed);
    rotationSpeed = rotationSpeed * Math.abs(rotationSpeed);

    MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(forwardSpeed, rightSpeed, rotationSpeed, 0);
    
    frontLeft.set(speeds.frontLeft);
    rearLeft.set(speeds.rearLeft);
    frontRight.set(speeds.frontRight);
    rearRight.set(speeds.rearRight);
  }

  public void MecanumFieldCentric(double forwardSpeed, double rightSpeed, double rotationSpeed, double gyroAngle) {
    leftSolenoid.set(false);
    rightSolenoid.set(false);

    forwardSpeed = deadband(forwardSpeed);
    rightSpeed = deadband(rightSpeed);
    rotationSpeed = deadband(rotationSpeed);

    forwardSpeed = forwardSpeed * Math.abs(forwardSpeed);
    rightSpeed = rightSpeed * Math.abs(rightSpeed);
    rotationSpeed = rotationSpeed * Math.abs(rotationSpeed);

    MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(forwardSpeed, rightSpeed, rotationSpeed, gyroAngle);
    
    frontLeft.set(speeds.frontLeft);
    rearLeft.set(speeds.rearLeft);
    frontRight.set(speeds.frontRight);
    rearRight.set(speeds.rearRight);
  }

  public void TankDrive(double forwardSpeed, double rotationSpeed) {
    leftSolenoid.set(true);
    rightSolenoid.set(true);

    forwardSpeed = deadband(forwardSpeed);
    rotationSpeed = deadband(rotationSpeed);

    forwardSpeed = forwardSpeed * Math.abs(forwardSpeed);
    rotationSpeed = rotationSpeed * Math.abs(rotationSpeed);

    DifferentialDrive.WheelSpeeds speeds = DifferentialDrive.arcadeDriveIK(forwardSpeed, rotationSpeed, false);

    frontLeft.set(speeds.left);
    rearLeft.set(speeds.left);
    tankLeft.set(speeds.left);
    frontRight.set(speeds.right);
    rearRight.set(speeds.right);
    tankRight.set(speeds.right);
  }

  public void setDeadband(double deadband) {
    this.deadband = deadband;
  }

  public double getDeadband() {
    return this.deadband;
  }
}
