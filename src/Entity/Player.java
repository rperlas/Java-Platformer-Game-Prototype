package Entity;

import TileMap.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/** 
 *	The "Player" class contains all the variables and methods which directly relate to the Player
 *  object. It is responsible for loading the player sprite, implementing a projectile motion based
 *  physics engine and continuously drawing and updating the player character.
 *  
 *  @author Ronald Perlas
 *  @author ForeignGuyMike
 *  @version 5.0
 */
public class Player extends MapObject
{
	// Player Variables
	
	/*
	 * health int Stores the player's health level.
	 */
	private int health;
	
	/*
	 * maxHealth int Stores the player's maximum health level.
	 */
	private int maxHealth;
	
	/*
	 * fire int Stores the power level of the fireball attack.
	 */
	private int fire;
	
	/*
	 * maxFire int Stores the max power level of the fireball atack.
	 */
	private int maxFire;
	
	/*
	 * dead boolean Stores the life/death state of the player.
	 */
	private boolean dead;
	
	/*
	 * flinching boolean Stores the flinching state of the player.
	 */
	private boolean flinching;
	
	/*
	 * flinchTimer long Stores the time it takes to flinch.
	 */
	private long flinchTimer;
	
	// Fireball
	
	/*
	 * firing boolean Stores the firing state of the player.
	 */
	private boolean firing;
	
	/*
	 * fireCost int Stores the energy cost of firing a fireball.
	 */
	private int fireCost;
	
	/*
	 * fireBallDamage int Stores the damage of a fireball.
	 */
	private int fireBallDamage;

	// Scratch
	/*
	 * scratching boolean Stores the scratching state of the player.
	 */
	
	private boolean scratching;
	/*
	 * scratchDamage int Stores the damage of a scratch attack.
	 */
	private int scratchDamage;
	
	/*
	 * scratchRange int Stores the range of the scratch attack.
	 */
	private int scratchRange;
	
	// Gliding
	/*
	 * gliding boolean Stores the gliding state of the player.
	 */
	private boolean gliding;
	
	// Magnetic Charge
	/*
	 * negative boolean Stores the positive/negative charge of the player.
	 */
	private boolean negative = false;
	
	// Animations
	/*
	 * sprites Reference References an ArrayList of BufferedImage objects for the player sprite.
	 */
	private ArrayList<BufferedImage[]> sprites;
	
	/*
	 * numFrames int[] Stores an array of frames for each player state, derived from spritesheet.
	 */
	private final  int[] numFrames = {2, 8, 1, 2, 4, 2, 5, 2, 8, 1, 2, 4, 2, 5};
	
	// Actions
	
	/*
	 * IDLE int Represents the idle player state.
	 */
	private static final int IDLE = 0;
	
	/*
	 * WALKING int Represents the walking player state.
	 */
	private static final int WALKING = 1;
	
	/*
	 * JUMPING int Represents the jumping player state.
	 */
	private static final int JUMPING = 2;
	
	/*
	 * FALLING int Represents the falling player state.
	 */
	private static final int FALLING = 3;
	
	/* 
	 * GLIDING int Represents the gliding player state.
	 */
	private static final int GLIDING = 4;
	
	/*
	 * FIREBALL int Represents the firing player state.
	 */
	private static final int FIREBALL = 5;
	
	/*
	 * SCRATCHING int Represents the scratching player state. 
	 */
	private static final int SCRATCHING = 6;
	
	/*
	 * PIDLE int Represents the positively charged idle player state.
	 */
	private static final int PIDLE = 7;
	
	/*
	 * PWALKING int Represents the positively charged walking player state.
	 */
	private static final int PWALKING = 8;
	
	/*
	 * PJUMPING int Represents the positively charged jumping player state.
	 */
	private static final int PJUMPING = 9;
	
	/*
	 * PFALLING int Represents the positively charged falling player state.
	 */
	private static final int PFALLING = 10;
	
	/*
	 * PGLIDING int Represents the positively charged gliding player state.
	 */
	private static final int PGLIDING = 11;
	
	/*
	 * PFIREBALL int Represents the positively charged firing player state.
	 */
	private static final int PFIREBALL = 12;
	
