import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;

//If performance starts dropping when images are added and stuff, limit the range when looking for boundaries
public class GameWindow extends JFrame implements KeyListener
{
	private static final long serialVersionUID = 1L;
	final int HEIGHT = 400;
	final int WIDTH = 600;
	Graphics g;
	BufferedImage buffer;
	ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	Enemies enemies;
	boolean left, right, up, down, space;
	int rightBound=WIDTH;
	int leftBound=0;
	int bufferSpace = (int)(WIDTH/2);
	int cameraOffset =0;
	Player player;
	int LEVEL_LENGTH;
	BufferedImage levelGuide;
	BufferedImage levelBackground;
	Color yColor = Color.black;
	Color xColor = new Color(0,0,255);
	Color enemyColor = Color.red;
	Projectiles projectiles = new Projectiles();
	int cameraSpeed = 3;

	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode()==KeyEvent.VK_LEFT)
			left=true;
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
			right=true;
		if(e.getKeyCode()==KeyEvent.VK_UP)
			up=true;
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			down=true;
			player.crouched=true;
		}
		if (e.getKeyCode()==KeyEvent.VK_SPACE)
		{
			space=true;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode()==KeyEvent.VK_LEFT)
			left=false;
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
			right=false;
		if(e.getKeyCode()==KeyEvent.VK_UP)
			up=false;
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			down=false;
			player.crouched=false;
		}
		if (e.getKeyCode()==KeyEvent.VK_SPACE)
		{
			space=false; 
		}
	}

	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();

		if (c=='r')
		{
			new GameWindow(LEVEL_LENGTH);
			this.dispose();
		}
	}

	public void controls()
	{
		if (player==null)
			return;
		if (player.y<getHeight(player.X(),player.Y(),player.height()))
		{
			player.y+=Constants.GRAVITY;
		}
		else
		{
			player.jumping=false;
			player.jumpHeight=0;
		}
		if (!player.jumping&&player.Y()>getHeight(player.X(),player.Y(),player.height()))
			player.y=getHeight(player.X(),player.Y(),player.height());
		if (up&&!player.jumping)
		{
			player.jumping=true;
			player.crouched=false;
		}

		if (player.jumping)
		{
			if (player.jumpHeight<Constants.MAXJUMP)
			{
				player.y-=10;
				player.jumpHeight+=10;
			}
		}
		if (!player.crouched)
		{
			if (right&&player.X()<-5+getRightBound(player.X(),player.Y()))
			{
				player.x+=player.speed;
				player.right=true;
			}
			if (left&&player.X()>5+getLeftBound(player.X(),player.Y())&&player.X()>5+leftBound)
			{
				player.x-=player.speed;
				player.right=false;
			}
		}

		if (space)
		{
			Projectile p = player.fire();
			if (p!=null)
			{
				projectiles.toAdd.add(p);
			}

		}

		processOffset();
	}

	public void processOffset()
	{
		if (player.x>WIDTH-bufferSpace+cameraOffset)
		{
			cameraOffset +=cameraSpeed;
			rightBound +=cameraSpeed;
			leftBound +=cameraSpeed;
			try
			{
				if (levelGuide.getRGB(leftBound,HEIGHT-5)==enemyColor.getRGB())
				{
					enemies.toAdd.add(new Enemy(leftBound,getHeight(leftBound,5,100),1,100,25));
				}
			}
			catch(Exception e)
			{
				System.out.println("Enemy left marker outside of range "+leftBound);
			}
			try
			{
				if (levelGuide.getRGB(rightBound,5)==enemyColor.getRGB())
				{
					enemies.toAdd.add(new Enemy(rightBound,getHeight(rightBound,5,100),1,100,25));
				}
			}
			catch(Exception e)
			{
				System.out.println("Enemy right marker outside of range "+rightBound);
			}
		}
	}

	public void paint (Graphics g)
	{
		if (player==null||buffer==null)
		{
			return;//todo: actual loading mechanism
		}
		controls();
		Graphics gi = buffer.createGraphics();
		gi.drawImage(levelBackground,0,0,null);
		gi.setColor(Color.red);

		gi.fillRect(player.x,player.y,player.width,player.height());

		ArrayList <Projectile> tempProjectiles = new ArrayList<Projectile>();
		tempProjectiles.addAll(projectiles.projectiles);
		gi.setColor(Color.red);
		for (Projectile p:tempProjectiles)
		{
			gi.fillOval((int)(p.x-p.radius/2),(int)(p.y-p.radius/2),p.radius,p.radius);
		}
		tempProjectiles.clear();
		ArrayList<Enemy> tempEnemies = new ArrayList<Enemy>();
		tempEnemies.addAll(enemies.enemies);
		gi.setColor(Color.green);
		for (Enemy e:tempEnemies)
		{
			gi.setColor(Color.green);
			gi.fillOval((e.X()),(e.Y()),e.range*2,e.range*2);

			gi.setColor(Color.black);
			gi.fillOval((int)(e.x-5),(int)(e.y-5),10,10);
		}
		tempEnemies.clear();

		gi.setColor(Color.black);
		gi.drawString("HP: "+Player.hp,cameraOffset,40);


		Graphics2D g2= (Graphics2D)g;
		g2.drawImage(buffer,null,-cameraOffset,0);

		try
		{
			Thread.sleep (10);
		}
		catch (Exception e)
		{
		}
		repaint();
	}

	public void loadImages()
	{
		try
		{
			levelGuide=ImageIO.read(new File("testlevelGuide.bmp"));
			levelBackground = ImageIO.read(new File("testlevelBackground.bmp"));
			rightBound = WIDTH;
		}
		catch(IOException e)
		{
		}
	}

	public int getHeight(int x, int y, int inputHeight)
	{

		int height=y;
		if(levelGuide==null)
			return height;
		for (int i=y;i<HEIGHT;i++)
		{
			if ( levelGuide.getRGB(x,i)==yColor.getRGB())
			{
				return i-inputHeight;
			}
		}
		return y;
	}

	public int getRightBound(int x,int y)
	{

		if(levelGuide==null)
			return LEVEL_LENGTH;
		for (int i=x;i<LEVEL_LENGTH;i++)
		{
			if (levelGuide.getRGB(i,y)==xColor.getRGB())
			{
				return i;
			}
		}
		return LEVEL_LENGTH;
	}

	public int getLeftBound(int x,int y)
	{
		if(levelGuide==null)
			return 0;
		for (int i=x;i>0;i--)
		{
			if ( levelGuide.getRGB(i,y)==xColor.getRGB())
			{
				return i;
			}
		}
		return 0;
	}

	public GameWindow(int length)
	{
		setVisible(true);
		setSize(WIDTH,HEIGHT);
		addKeyListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		player = new Player();
		enemies = new Enemies(this);
		player.gw=this;
		player.x=300;
		LEVEL_LENGTH=length;
		loadImages();
		projectiles.start();
		enemies.start();
		buffer = new BufferedImage (length,HEIGHT,BufferedImage.TYPE_INT_RGB);
		repaint();

	}


	public static void main (String[]args)
	{
		new GameWindow(2400);
	}
} 
