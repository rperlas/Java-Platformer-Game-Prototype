package Entity;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.Rectangle;

/*
 *	The "MapObject" class is an abstract class containing the methods and variables common to all
 *  the objects on the TileMap. It contains the methods which every one of these objects inherit 
 *  and use as well as variables to be defined individually.
 */
public abstract class MapObject 
{
	// Tile Variables
	
	/*
	 * tileMap Reference References the TileMap class. 
	 */
	protected TileMap tileMap;
	/*
	 * tileSize int Stores the size of a tile.
	 */
	protected int tileSize;
	/*
	 * xmap double Stores the size of the map along the x-axis.
	 */
	protected double xmap;
	/*
	 * ymap double Stores the size of the map along the y-axis.
	 */
	protected double ymap;
	
	// Position and Vector
	
	/*
	 * x double Stores the x value of the MapObject.
	 */
	protected double x;
	/*
	 * y double Stores the y value of the MapObject.
	 */
	protected double y;
	/*
	 * dx double Stores the dx value of the MapObject.
	 */
	protected double dx;
	/*
	 * dy double Stores the dy value of the MapObject.
	 */
	protected double dy;
	
	// Dimensions
	
	/*
	 * width int Stores the width of the MapObject.
	 */
	protected int width;
	/*
	 * height int Stores the height of the MapObject.
	 */
	protected int height;
	
	// Collison Box
	
	/*
	 * cwidth int Stores the cwidth of the MapObject.
	 */
	protected int cwidth;
	/*
	 * cheight int Stores the cheight of the MapObject.
	 */
	protected int cheight;
	
	// Collision Variables
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	protected boolean tangible;
	
	// Animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	// Movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// Movement Attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	/*
	 * This constructor sets up the TileMap for objects to be placed in as well
	 * as the size of each individual tile.
	 */
	public MapObject (TileMap tm)
	{
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	/*
	 * This method returns whether or not one MapObject intersects another MapObject.
	 * 
	 * @param r1 Reference References the related Rectangle object of a specific MapObject.
	 * @param r2 Reference References the related Rectangle object of another MapObject.
	 */
	public boolean intersect (MapObject o)
	{
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	/*
	 * This method returns the Rectangle object related to a specific MapObject.
	 */
	public Rectangle getRectangle ()
	{
		return new Rectangle((int) x - cwidth, (int) y - cheight, cwidth, cheight);
	}
	
	/*
	 * This method calculates whether any of the corners are intersecting a blocked tile.
	 * 
	 * @param leftTile int Stores the location of the left tile.
	 * @param rightTile int Stores the location of the right tile.
	 * @param topTile int Stores the location of the top tile.
	 * @param bottom int Stores the location of the bottom tile.
	 * @param tl int Stores the type of the top left tile. 
	 * @param tr int Stores the type of the top right tile.
	 * @param bl int Stores the type of the bottom let tile.
	 * @param br int Stores the type of the bottom right tile.
	 * @param x double Stores the width of the current location.
	 * @param y double Stores the height of the current location.
	 */
	public void calculateCorners (double x, double y)
	{
		int leftTile = (int) (x - cwidth / 2) / tileSize;
		int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cheight / 2) / tileSize;
		int bottom = (int) (y + cheight / 2 - 1) / tileSize;
		
		int tl = tileMap.getType (topTile, leftTile);
		int tr = tileMap.getType (topTile, rightTile);
		int bl = tileMap.getType (bottom, leftTile);
		int br = tileMap.getType(bottom, rightTile);
	
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}
	
	/*
	 * This method determines whether a collision has occurred.
	 */
	public void checkTileMapCollision ()
	{
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners (x, ydest);
		if (dy < 0)
		{
			if (topLeft || topRight)
			{
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else
			{
				ytemp += dy;
			}
		}
		if (dy > 0)
		{
			if (bottomLeft || bottomRight)
			{
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else
			{
				ytemp += dy;
			}
		}
		
		calculateCorners (xdest, y);
		if (dx < 0)
		{
			if (topLeft || bottomLeft)
			{
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else
			{
				xtemp += dx;
			}
		}
		
		if (dx > 0)
		{
			if (topRight|| bottomRight)
			{
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}		
			else
			{
				xtemp += dx;
			}
		}
		
		if (!falling)
		{
			calculateCorners (x, ydest + 1);
			if (!bottomLeft && !bottomRight)
			{
				falling = true;
			}
		}
	}
	
	/*
	 * This method returns the x value of the MapObject.
	 */
	public int getx () 
	{
		return (int) x;
	}
	
	/*
	 * This method returns the y value of the MapObject.
	 */
	public int gety () 
	{
		return (int) y;
	}
	
	/*
	 * This method returns the width of the MapObject.
	 */
	public int getWidth () 
	{
		return width;
	}
	
	/*
	 * This method returns the height of the MapObject.
	 */
	public int getHeight () 
	{
		return height;
	}
	
	/*
	 * This method returns the cwidth of the MapObject.
	 */
	public int getCWidth () 
	{
		return cwidth;
	}
	
	/*
	 * This method returns the cheight of the MapObject.
	 */
	public int getCHeight () 
	{
		return cheight;
	}
	
	/*
	 * This method sets the position of the MapObject.
	 */
	public void setPosition (double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/*
	 * This method sets the direction vector of the MapObject.
	 */
	public void setVector (double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	/*
	 * This method sets the position of the TileMap.
	 */
	public void setMapPosition()
	{
		xmap = tileMap.getX();
		ymap = tileMap.getY();
	}
	
	/*
	 * This method sets the left direction as true or false.
	 * 
	 * @param b boolean Stores whether the MapObject is facing left or not.
	 */
	public void setLeft (boolean b)
	{
		left = b;
	}
	
	/*
	 * This method sets the right direction as true or false.
	 * 
	 * @param b boolean Stores whether the MapObject is facing right or not.
	 */
	public void setRight (boolean b)
	{
		right = b;
	}
	
	/*
	 * This method sets the up direction as true or false.
	 * 
	 * @param b boolean Stores whether the MapObject is facing up or not.
	 */	
	public void setUp (boolean b)
	{
		up = b;
	}
	
	/*
	 * This method sets the down direction as true or false.
	 * 
	 * @param b boolean Stores whether the MapObject is facing down or not.
	 */
	public void setDown (boolean b )
	{
		down = b;
	}
	
	/*
	 * This method sets the jumping state as true or false.
	 * 
	 * @param b boolean Stores whether the MapObject is jumping or not.
	 */
	public void setJumping (boolean b)
	{
		jumping = b;
	}
	
	/*
	 * This method determines whether the MapObject is on the screen or not.
	 */
	public boolean notOnScreen ()
	{
		return x + xmap + width < 0 || x + xmap - width > GamePanel.WIDTH || y + ymap + height < 0 || y + ymap - height > GamePanel.HEIGHT;
	}
}


// END
