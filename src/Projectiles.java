import java.util.*;

public class Projectiles extends Thread
{
  boolean pause=false;
  boolean end = false;
  ArrayList<Projectile> projectiles=new ArrayList<Projectile>();
  ArrayList<Projectile> toRemove = new ArrayList<Projectile>();
  ArrayList<Projectile> toAdd = new ArrayList<Projectile>();
  
  public void run()
  {
    while (!end)
    {
      //System.out.println("Running");
      if (!pause)
      {
        for (Projectile p:projectiles)
        {
          p.update();
          if (p.range<1)
            toRemove.add(p);
        }
        for (Projectile p:toRemove)
        {
          projectiles.remove(p);
        }
        projectiles.addAll(toAdd);
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