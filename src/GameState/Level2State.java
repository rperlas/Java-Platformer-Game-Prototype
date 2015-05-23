package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.PrintWriter;

import Main.GamePanel;
import TileMap.*;
import Entity.*;

public class Level2State extends GameState
{
	
	private TileMap tileMap;
	private Background bg;
	private Player player;
	private boolean firstBlock;
	private boolean secondBlock;
	private boolean thirdBlock;
	private boolean fourthBlock;
	private boolean fifthBlock;
	private boolean pause = false;
	public double timer = 0;
	private int currentChoice = 0;
	private String[] options = {"Resume","Instructions", "Quit"};
	
	public Level2State (GameStateManager gsm) 
	{
		this.gsm = gsm;
		init();
	}
	
	public void init ()
	{
		tileMap = new TileMap (30);
		tileMap.loadTiles("/Tilesets/grasstileset2.gif");
		tileMap.loadMap ("/Maps/level2-1.map");
		tileMap.setPosition (0,0);
		
		bg = new Background ("/Backgrounds/forestbg.gif", 0.1);
		player = new Player (tileMap);
		player.setPosition(100, 100);
	}
	
	public void update ()
	{
		// Update Player
		if (pause == false)
		{
			player.update();
			tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
			System.out.println (player.getx());
			timer++;
		} 
		
		// First Block Movement
		if (380 >= player.getx() && player.getx() >= 330)
			firstBlock = true;		
		else
			firstBlock = false;
		
		// Second Block Movement
		if (890 >= player.getx() && player.getx() >= 860)
			secondBlock = true;
		else
			secondBlock = false;
		
		// Third Block Movement
		if (1380 >= player.getx() && player.getx() >= 1355)
			thirdBlock = true;
		else
			thirdBlock = false;
		
		// Fourth Block Movement
		if (1550 >= player.getx() && player.getx() >= 1540)
			fourthBlock = true;
		else
			fourthBlock = false;
		
		// Fifth Block Movement
		if (1730 >= player.getx() && player.getx() >= 1720)
			fifthBlock = true;
		else
			fifthBlock = false;
		
		// Next Level
		if (player.getx() >= 2420)
		{
			try
			{
				PrintWriter pw = new PrintWriter(new FileWriter("LevelTwoTime.hs"));
				pw.println(timer);
				pw.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			gsm.setState(GameStateManager.LEVEL3STATE);
		}
			
	}
	
	public void draw (Graphics2D g)
	{
		// Draw Background
		bg.draw(g);
		
		// Draw TileMap
		tileMap.draw(g);
		
		//Draw Player
		player.draw(g);
		
		// First Block Animation
		if (firstBlock == true && player.getNegative() == false)
		{
			tileMap.loadMap ("/Maps/level2-2.map");
			firstBlock = false;
		}
		
		// Second Block Animation
		if (secondBlock == true && player.getNegative() == true)
		{
			tileMap.loadMap ("/Maps/level2-3.map");
			secondBlock = false;
		}
		
		// Third Block Animation
		if (thirdBlock == true && player.getNegative() == false)
		{
			tileMap.loadMap ("/Maps/level2-4.map");
			thirdBlock = false;
		}
		
		// Fourth Block Animation
		if (fourthBlock == true && player.getNegative() == true)
		{
			tileMap.loadMap ("/Maps/level2-5.map");
			fourthBlock = false;
		}
		
		// Fifth Block Animation
		if (fifthBlock == true && player.getNegative() == false)
		{
			tileMap.loadMap("/Maps/level2-6.map");
			fifthBlock = false;
		}
		
		g.drawString(Double.toString(timer / 100), 20, 20);
		
		if (pause == true)
		{
			for (int i = 0; i < options.length; i++)
			{
				if (i == currentChoice)
				{
					g.setColor (Color.BLACK);
				}
				else
				{
					g.setColor (Color.RED);
				}
				g.drawString (options[i], 145, 100 + i * 15);
			}
		}
	}
	
	public void keyPressed (int k)
	{
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(true);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(true);
		if (k == KeyEvent.VK_DOWN && pause == false)
		{
			if (player.getNegative() == false)
				player.setNegative(true);
			else
				player.setNegative(false);
		}
		if (k == KeyEvent.VK_UP)
			player.setJumping(true);
		if (k == KeyEvent.VK_G)
			player.setGliding (true);
		if (k == KeyEvent.VK_P)
		{
			if (pause == false)
				pause = true;
			else
				pause = false;
		}
		if (pause == true)
		{
			if (k == KeyEvent.VK_UP)
			{
				if (currentChoice == 0)
					currentChoice = 2;
				else
					currentChoice--;
			}
			if (k == KeyEvent.VK_DOWN)
			{
				if (currentChoice == (options.length - 1))
					currentChoice = 0;
				else
					currentChoice++;
			}
			if (k == KeyEvent.VK_ENTER && currentChoice == 0)
				pause = false;
			if (k == KeyEvent.VK_ENTER && currentChoice == 2)
			{	
				pause = false;
				timer = 0;
				gsm.setState(GameStateManager.MENUSTATE);
			}
		}
	}
	
	public void keyReleased (int k)
	{
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if (k == KeyEvent.VK_UP)
			player.setJumping(false);
		if (k == KeyEvent.VK_G)
			player.setGliding (false);
	}

}
