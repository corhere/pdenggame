import java.util.*;

public class Enemies extends Thread
{
  boolean pause=false;
  boolean end = false;
  ArrayList<Enemy> enemies=new ArrayList<Enemy>();
  ArrayList<Enemy> toRemove = new ArrayList<Enemy>();
  ArrayList<Enemy> toAdd = new ArrayList<Enemy>();
  GameWindow gw;
  
  public Enemies(GameWindow gw)
  {
    this.gw=gw;
  }
  
  public void update(Enemy e)
  {
    ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    projectiles.addAll(gw.projectiles.projectiles);
    for(Projectile p:projectiles)
    {
      if (Constants.distanceBetween(e.x,e.y,(int)p.x,(int)p.y)<(e.range+p.radius))
      {
        e.hit(p.damage,300);
        gw.projectiles.toRemove.add(p);
      }
    }
    
    if (e.activeMovement<System.currentTimeMillis())
    {
      if (e.detected)
      {
        if (e.x<Player.X()&&e.x+e.range<gw.getRightBound((int)(e.x+e.range/2.0+e.speed),e.y))
          e.x+=e.speed;
        else if (e.x>Player.X()&&e.x-e.range>gw.getLeftBound((int)(e.x-e.range/2.0-e.speed),e.y))
          e.x-=e.speed;
      }
      else
      {
        if (e.right)
        {
          
          e.x+=e.speed;
          if (!(e.x+e.range<gw.getRightBound((int)(e.x+e.range/2.0+e.speed),e.y)))
            e.right = false;
          
          if (e.x-Player.X()>0&&e.x-Player.X()<Constants.DETECTION_DISTANCE)
            e.detected = true;
        }
        else
        {
          
          e.x-=e.speed;
          if (!(e.x-e.range>gw.getLeftBound((int)(e.x-e.range/2.0-e.speed),e.y)))
            e.right = true;
          
          if (Player.X()-e.x>0&&Player.X()-e.x<Constants.DETECTION_DISTANCE)
            e.detected = true;
        }
      }
      if (!e.flying)
      {
        int height = gw.getHeight(e.x,e.y,e.range+e.floatHeight);
        if (height<e.y+e.range)
          e.y=height;
        else
          e.y+=Constants.GRAVITY;
      }
    }
    if (e.activeDamage<System.currentTimeMillis())
    {
      if (Constants.distanceBetween(e.x,e.y,Player.X(),Player.Y())<e.range)
      {
        Player.hp-=e.damage;
        e.activeDamage=System.currentTimeMillis()+e.delay;
      }
    }
    if (!e.flying&&e.Y()<gw.getHeight(e.x,e.y,e.range*2))
    {
      e.y+=Constants.GRAVITY;
    }
    
   
  }
  
  public void run()
  {
    while (!end)
    {
      //System.out.println("Running");
      if (!pause)
      {
        for (Enemy e:enemies)
        {
          update(e);
          if (e.hp<1)
            toRemove.add(e);
        }
        for (Enemy e:toRemove)
        {
          enemies.remove(e);
        }
        enemies.addAll(toAdd);
        toAdd.clear();
        toRemove.clear();
      }
      try
      {
        Thread.sleep(5);
      }
      catch (Exception e)
      {
      }
    }
  }
}