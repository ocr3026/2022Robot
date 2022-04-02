package ocr3026.util.Autonomous;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Robot;
import ocr3026.util.MecanumTankDrive;
import ocr3026.util.OCRMath;
import ocr3026.util.RobotAutonomous;
import ocr3026.util.Vision;

public class twoBall extends RobotAutonomous {
	private final PIDController autoRotationPID = new PIDController(.2, 0, 0);
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

	public twoBall() {
		addStep(() -> {
			vision.setVisionMode();
			intakeSolenoid.set(Value.kReverse);
		}, () -> {
			return !timer.hasElapsed(.3);
		});
				
		addStep(() -> {
			intake.set(.66);
		}, () -> {
			return !timer.hasElapsed(.2);
		});

		addStep(() -> {
			drivetrain.MecanumRobotCentric(OCRMath.Clamp(-0.4 * timer.get(), -0.4, 0), 0, 0, false);
		}, () -> {
			return !timer.hasElapsed(1.4);
		});

		addStep(() -> {
			drivetrain.MecanumRobotCentric(0, 0, 0);
		}, () -> {
			return !timer.hasElapsed(.1);
		});
		

		addStep(() -> {
			intake.set(0);
		}, () -> {
			return !timer.hasElapsed(.1);
		});

		addStep(() -> {
			if (vision.limelight.checkForTarget() == false) {
				drivetrain.MecanumRobotCentric(0, 0,.25, false);
			}
			else {
				gyroInRange = true;
			}
		}, () -> {
			return !gyroInRange;
		});
		addStep(() -> {
			isCentered = vision.centerTarget();
		}, () -> {
			return !isCentered && !timer.hasElapsed(1.5);
		});
		
		

		addStep(() -> {
			flywheel.set(vision.getFlywheelSpeed() + 0.05);
		}, () -> {
			return !timer.hasElapsed(1.5);
		});
		
		addStep(() -> {
			kickup.set(Value.kReverse);
		}, () -> {
			return !timer.hasElapsed(1.5);
		});
		addStep(() -> {
			kickup.set(Value.kForward);
		}, () -> {
			return !timer.hasElapsed(1);
		});
		addStep(() -> {
			intake.set(.8);
		}, () -> {
			return !timer.hasElapsed(2);
		});
		
		addStep(() -> {
			kickup.set(Value.kReverse);
		}, () -> {
			return !timer.hasElapsed(1.5);
		});
		addStep(() -> {
			kickup.set(Value.kForward);
		}, () -> {
			return !timer.hasElapsed(1);
		});
		addStep(() -> {
			flywheel.set(0);
		}, () -> {
			return !timer.hasElapsed(.5);
		});
		addStep(() -> {
			vision.setDriverMode();
		}, () -> {
			return !timer.hasElapsed(1);
		});		
		addStep(() -> {
			intake.set(0);
		}, () -> {
			return !timer.hasElapsed(.5);
		});
	}
}

