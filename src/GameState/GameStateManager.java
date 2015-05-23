package GameState;

import java.util.ArrayList;

public class GameStateManager 
{
	private ArrayList <GameState> gameStates;
	private int currentState;
	
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LEVEL2STATE = 2;
	public static final int LEVEL3STATE = 3;
	
	public GameStateManager ()
	{
		gameStates = new ArrayList <GameState>();
		currentState = 0;
		gameStates.add (new MenuState (this));
		gameStates.add (new Level1State(this));
		gameStates.add (new Level2State(this));
		gameStates.add (new Level3State(this));
	}
	
	public void setState (int state)
	{
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	public void update ()
	{
		gameStates.get(currentState).update();
	}
	
	public void draw (java.awt.Graphics2D g)
	{
		gameStates.get(currentState).draw(g);
	}
	
	public void keyPressed (int k)
	{
		gameStates.get(currentState).keyPressed (k);
	}
	
	public void keyReleased (int k)
	{
		gameStates.get(currentState).keyReleased (k);
	}
}








