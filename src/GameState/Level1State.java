package GameState;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

import Main.GamePanel;
import TileMap.*;
import Entity.*;

public class Level1State extends GameState
{
	
	private TileMap tileMap;
	private Background bg;
	private Player player;
	private boolean pause = false;
	private boolean firstBlock = false;
	private boolean secondBlock = false;
	public int counter = 0;
	public double timer = 0;
	private int currentChoice = 0;
	private String[] options = {"Resume","Instructions", "Quit"};
	
	public Level1State (GameStateManager gsm)
	{
		this.gsm = gsm;
		init();
	}
	
	public void init ()
	{
		tileMap = new TileMap (30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap ("/Maps/level1-1.map");
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
			timer++;
			System.out.println (player.getx());
		}
		// First Block Movement
		if (631 >= player.getx() && player.getx() >= 580)
			firstBlock = true;		
		else
			firstBlock = false;
		
		// Second Block Movement 
		if (1161 >= player.getx() && player.getx() >= 1100)
			secondBlock = true;
		else
			secondBlock = false;
		
		// Next Level
		if (player.getx() >= 2420)
		{
			try
			{
				PrintWriter pw = new PrintWriter(new FileWriter("LevelOneTime.hs"));
				pw.println(timer);
				pw.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			gsm.setState(GameStateManager.LEVEL2STATE);
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
		
		g.drawString(Double.toString(timer / 100), 20, 20);
		
		// First Block Animation
		if (firstBlock == true)
		{
			g.drawString ("Change into the negative charge to repel this block", 20, 60);
			if (player.getNegative() == true)
			{
				if (counter == 0)
				{
					tileMap.loadMap ("/Maps/level1-2.map");
					counter++;
				}
				else if (counter == 1)
				{
					tileMap.loadMap ("/Maps/level1-3.map");
					counter++;
				}
				else
				{
					tileMap.loadMap ("/Maps/level1-4.map");
					firstBlock = false;
				}
			}
		}
		
		// Second Block Animation
		if (secondBlock == true)
		{
			g.drawString ("Change into the positive charge to repel this block", 20, 60);
			if (player.getNegative() == false)
			{
					tileMap.loadMap ("/Maps/level1-5.map");
					secondBlock = false;
			}
		}
		
		if (player.getx() > 2100)
			g.drawString("Move to the edge of the scren to exit the level.", 20, 60);
		
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
