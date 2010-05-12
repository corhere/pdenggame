import java.awt.*;

public class Player
{
  static int hp=100;
  int ep=100;
  static int x;
  static int y;
  int jumpHeight=0;
  int range=50;
  static int width = 30;
  static int height = 50;
  boolean jumping;
  int speed=3;
  static boolean crouched;
  static boolean right;
  static int delayMillis=100;
  static int currentDamage=10;
  static int currentKnockback =5;
  static GameWindow gw=null;
  
  static long readyToFire=0;
  
  public void setWeapon(Weapon w)
  {
    delayMillis = w.delay;
    currentDamage = w.damage;
    currentKnockback = w.knockback;
  }
  
  public static int X()
  {
    return(x+(int)(width/2));
  }
  public static int Y()
  {
    return(y+(int)(height/2));
  }
  
  public static int height()
  {
    if (crouched)
      return height/2;
    else
      return height;
  }
  
  public static Projectile fire()
  {
    if (System.currentTimeMillis()<readyToFire)
    {
      return null;
    }
    else
    {
      readyToFire = System.currentTimeMillis()+delayMillis;
      Projectile p = new Projectile(X(),Y(),right,currentDamage,currentKnockback);
      if (right)
        p.range=gw.getRightBound(X(),Y())-X();
      else
        p.range=X()-gw.getLeftBound(X(),Y());
      for (int i=0;i<5;i++)
        p.update();
      
      return p;
    }
  }
}