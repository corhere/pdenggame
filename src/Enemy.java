public class Enemy
{
  int hp=50;
  int x;
  int y;
  int jumpHeight=100;
  int range=0;
  long activeMovement = System.currentTimeMillis();
  long activeDamage = System.currentTimeMillis();
  int speed; 
  int damage=5;
  int delay;
  boolean flying = false;
  boolean shooting = false;
  boolean detected = false;
  boolean right = false;
  int floatHeight =0;
  
  public Enemy(int x, int y, int speed, int delay, int range)
  {
    this.x=x;
    this.y=y;
    this.speed = speed;
    this.delay = delay;
    this.range = range;
  }
  
  public int X()
  {
    return x-range;
  }
  
  public int Y()
  {
    return y-range;
  }
  
  public void hit (int damage, long stun)
  {
    hp-=damage;
    activeMovement=System.currentTimeMillis()+stun;
    activeDamage=System.currentTimeMillis()+stun;
  }
   
 
}

