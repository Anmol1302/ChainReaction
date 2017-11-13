package Backend;

import java.util.ArrayList;

import Backend.Grid.Cell;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Player 
{
	private Color color;
	public String playerName;
	public int[] griddim;
	
	Player(String s, Color color, int[] gridSize)
	{
		this.playerName= s;
		this.color= color;
		//grid = d;
		this.griddim= gridSize;
	}
	
	public void takeTurn(Cell cell)
	{
		if(cell.numOrbs< cell.criticalMass-1)
		{
			cell.addOrb();//okay
		}
		else
		{
			//orb.getChildren().clear();
			ArrayList<Cell> cellist= cell.getNeighbour();
			for(int i=0; i<cellist.size(); i++){
				takeTurn(cellist.get(i));
			}
		}
		Grid.playerturn++;
	}
	
	public static void main(String[] args)
	{
		
	}
	public Color getColor()
	{
		return this.color;
	}
}
