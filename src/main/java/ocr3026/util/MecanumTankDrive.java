package ocr3026.util;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class MecanumTankDrive {
  private MotorController frontLeft, rearLeft, tankLeft, frontRight, rearRight, tankRight;

  private double deadband(double value) {
    if(value > deadband) {
      return (value - deadband) / (1 - deadband);
    } else if(value < -deadband) {
      return (value + deadband) / (1 - deadband);
    } else {
      return 0;
    }
  }

  public double deadband = 0;

  public MecanumTankDrive(MotorController frontLeft, MotorController rearLeft, MotorController tankLeft, MotorController frontRight, MotorController rearRight, MotorController tankRight) {
    this.frontLeft = frontLeft;
    this.rearLeft = rearLeft;
    this.tankLeft = tankLeft;
    this.frontRight = frontRight;
    this.rearRight = rearRight;
    this.tankRight = tankRight;
  }
  
  public void MecanumRobotCentric(double forwardSpeed, double rightSpeed, double rotationSpeed) {

    MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(deadband(forwardSpeed), deadband(rightSpeed), deadband(rotationSpeed), 0);
    
    frontLeft.set(speeds.frontLeft);
    rearLeft.set(speeds.rearLeft);
    frontRight.set(speeds.frontRight);
    rearRight.set(speeds.rearRight);
  }

  public void MecanumFieldCentric(double forwardSpeed, double rightSpeed, double rotationSpeed, double gyroAngle) {

    MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(deadband(forwardSpeed), deadband(rightSpeed), deadband(rotationSpeed), gyroAngle);
    
    frontLeft.set(speeds.frontLeft);
    rearLeft.set(speeds.rearLeft);
    frontRight.set(speeds.frontRight);
    rearRight.set(speeds.rearRight);
  }

  public void TankDrive(double forwardSpeed, double rotationSpeed) {

    DifferentialDrive.WheelSpeeds speeds = DifferentialDrive.arcadeDriveIK(deadband(forwardSpeed), deadband(rotationSpeed), true);

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
