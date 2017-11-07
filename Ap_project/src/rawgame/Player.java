package rawgame;

import java.util.Scanner;

public class Player 
{
	static Scanner Reader = new Scanner(System.in);
	double[] color;
	boolean inGame;
	static Grid grid;
	String name;
	int playernum;
	static int[] gridSize;
	Player(double a,double b,double c,Grid d,String name,int num)
	{
		color = new double[3];
		color[0]=a; color[1]=b; color[2]=c;
		inGame=true;
		grid=d;
		this.name = name;
		gridSize=grid.gridSize;
		playernum = num;
	}
	
	public void taketurn()
	{
		boolean validmove;
		do
		{
			System.out.println("Enter coordinates for "+name);
			int x = Reader.nextInt();
			int y = Reader.nextInt();
			
			if(grid.get(x,y).equals("00"))
			{
				grid.set(x, y,"1"+this.playernum);
				validmove=true;
				System.out.println("You made a valid move");
			}
			else if(grid.get(x,y).substring(1).equals(""+this.playernum))
			{
				int numBalls=Integer.parseInt(grid.get(x, y).substring(0,1))+1;
				grid.set(x, y,Integer.toString(numBalls)+this.playernum);
				validmove=true;
				System.out.println("You made a valid move");
				checkmass(x,y,numBalls);
			}
			else
			{
				validmove=false;
			}
		}while(validmove==false);
	}
	
