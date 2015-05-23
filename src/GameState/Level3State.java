package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.PrintWriter;

import Main.GamePanel;
import TileMap.*;
import Entity.*;

public class Level3State extends GameState
{
	
	private TileMap tileMap;
	private Background bg;
	private Player player;
	private boolean firstBlock;
	private boolean secondBlock;
	private boolean thirdBlock;
	private boolean fourthBlock;
	private boolean fifthBlock;
	private boolean sixthBlock;
	private boolean seventhBlock;
	private boolean eigthBlock;
	private boolean keyOne = false;
	private boolean keyTwo = false;
	private boolean occurred = false;
	private int counter = 0;
	private boolean pause = false;
	public double timer = 0;
	private int currentChoice = 0;
	private String[] options = {"Resume","Instructions", "Quit"};
	
	public Level3State (GameStateManager gsm)
	{
		this.gsm = gsm;
		init();
	}
	
	public void init ()
	{
		tileMap = new TileMap (30);
		tileMap.loadTiles("/Tilesets/grasstileset3.gif");
		tileMap.loadMap ("/Maps/level3-1.map");
		tileMap.setPosition (0,0);
		
		bg = new Background ("/Backgrounds/forestbg.gif", 0.1);
		player = new Player (tileMap);
		player.setPosition(200, 100);
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
		if (56 >= player.getx() && player.getx() >= 40)
		{
			firstBlock = true;		
			keyOne = true;
		}
		else
			firstBlock = false;
		
		// Second Block Movement
		if (320 >= player.getx() && player.getx() >= 301 && keyOne == true)
			secondBlock = true;		
		else
			secondBlock = false;
		
		// Third Block Movement
		if (590 >= player.getx() && player.getx() >= 575)
			thirdBlock = true;		
		else
			thirdBlock = false;

		// Fourth Block Movement
		if (890 >= player.getx() && player.getx() >= 790)
			fourthBlock = true;		
		else
			fourthBlock = false;
		
		// Fifth Block Movement
		if (1257 >= player.getx() && player.getx() >= 1240)
			fifthBlock = true;		
		else
			fifthBlock = false;
		
		// Sixth Block Movement
		if (1160 >= player.getx() && player.getx() >= 1150)
			sixthBlock = true;		
		else
			sixthBlock = false;

		// Seventh Block Movement
		if (1370 >= player.getx() && player.getx() >= 1355)
			seventhBlock = true;		
		else
			seventhBlock = false;
		
		// Eight Block Movement
		if (1610 >= player.getx() && player.getx() >= 1520)
			eigthBlock = true;		
		else
			eigthBlock = false;
		
		// Exit
		if (player.getx() >= 2420)
		{
			try
			{
				PrintWriter pw = new PrintWriter(new FileWriter("LevelThreeTime.hs"));
				pw.println(timer);
				pw.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			gsm.setState(GameStateManager.MENUSTATE);
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
			if (counter == 0)
				tileMap.loadMap ("/Maps/level3-2.map");
			else
			{
				tileMap.loadMap("/Maps/level3-3.map");
				firstBlock = false;
			}
			counter++;
		}
		
		// Second Block Animation
		if (secondBlock == true)
		{
			tileMap.loadMap("/Maps/level3-4.map");
			secondBlock = false;
		}

		// Third Block Animation
		if (thirdBlock == true && player.getNegative() == true)
		{
			tileMap.loadMap("/Maps/level3-5.map");
			thirdBlock = false;
		}
		
		// Fourth Block Animation
		if (fourthBlock == true)
		{
			g.drawString ("What is the charge of an electron?", 20, 60);
			if (player.getNegative() == true)
			{
				tileMap.loadMap("/Maps/level3-6.map");
				thirdBlock = false;
			}
		}
		
		// Fifth Block Animation
		if (fifthBlock == true && player.getNegative() == true && occurred == false)
		{
			tileMap.loadMap("/Maps/level3-7.map");
			occurred = true;
			fifthBlock = false;
		}
		
		// Sixth Block Animation
		if (sixthBlock == true && player.getNegative() == true && occurred == true)
		{
			tileMap.loadMap("/Maps/level3-8.map");
			keyTwo = true;
			sixthBlock = false;
		}

		// Seventh Block Animation
		if (seventhBlock == true && keyTwo == true)
		{
			tileMap.loadMap("/Maps/level3-9.map");
			seventhBlock = false;
		}
		
		// Eighth Block Animation
		if (eigthBlock == true && player.getNegative() == false)
		{
			tileMap.loadMap("/Maps/level3-10.map");
			eigthBlock = false;
		}		
		
		if (1610 >= player.getx() && player.getx() >= 1416)
			g.drawString("What is the charge of a proton?", 20, 60);

		
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
