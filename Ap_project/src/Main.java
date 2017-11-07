import java.util.*;
import java.io.*;

class Player 
{
	private double[] colour = new double[3];
	private static int[] gridSize = new int[2];
	private String name;
	String getname()
	{
		return name;
	}

	private static String[][] grid;
	
	Player(double[] colour,String name,String[][] grid)
	{
		this.name=name;
		this.colour=colour;
		this.grid=grid;
	}
	double[] getColour()
	{
		return colour;
	}
	void setGridsize(int n)
	{
		if(n==0)
		{
			gridSize[0] = 9;
			gridSize[1] = 6;
		}
		else if(n==1)
		{
			gridSize[0] = 15;
			gridSize[1] = 10;
		}
	}
	int[] getGridsize()
	{
		return gridSize;
	}
	
	void taketurn(int x,int y)
	{
		if((x==0 || x==gridSize[0]) && (y==0 || y==gridSize[1]))
		{
			
		}
		else if(x==0 || x==gridSize[0] || y==0 || y==gridSize[1])
		{
			
		}
		else
		{
			
		}
	}
}

class Game
{
	private String[][] grid;
	private static ArrayList<Player> playerData;
	private int[] gridSize;
}
public class Main {
	
	private int numPlayers;
	private int gridSize;
	private ArrayList<Player> playerdata;
	public static void main(String[] args)
	{
		Main page = new Main();
		
		ArrayList<Player> data = new ArrayList<Player>();
		double[] color = {1,0,0};
		data.add(new Player(color,"Player 1"));
		color[0]=0;color[1]=1;color[2]=0;
		data.add(new Player(color,"Player 2"));
		color[0]=0;color[1]=0;color[2]=1;
		data.add(new Player(color,"Player 3"));
		color[0]=1;color[1]=1;color[2]=0;
		data.add(new Player(color,"Player 4"));
		color[0]=1;color[1]=0;color[2]=1;
		data.add(new Player(color,"Player 5"));
		color[0]=0;color[1]=1;color[2]=1;
		data.add(new Player(color,"Player 6"));
		color[0]=1;color[1]=0.5;color[2]=0;
		data.add(new Player(color,"Player 7"));
		color[0]=1;color[1]=1;color[2]=1;
		data.add(new Player(color,"Player 8"));
		
		page.setplayerdata(data);
	}
	
	ArrayList<Player> getplayerdata()
	{
		return playerdata;
	}
	void setplayerdata(ArrayList<Player> data)
	{
		playerdata=data;
	}
	public ArrayList<Player> settings()
	{
		
	}
}
