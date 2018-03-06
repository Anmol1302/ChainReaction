/**
 * This class is used to save the state of the Game for implementing undo and ResumeGame functionalities.
 * @author Anmol Agarwal
 */
package FinalCode;

import java.io.Serializable;
import java.util.ArrayList;
public class GameState implements Serializable
{
	private static final long serialVersionUID = 1L;
	int[] gridSize;
	ArrayList<Player> activePlayers;
	String[][] grid;
	int playerTurn;
	boolean gameResumed;
	Player currentPlayer;
	
	/**
	 * This method helps to serialize and store the the state of the Game at each turn 
	 * @param gridSize Size of the grid to be serialized
	 * @param playerList List of Players with the player info.
	 * @param playerTurn Player from whose turn to resume the Game
	 * @param grid The Grid
	 * @param currentPlayer The current player
	 * @param gameResumed boolean parameter to inform the constructors of a new Game or a resumed Game
	 */
	GameState(int[] gridSize,ArrayList<Player> playerList,int playerTurn,String[][] grid,Player currentPlayer,boolean gameResumed)
	{
		this.gridSize = gridSize;
		activePlayers = new ArrayList<>(playerList);
		this.playerTurn=playerTurn;
		this.gameResumed = gameResumed;
		this.grid = new String[gridSize[0]][gridSize[1]];
		for(int i=0 ; i<gridSize[0] ; i++)
		{
			for(int j=0 ; j<gridSize[1] ; j++)
			{
				this.grid[i][j] = grid[i][j];
				
			}
		}
		this.currentPlayer=currentPlayer;
	}
	
}
