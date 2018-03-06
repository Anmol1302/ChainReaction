package rawGame;

import java.util.ArrayList;
import java.util.Scanner;

public class Game 
{
	int numPlayers;
	int[] gridSize;
	ArrayList<Player> playerList;
	Grid grid;
	static Scanner Reader = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		Game firstGame = new Game();
		firstGame.gridSize = new int[2];
		firstGame.numPlayers = Reader.nextInt();
		firstGame.gridSize[0] = Reader.nextInt();
		firstGame.gridSize[1] = Reader.nextInt();
		firstGame.grid = new Grid(firstGame.gridSize);
		for(int i=0 ; i<firstGame.gridSize[0] ; i++)
		{
			for(int j=0 ; j<firstGame.gridSize[1] ; j++)
			{
				firstGame.grid.set(i,j,"00");
			}
		}
		
		firstGame.playerList = new ArrayList<Player>();
		firstGame.playerList.add(new Player(1,0,0,firstGame.grid,"Player 1",1));
		firstGame.playerList.add(new Player(0,1,0,firstGame.grid,"Player 2",2));
		firstGame.playerList.add(new Player(0,0,1,firstGame.grid,"Player 3",3));
		firstGame.playerList.add(new Player(1,1,0,firstGame.grid,"Player 4",4));
		firstGame.playerList.add(new Player(1,0,1,firstGame.grid,"Player 5",5));
		firstGame.playerList.add(new Player(0,1,1,firstGame.grid,"Player 6",6));
		firstGame.playerList.add(new Player(1,0.5,0,firstGame.grid,"Player 7",7));
		firstGame.playerList.add(new Player(1,1,1,firstGame.grid,"Player 8",8));
		firstGame.playerList = new ArrayList<Player>(firstGame.playerList.subList(0,firstGame.numPlayers));
		firstGame.playGame();
	}
	 public void playGame()
	 {
		 int numPlayers = this.numPlayers;
		 int i=1;
		 while(numPlayers>1)
		 {
			 for(int j=0 ; j<playerList.size() ;)
			 {
				 Player cur = playerList.get(j);
				 if(i!=1)
				 {
					 cur.inGame=this.checkInGame(cur); 
				 }
				 if(cur.inGame)
				 {
					 cur.taketurn();
					 display(this.grid);
					 j++;
				 }
				 else
					 playerList.remove(cur);
				 numPlayers=playerList.size();
			 }
			 i++;
		 }
		 System.out.print("Winner is "+playerList.get(0).name);
	 }
	 boolean checkInGame(Player cur)
	 {
		 int playerNum =  cur.playernum;
		 for(int i=0 ; i<gridSize[0] ; i++)
		 {
			 for(int j=0 ; j<gridSize[1] ; j++)
			 {
				 if(Integer.parseInt(this.grid.get(i,j).substring(1))==playerNum)
				 {
					 return true;
				 }
			 }
		 }
		 return false;
	 }
	 void display(Grid grid)
	 {
		 for(int i=0 ; i<grid.gridSize[0] ; i++)
		 {
			 for(int j=0 ; j<grid.gridSize[1] ; j++)
			 {
				 System.out.print(grid.get(i,j)+ " ");
			 }
			 System.out.println();
		 }
	 }
}
