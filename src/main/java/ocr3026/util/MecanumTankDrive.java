package ocr3026.util;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

import ocr3026.util.Toggle;

public class MecanumTankDrive {
  MotorController frontLeft, rearLeft, tankLeft, frontRight, rearRight, tankRight;
  Solenoid leftSolenoid, rightSolenoid;

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
  
  public void MecanumRobotCentric() {
    leftSolenoid.set(false);
    rightSolenoid.set(false);
    
  }

  public void MecanumFieldCentric() {
    leftSolenoid.set(false);
    rightSolenoid.set(false);
  }

  public void TankDrive() {
    leftSolenoid.set(true);
    rightSolenoid.set(true);
  }
}
