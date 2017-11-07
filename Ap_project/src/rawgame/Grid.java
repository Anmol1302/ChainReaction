package rawgame;

public class Grid 
{
	int[] gridSize;
	String[][] grid;
	Grid(int[] size)
	{
		gridSize=size;
		grid = new String[gridSize[0]][gridSize[1]];
		for(int i=0 ; i<gridSize[0] ; i++)
		{
			for(int j=0 ; j<gridSize[1] ; j++)
			{
				grid[i][j]="00";
			}
		}
	}
	void set(int x,int y, String s)
	{
		grid[x][y] =s;
	}
	String get(int x,int y)
	{
		return grid[x][y];
	}
}
