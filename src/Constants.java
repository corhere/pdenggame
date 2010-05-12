public class Constants
{
  public static final int GRAVITY =5;
  public static final int MAXJUMP = 100;
  public static final int DETECTION_DISTANCE = 150;
  
  public static double distanceBetween(int x1, int y1, int x2, int y2)
  {
    return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
  }
  
}