	/*
	 * PSCRATCHING int Represents the positively charged scratching player state.
	 */
	private static final int PSCRATCHING = 13;
	
	/*
	 * This constructor is responsible for initializing the variables inherited from the abstract 
	 * parent class, MapObject. It is also responsible for reading the spritesheet image and 
	 * loading the appropriate portions to the right variables.
	 * 
	 * @param spritesheet Reference References the BufferedImage of the spritesheet.
	 * @param bi Reference References the BufferedImage class.
	 * @param e Reference Stores the exception to be handled.
	 */
	public Player (TileMap tm)
	{
		super(tm);
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		tangible = true;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -3.9;
		stopJumpSpeed = 0.3;
		facingRight = true;
	    
		health = maxHealth = 5;
		fire = maxFire = 2500;
		fireCost = 200;
		fireBallDamage = 5;
		scratchDamage = 8;
		scratchRange = 40;
		
		// Load Sprites
		try
		{
			BufferedImage spritesheet = ImageIO.read (getClass().getResourceAsStream("/Sprites/Player/playersprites2.gif"));
			sprites = new ArrayList<BufferedImage[]>(); 
			
			for (int i = 0; i < 14; i++)
			{
				BufferedImage [] bi = new BufferedImage [numFrames[i]];
				for (int j = 0; j < numFrames [i]; j++)
				{
					if (i != 6 && i!= 13)
					{
						bi [j] = spritesheet.getSubimage(j * width, i * height, width, height);
					}
					else
					{
						bi [j] = spritesheet.getSubimage(j * width * 2, i * height, width, height);
					}
				}
				sprites.add(bi);
					
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		animation = new Animation();	
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
	}
	
	/*
	 * This method returns the health of the player.
	 */
	public int getHealth ()
	{
		return health;
	}
	
	/*
	 * This method returns the maximum health of the player.
	 */
	public int getMaxHealth ()
	{
		return maxHealth;
	}
	
	/*
	 * This method returns the fire level of the player.
	 */
	public int getFire ()
	{
		return fire;
	}
	
	/*
	 * This method returns the maximum fire level of the player.
	 */
	public int getMaxFire ()
	{
		return maxFire;
	}
	
	/*
	 * This method returns the charge of the player.
	 */
	public boolean getNegative()
	{
		return negative;
	}
	
	/*
	 * This method sets the player to the firing state.
	 */
	public void setFiring ()
	{
		firing = true;
	}
	
	/*
	 * This method sets the player to the scratching state.
	 */
	public void setScratching ()
	{
		scratching = true;
	}
	
	/*
	 * This method sets the player to the gliding state.
	 */
	public void setGliding (boolean b)
	{
		gliding = b;
	}
	
	/*
	 * This method sets the charge of the player.
	 */
	public void setNegative (boolean c)
	{
		negative = c;
	}
	
	/*
	 * This method calculates the next position of the player on the tilemap.
	 */
	private void getNextPosition ()
	{
		// Movement
		if (left)
		{
			dx -= moveSpeed;
			if (dx < -maxSpeed)
				dx = -maxSpeed;
		}
		else if (right)
		{
			dx += moveSpeed;
			if (dx > maxSpeed)
				dx = maxSpeed;				
		}
		else
		{
			if (dx > 0)
			{
				dx -= stopSpeed;
				if (dx < 0)
					dx = 0;
			}
			else if (dx < 0)
			{
				dx += stopSpeed;
				if (dx > 0)
					dx = 0;
			}
		}
		
		// Cannot Attack While Moving
		if ((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling))
		{
			dx = 0;
		}
		
		// Jumping
		if (jumping && !falling)
		{
			dy = jumpStart;
			falling = true;
		}
		
		// Falling
		if (falling)
		{
			if (dy > 0 && gliding)
				dy += fallSpeed * 0.1;
			else
				dy += fallSpeed;
			
			if (dy > 0)
				jumping = false;
			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;
			if (dy > maxFallSpeed)
				dy = maxFallSpeed;	
		}
	}
	
	/*
	 * This method updates the player's location on the tilemap.
	 */
	public void update ()
	{
		// Update Position
		getNextPosition();
		checkTileMapCollision();
		setPosition (xtemp, ytemp);
		
		// Set Animation
		if (negative)
		{
			if (scratching)
			{
				if (currentAction != SCRATCHING)
				{
					currentAction = SCRATCHING;
					animation.setFrames (sprites.get(SCRATCHING));
					animation.setDelay (50);
					width = 60;
				}
			}
			else if (firing)
			{
				if (currentAction != FIREBALL)
				{
					currentAction = FIREBALL;
					animation.setFrames (sprites.get(FIREBALL));
					animation.setDelay (100);
					width = 30;
				}
			}
			else if (dy > 0)
			{
				if (gliding)
				{
					if (currentAction != GLIDING)
					{
						currentAction = GLIDING;
						animation.setFrames (sprites.get(GLIDING));
						animation.setDelay (100);
						width = 30;
					}
				}
				else if (currentAction != FALLING)
				{
					currentAction = FALLING;
					animation.setFrames (sprites.get(FALLING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if (dy < 0)
			{
				if (currentAction != JUMPING)
				{
					currentAction = JUMPING;
					animation.setFrames (sprites.get(JUMPING));
					animation.setDelay(-1);
					width = 30;
				}
			}
			else if (left || right)
			{
				if (currentAction != WALKING)
				{
					currentAction = WALKING;
					animation.setFrames (sprites.get(WALKING));
					animation.setDelay(40);
					width = 30;
				}
			}
			else 
			{
				if (currentAction != IDLE)
				{
					currentAction = IDLE;
					animation.setFrames (sprites.get(IDLE));
					animation.setDelay(400);
					width = 30;
				}
			}
		}
		else
		{
			if (scratching)
			{
				if (currentAction != PSCRATCHING)
				{
					currentAction = PSCRATCHING;
					animation.setFrames (sprites.get(PSCRATCHING));
					animation.setDelay (50);
					width = 60;
				}
			}
			else if (firing)
			{
				if (currentAction != PFIREBALL)
				{
					currentAction = PFIREBALL;
					animation.setFrames (sprites.get(PFIREBALL));
					animation.setDelay (100);
					width = 30;
				}
			}
			else if (dy > 0)
			{
				if (gliding)
				{
					if (currentAction != PGLIDING)
					{
						currentAction = PGLIDING;
						animation.setFrames (sprites.get(PGLIDING));
						animation.setDelay (100);
						width = 30;
					}
				}
				else if (currentAction != PFALLING)
				{
					currentAction = PFALLING;
					animation.setFrames (sprites.get(PFALLING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if (dy < 0)
			{
				if (currentAction != PJUMPING)
				{
					currentAction = PJUMPING;
					animation.setFrames (sprites.get(PJUMPING));
					animation.setDelay(-1);
					width = 30;
				}
			}
			else if (left || right)
			{
				if (currentAction != PWALKING)
				{
					currentAction = PWALKING;
					animation.setFrames (sprites.get(PWALKING));
					animation.setDelay(40);
					width = 30;
				}
			}
			else 
			{
				if (currentAction != PIDLE)
				{
					currentAction = PIDLE;
					animation.setFrames (sprites.get(PIDLE));
					animation.setDelay(400);
					width = 30;
				}
			}
		}
		
		animation.update();
		
		// Set Direction
		if (currentAction != SCRATCHING && currentAction != FIREBALL)
		{
			if (right) 
				facingRight = true;
			if (left)
				facingRight = false;
		}
	}
	
	/*
	 * This method draws the player sprite's graphics onto the screen.
	 */
	public void draw (Graphics2D g)
	{
		setMapPosition();
		
		// Draw Player
		if (flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0)
				return;
		}
		
		if (facingRight)
		{
			g.drawImage (
					animation.getImage(),
					(int) (x + xmap - width / 2),
					(int) (y + ymap - height / 2),
					null
					);
		}
		else
		{
			g.drawImage (
					animation.getImage(),
					(int) (x + xmap - width / 2 + width),
					(int) (y + ymap - height / 2),
					-width,
					height,
					null
					);
		}
	}
}



// END
