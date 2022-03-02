package ocr3026.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.kauailabs.navx.frc.AHRS;
import frc.robot.Robot;
import java.lang.Math;

public class MecanumTankDrive {
  private static final Joystick joystick = Robot.joystick;
  private final MotorController frontLeft, rearLeft, tankLeft, frontRight, rearRight, tankRight;
  private final DoubleSolenoid solenoid;
  private double deadband = 0;
  public static final AHRS gyroscope = Robot.gyroscope;
  private double theta = (java.lang.Math.atan(joystick.getY()/joystick.getX()))  * (180 / java.lang.Math.PI);  
  private double error =  theta -gyroscope.getYaw();

  public double deadband(double value) {
    if(value > deadband) {
      return (value - deadband) / (1 - deadband);
    } else if(value < -deadband) {
      return (value + deadband) / (1 - deadband);
    } else {
      return 0;
    }
  }

  public MecanumTankDrive(MotorController frontLeft, MotorController rearLeft, MotorController tankLeft, MotorController frontRight, MotorController rearRight, MotorController tankRight, DoubleSolenoid solenoid) {
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

    forwardSpeed = deadband(forwardSpeed);
    rightSpeed = deadband(rightSpeed);
    rotationSpeed = deadband(rotationSpeed);

    forwardSpeed = forwardSpeed * Math.abs(forwardSpeed);
    rightSpeed = rightSpeed * Math.abs(rightSpeed);
    rotationSpeed = (rotationSpeed * Math.abs(rotationSpeed)) + error;

    MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(forwardSpeed, rightSpeed, rotationSpeed, 0);
    
    frontLeft.set(
      speeds.frontLeft);
    tankLeft.set(speeds.rearLeft);
    rearLeft.set(speeds.rearLeft);
    frontRight.set(speeds.frontRight);
    tankRight.set(speeds.rearRight);
    rearRight.set(speeds.rearRight);
  }

  public void MecanumFieldCentric(double forwardSpeed, double rightSpeed, double rotationSpeed, double gyroAngle) {
    solenoid.set(DoubleSolenoid.Value.kReverse);

    forwardSpeed = deadband(forwardSpeed);
    rightSpeed = deadband(rightSpeed);
    rotationSpeed = deadband(rotationSpeed);

    forwardSpeed = forwardSpeed * Math.abs(forwardSpeed);
    rightSpeed = rightSpeed * Math.abs(rightSpeed);
    rotationSpeed = rotationSpeed * Math.abs(rotationSpeed);

    MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(forwardSpeed, rightSpeed, rotationSpeed, gyroAngle);
    
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