	void checkmass(int x,int y,int numBalls)
	{
		if((x==0 || (x==gridSize[0]-1)) && (y==0 || (y==gridSize[1]-1)))
		{
			if(numBalls==2)
				burst(x,y);
		}
		else if(x==0 || (x==gridSize[0]-1) || y==0 || (y==gridSize[1]-1))
		{
			if(numBalls==3)
				burst(x,y);
		}
		else
		{
			if(numBalls==4)
				burst(x,y);
		}
	}
	void burst(int x,int y)
	{
		String num = grid.get(x,y).substring(1);
		grid.set(x, y,"00");
		if((x==0 || (x==gridSize[0]-1)) && (y==0 || (y==gridSize[1]-1)))
		{
			if(x==0)
			{
				if(grid.get(x+1,y).equals("00"))
				{
					grid.set(x+1,y,"1"+this.playernum);
				}
				else
				{
					int numBalls=Integer.parseInt(grid.get(x+1,y).substring(0,1))+1;
					grid.set(x+1,y,Integer.toString(numBalls)+num);
					checkmass(x+1,y,numBalls);
				}
			}
			if(x==gridSize[0]-1)
			{
				if(grid.get(x-1,y).equals("00"))
				{
					grid.set(x-1,y,"1"+this.playernum);
				}
				else
				{
					int numBalls=Integer.parseInt(grid.get(x-1,y).substring(0,1))+1;
					grid.set(x-1,y,Integer.toString(numBalls)+num);
					checkmass(x-1,y,numBalls);
				}
			}
			if(y==0)
			{
				if(grid.get(x,y+1).equals("00"))
				{
					grid.set(x,y+1,"1"+this.playernum);
				}
				else
				{
					int numBalls=Integer.parseInt(grid.get(x,y+1).substring(0,1))+1;
					grid.set(x,y+1,Integer.toString(numBalls)+num);
					checkmass(x,y+1,numBalls);
				}
			}
			if(y==gridSize[1]-1)
			{
				if(grid.get(x,y-1).equals("00"))
				{
					grid.set(x,y-1,"1"+this.playernum);
				}
				else
				{
					int numBalls=Integer.parseInt(grid.get(x,y-1).substring(0,1))+1;
					grid.set(x,y-1,Integer.toString(numBalls)+num);
					checkmass(x,y-1,numBalls);
				}
			}
		}
		else if(x==0 || (x==gridSize[0]-1) || y==0 || (y==gridSize[1]-1))
		{
			if(x==0 || (x==gridSize[0]-1))
			{
				if(grid.get(x,y+1).equals("00"))
				{
					grid.set(x,y+1,"1"+this.playernum);
				}
				else
				{
					int numBalls=Integer.parseInt(grid.get(x,y+1).substring(0,1))+1;
					grid.set(x,y+1,Integer.toString(numBalls)+num);
					checkmass(x,y+1,numBalls);
				}
				if(grid.get(x,y-1).equals("00"))
				{
					grid.set(x,y-1,"1"+this.playernum);
				}
				else
				{
					int numBalls=Integer.parseInt(grid.get(x,y-1).substring(0,1))+1;
					grid.set(x,y-1,Integer.toString(numBalls)+num);
					checkmass(x,y-1,numBalls);
				}
				if(x==0)
				{
					if(grid.get(x+1,y).equals("00"))
					{
						grid.set(x+1,y,"1"+this.playernum);
					}
					else
					{
						int numBalls=Integer.parseInt(grid.get(x+1,y).substring(0,1))+1;
						grid.set(x+1,y,Integer.toString(numBalls)+num);
						checkmass(x+1,y,numBalls);
					}
				}
				else if(x==gridSize[0]-1)
				{
					if(grid.get(x-1,y).equals("00"))
					{
						grid.set(x-1,y,"1"+this.playernum);
					}
					else
					{
						int numBalls=Integer.parseInt(grid.get(x-1,y).substring(0,1))+1;
						grid.set(x-1,y,Integer.toString(numBalls)+num);
						checkmass(x-1,y,numBalls);
					}
				}
			}
			if(y==0 || (y==gridSize[1]-1))
			{
				if(grid.get(x+1,y).equals("00"))
				{
					grid.set(x+1,y,"1"+this.playernum);
				}
				else
				{
					int numBalls=Integer.parseInt(grid.get(x+1,y).substring(0,1))+1;
					grid.set(x+1,y,Integer.toString(numBalls)+num);
					checkmass(x+1,y,numBalls);
				}
				if(grid.get(x-1,y).equals("00"))
				{
					grid.set(x-1,y,"1"+this.playernum);
				}
				else
				{
					int numBalls=Integer.parseInt(grid.get(x-1,y).substring(0,1))+1;
					grid.set(x-1,y,Integer.toString(numBalls)+num);
					checkmass(x-1,y,numBalls);
				}
				if(y==0)
				{
					if(grid.get(x,y+1).equals("00"))
					{
						grid.set(x,y+1,"1"+this.playernum);
					}
					else
					{
						int numBalls=Integer.parseInt(grid.get(x,y+1).substring(0,1))+1;
						grid.set(x,y+1,Integer.toString(numBalls)+num);
						checkmass(x,y+1,numBalls);
					}
				}
				else if(y==gridSize[1]-1)
				{
					if(grid.get(x,y-1).equals("00"))
					{
						grid.set(x,y-1,"1"+this.playernum);
					}
					else
					{
						int numBalls=Integer.parseInt(grid.get(x,y-1).substring(0,1))+1;
						grid.set(x,y-1,Integer.toString(numBalls)+num);
						checkmass(x,y-1,numBalls);
					}
				}
			}
		}
		else
		{
			if(grid.get(x,y+1).equals("00"))
			{
				grid.set(x,y+1,"1"+this.playernum);
			}
			else
			{
				int numBalls=Integer.parseInt(grid.get(x,y+1).substring(0,1))+1;
				grid.set(x,y+1,Integer.toString(numBalls)+num);
				checkmass(x,y+1,numBalls);
			}
			if(grid.get(x,y-1).equals("00"))
			{
				grid.set(x,y-1,"1"+this.playernum);
			}
			else
			{
				int numBalls=Integer.parseInt(grid.get(x,y-1).substring(0,1))+1;
				grid.set(x,y-1,Integer.toString(numBalls)+num);
				checkmass(x,y-1,numBalls);
			}
			if(grid.get(x+1,y).equals("00"))
			{
				grid.set(x+1,y,"1"+this.playernum);
			}
			else
			{
				int numBalls=Integer.parseInt(grid.get(x+1,y).substring(0,1))+1;
				grid.set(x+1,y,Integer.toString(numBalls)+num);
				checkmass(x+1,y,numBalls);
			}
			if(grid.get(x-1,y).equals("00"))
			{
				grid.set(x-1,y,"1"+this.playernum);
			}
			else
			{
				int numBalls=Integer.parseInt(grid.get(x-1,y).substring(0,1))+1;
				grid.set(x-1,y,Integer.toString(numBalls)+num);
				checkmass(x-1,y,numBalls);
			}
		}
	}
}
