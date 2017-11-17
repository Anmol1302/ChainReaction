package Backend;

import java.util.ArrayList;
import Backend.Grid.Cell;
import javafx.scene.paint.Color;

public class Player 
{
	private Color color;
	public String playerName;
	public int[] griddim;
	int playernum;
	public int flag= 0;
	
	Player(int n,String s, Color color, int[] gridSize)
	{
		this.playerName= s;
		this.color= color;
		//grid = d;
		this.griddim= gridSize;
		playernum=n;
	}
	
	public void subtakeTurn(Cell cell)
	{//check if you are constantly changing the color
		cell.color= this.color;
		if(cell.numOrbs< cell.criticalMass-1)
		{
			cell.addOrb();//okay
		}
		else
		{
			ArrayList<Cell> cellist= cell.getNeighbour();//check
			cell.orb.getChildren().clear();//okay
			cell.numOrbs= 0;
			cell.translate(this,cellist,cell.color);
			cell.color= null;
		}
	}
	
	public void takeTurn(Cell cell)
	{//check if you are constantly changing the color
		cell.color= this.color;
		if(cell.numOrbs< cell.criticalMass-1)
		{
			cell.addOrb();//okay
		}
		else
		{
			ArrayList<Cell> cellist= cell.getNeighbour();//check
			cell.orb.getChildren().clear();//okay
			cell.numOrbs= 0;
			cell.translate(this,cellist,cell.color);
			cell.color= null;
			
		}
		
		if(Grid.playerturn>= Grid.listPlayers.size()-1){
			Grid.playerturn= 0;
			for(int i=0; i<Grid.playerList.size(); i++){
				Grid.playerList.get(i).flag= 1;//means one round has been completed! okay
			}
		}else{
			Grid.playerturn++;
		}
		
		for(int i=0 ; i<Grid.grid.length ; i++)
		{
			for(int j=0 ; j<Grid.grid[0].length ; j++)
			{
				Grid.grid[i][j].box.setStroke(Grid.playerList.get(Grid.playerturn).getColor());
				Grid.grid[i][j].box.setStrokeWidth(2);
			}
		}
	}
	
	public Color getColor()
	{
		return this.color;
	}
}