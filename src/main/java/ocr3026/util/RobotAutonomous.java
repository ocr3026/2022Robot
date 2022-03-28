package ocr3026.util;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Timer;

import java.lang.Runnable;
import java.util.LinkedList;
import java.util.Queue;

public class RobotAutonomous {
  protected Timer timer = new Timer();

  private Queue<Pair<Runnable, Supplier<Boolean>>> stepQueue = new LinkedList<Pair<Runnable, Supplier<Boolean>>>();
  private Pair<Runnable, Supplier<Boolean>> currentStep;

  protected void addStep(Runnable onLoop, Supplier<Boolean> condition) {
    stepQueue.add(new Pair<Runnable, Supplier<Boolean>>(onLoop, condition));
  }

  public void init() {
    timer.start();
    currentStep = stepQueue.remove();
  }

  public void periodic() {
    if(currentStep != null && currentStep.second.get()) {
      currentStep.first.run();
    } else {
        if (!stepQueue.isEmpty()) {
          currentStep = stepQueue.remove();
          timer.reset();
          timer.start();
        }
        else {
          currentStep = null;
        }
    }
  }
}
