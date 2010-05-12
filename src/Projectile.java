public class Projectile
{
  double x,y;
  double speed;
  boolean right;
  int radius;
  int range;
  int damage;
  int knockback;
  
  public Projectile(int x, int y, boolean right)
  {
    this.x=x;
    this.y=y;
    this.right=right;
    this.radius=5;
    this.range=400;
    this.speed=10;
    damage = 1;
    knockback=0;
  }
  
  public Projectile(int x, int y, boolean right, int damage,int knockback)
  {
    this.x=x;
    this.y=y;
    this.right=right;
    this.radius=5;
    this.range=400;
    this.speed=10;
    this.damage=damage;
    this.knockback=knockback;
  }
  
  
  public void update()
  {
    range-=(int)speed;
    if (right)
      x+=speed;
    else
      x-=speed;
  }
}