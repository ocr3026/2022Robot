package ocr3026.util;

public class Deadband {
  public static double deadband(double value, double deadband) {
    if(value > deadband) {
      return (value - deadband) / (1 - deadband);
    } else if(value < -deadband) {
      return (value + deadband) / (1 - deadband);
    } else {
      return 0;
    }
  }
}
