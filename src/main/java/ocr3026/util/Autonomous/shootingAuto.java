
	package ocr3026.util.Autonomous;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Robot;
import ocr3026.util.MecanumTankDrive;
import ocr3026.util.RobotAutonomous;
import ocr3026.util.Vision;

public class shootingAuto extends RobotAutonomous {
	private final MecanumTankDrive drivetrain = Robot.drivetrain;
	private final AHRS gyroscope = Robot.gyroscope;
	private final RelativeEncoder encoder = Robot.encoder;
	private final Vision vision = Robot.vision;
	private final  PIDController gyroscoperotation = Robot.gyroscoperotation;
	private final DoubleSolenoid intakeSolenoid = Robot.intakeSolenoid;
	private final DoubleSolenoid kickup = Robot.kickup;
	private final CANSparkMax intake = Robot.intake;
	private final CANSparkMax flywheel = Robot.flywheel;

	private boolean isCentered = false;
	private boolean inSweetSpot = false;
	private boolean gyroInRange = false;
	private boolean driveInRange  = false;
	private boolean drivenDistance = false;

	public shootingAuto() {
		addStep(() -> {
			drivetrain.MecanumRobotCentric(0.5, 0, 0, false);
		}, () -> {
			return !timer.hasElapsed(1.0);
		});
		addStep(() -> {
			drivetrain.MecanumRobotCentric(0, 0, 0);
		}, () -> {
			return !timer.hasElapsed(.5);
		});
		addStep(() -> {
			if ((vision.limelight.getTargetX() < -0.3 || vision.limelight.getTargetX() > 0.3)) {
				vision.setVisionMode();
				vision.centerTarget();
			}
			else  {
				isCentered = true;
				vision.setDriverMode();
			}
	
		}, () -> {
			return !isCentered;
		});
		
		/*addStep(() -> {
			if (vision.limelight.getTargetY() > (vision.sweetSpot + 0.01) || vision.limelight.getTargetY() < (vision.sweetSpot - 0.01)) {
				vision.goToSweetSpot();
			}
			else {
				vision.setDriverMode();
				inSweetSpot = true;
			}
		}, () -> {
			return !inSweetSpot;
		}); */

		addStep(() -> {
			flywheel.set(.75);
		}, () -> {
			return !timer.hasElapsed(1);
		});
		
		addStep(() -> {
			kickup.set(Value.kForward);
		}, () -> {
			return !timer.hasElapsed(.5);
		});
				
		addStep(() -> {
			kickup.set(Value.kReverse);
		}, () -> {
			return !timer.hasElapsed(.5);
		});
		addStep(() -> {
			flywheel.set(0);
		}, () -> {
			return !timer.hasElapsed(.5);
		});
	}
}

