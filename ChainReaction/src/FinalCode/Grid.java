package FinalCode;

import java.io.*;
import java.util.ArrayList;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Grid extends RunGame implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static Stage gridStage;
	private Pane root = new Pane();
	public static int[] gridSize;
	boolean playable;
	public static ArrayList<Player> playerList;
	public static Cell[][] grid;
	public static Player currentPlayer;
	private static int count;
	public static int playerturn;
	public boolean gameResumed;
	public static GameState lastState;
	String[][] rawGrid;
	public Grid(int[] gridSize, ArrayList<Player> playerList,int playerTurn,String[][] rawGrid,Player currentPlayer,boolean gameResumed)
	{
		Grid.gridSize= gridSize;
		Grid.playerList= new ArrayList<Player>(playerList);
		Grid.grid= new Cell[gridSize[0]][gridSize[1]];
		Grid.currentPlayer=currentPlayer;
		Grid.playerturn=playerTurn;
		this.rawGrid = rawGrid;
		this.gameResumed=gameResumed;
		lastState = RunGame.curState;
		count=0;
	}
	static int[] getgridSize()
	{
		return gridSize;
	}
	
	/**
	* The create content method 
	* makes the whole GUI of the grid
	* @author  Anmol Agarwal
	* @version 1.0
	* @since   18-11-2017
	*/
	
	public Parent createContent(int[] gridSize, ArrayList<Player> playerList) throws FileNotFoundException
	{
		root.setPrefSize(700,1000);
		
		//Add the blue separator line
		Line topLine = new Line();
		topLine.setStroke(Color.BLUE);
		topLine.setStartX(0); 
		topLine.setStartY(50); 
		topLine.setEndX(800); 
		topLine.setEndY(50);
		topLine.setStrokeWidth(4);
		root.getChildren().add(topLine);
		
		//Add heading
		Text heading = new Text();
		heading.setText("Chain Reaction");
		heading.setTextOrigin(VPos.TOP);
		heading.setFont(Font.font(36));
		root.getChildren().add(heading);
		
		//Add buttons
		Button newGameBtn = new Button();
		newGameBtn.setText("New Game");
		newGameBtn.setLayoutX(460);
		newGameBtn.setLayoutY(10);
		newGameBtn.setMinWidth(100);
		newGameBtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) 
			{
				gridStage.close();
				try 
				{
					Grid.main(RunGame.gridSize, RunGame.listPlayers,0,new String[RunGame.gridSize[0]][RunGame.gridSize[1]],RunGame.listPlayers.get(0),false);
				}
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
			}
		});
		root.getChildren().add(newGameBtn);
		
		Button exitBtn = new Button();
		exitBtn.setText("Exit");
		exitBtn.setLayoutX(580);
		exitBtn.setLayoutY(10);
		exitBtn.setMinWidth(100);
		exitBtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				System.out.println("Stage is closing");
			    ObjectOutputStream writer = null;
				try{
		            	writer = new ObjectOutputStream(new FileOutputStream(new File("savedGame.txt")));
		            	writer.writeObject(curState);
		            }
		        catch(IOException e){
		        	
		        }
				finally
				{
					try 
					{
						writer.close();
					} catch (IOException e) 
					{
						e.printStackTrace();
					}
					gridStage.close();
				}
			}
		});
		root.getChildren().add(exitBtn);
		
		Button undoBtn = new Button();
		undoBtn.setText("Undo");
		undoBtn.setLayoutX(340);
		undoBtn.setLayoutY(10);
		undoBtn.setMinWidth(100);
		undoBtn.setOnAction(new EventHandler<ActionEvent>(){
				
				@Override
				public void handle(ActionEvent event) 
				{
					gridStage.close();
					try 
					{	
						RunGame.curState=lastState;
						Grid.main(lastState.gridSize, lastState.activePlayers,lastState.playerTurn,lastState.grid,lastState.currentPlayer,lastState.gameResumed);
					}
					catch(NullPointerException | FileNotFoundException e)
					{
						event.consume();
						try {
							Grid.main(RunGame.gridSize, RunGame.listPlayers,0,new String[RunGame.gridSize[0]][RunGame.gridSize[1]],RunGame.listPlayers.get(0),false);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				}
		});
		root.getChildren().add(undoBtn);
		
		gridStage.setOnCloseRequest(event -> {
		    System.out.println("Stage is closing");
		    ObjectOutputStream writer = null;
			try{
	            	writer = new ObjectOutputStream(new FileOutputStream(new File("savedGame.txt")));
	            	writer.writeObject(curState);
	            }
	        catch(IOException e){
	        	
	        }
			finally
			{
				try 
				{
					writer.close();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
				gridStage.close();
			}
			
		});
		
		//Add the cells
		int orbSize=0;
		int cellSize=0;
		if(gridSize[0]==15)
		{
			orbSize=12;
			cellSize=60;
		}
		else
		{
			orbSize=20;
			cellSize=100;
		}
		for(int i=0 ; i<gridSize[0] ; i++)
		{
			for(int j=0 ; j<gridSize[1] ; j++)
			{
				int X,Y;
				if(gridSize[0]==15 && gridSize[1]==10)
				{
					X = 50+(j * 60);
					Y = 75+(i * 60);
				}
				else
				{
					X = 50+(j * 100);
					Y = 75+(i * 100);
				}
				Cell box;
				if(!gameResumed)
				{
					
					if((i==0 || i==gridSize[0]-1) && (j==0 || j==gridSize[1]-1))
					{
						box = new Cell(2,i,j,X,Y,orbSize,cellSize);
						grid[i][j]=box;
					}
					else if(i==0 || i==gridSize[0]-1 || j==0 || j==gridSize[1]-1)
					{
						box = new Cell(3,i,j,X,Y,orbSize,cellSize);
						grid[i][j]=box;
					}
					else
					{
						box = new Cell(4,i,j,X,Y,orbSize,cellSize);
						grid[i][j]=box;
					}
					box.setTranslateX(X);
	                box.setTranslateY(Y);
					root.getChildren().add(box);
				}
			}
		}
		if(gameResumed)
		{
			for(int i=0 ; i<gridSize[0] ; i++)
			{
				for(int j=0 ; j<gridSize[1] ; j++)
				{
					String[] cellData = rawGrid[i][j].split(" ");
					if(cellData[0].equals("-1"))
					{						
						grid[i][j] = new Cell(Integer.parseInt(cellData[2]),i,j,Integer.parseInt(cellData[3]),Integer.parseInt(cellData[4]),orbSize,cellSize);
						grid[i][j].setTranslateX(Integer.parseInt(cellData[3]));
						grid[i][j].setTranslateY(Integer.parseInt(cellData[4]));
						root.getChildren().add(grid[i][j]);
						
					}
					else
					{
						grid[i][j] = new Cell(Integer.parseInt(cellData[2]),i,j,Integer.parseInt(cellData[3]),Integer.parseInt(cellData[4]),orbSize,cellSize);
						grid[i][j].numOrbs = Integer.parseInt(cellData[1]);
						grid[i][j].color = playerList.get(Integer.parseInt(cellData[0])).getHexColor();
						grid[i][j].setTranslateX(Integer.parseInt(cellData[3]));
						grid[i][j].setTranslateY(Integer.parseInt(cellData[4]));
						grid[i][j].addBalls();
						root.getChildren().add(grid[i][j]);
					}
					
				}
			}
		}
		return root;
	}
	/**
	* The save Game method updates the curState object by overwriting it
	* @author  Anmol Agarwal
	* @version 1.0
	* @since   18-11-2017
	*/
	
	public static void saveGame(String[][] grid)
	{
		String[][] test = new  String[Grid.gridSize[0]][Grid.gridSize[1]];
		for(int i=0 ; i<Grid.gridSize[0] ; i++)
		{
			for(int j=0 ; j<Grid.gridSize[1] ; j++)
			{
				test[i][j] = grid[i][j];
			}
		}
		RunGame.curState = new GameState(Grid.gridSize,Grid.playerList,Grid.playerturn,test,Grid.currentPlayer,true);
	}
	
	/**
	* Cell class contains all the data about a particular cell of the grid
	* @author  Anmol Agarwal
	* @version 1.0
	* @since   18-11-2017
	*/
	
	public class Cell extends StackPane
	{
		int criticalMass;
		int orbSize;
		int cellSize;
		boolean isEmpty;
		String color;
		Group orb;
		int numOrbs=0;
		int[] coordinate = new int[2];
		int X;
		int Y;
		ArrayList<Cell> neighbours;
		Rectangle box;
		Cell(int criticalMass,int x,int y,int X,int Y,int orbSize,int cellSize)
		{
			orb = new Group();
			coordinate[0]=x;
			coordinate[1]=y;
			this.X=X;
			this.Y=Y;
			this.criticalMass=criticalMass;
			isEmpty=true;
			neighbours = new ArrayList<Cell>();
			this.orbSize=orbSize;
			this.cellSize=cellSize;
			box = new Rectangle(cellSize,cellSize);
			box.setFill(null);
			box.setStroke(Color.valueOf(playerList.get(0).getHexColor()));
			box.setStrokeWidth(2);
			setAlignment(Pos.CENTER);
			getChildren().add(box);
			
			setOnMouseClicked(event -> 
			{
				currentPlayer= playerList.get(playerturn);
				if(this.color==currentPlayer.getHexColor() || this.color== null)
				{
					this.color= currentPlayer.getHexColor();//okay
					if(Grid.playerturn >= Grid.playerList.size()-1)
					{
						Grid.playerturn= 0;
						if(Grid.playerList.get(0).flag!=1)
						{
							for(int i=0; i<Grid.playerList.size(); i++)
							{
								Grid.playerList.get(i).flag= 1;//means one round has been completed! okay
							}
						}
						
					}
					else
					{
						Grid.playerturn++;
					}
					currentPlayer.takeTurn(this);
				}	
            });
			getChildren().add(orb);
		}
		
		/**
		* addballs() gets called through undo or resume game to add the balls into the grid
		* @author  Anmol Agarwal
		* @version 1.0
		* @since   18-11-2017
		*/
		
		public void addBalls()
		{
			orb.getChildren().clear();
			if(numOrbs==1)
			{
				Sphere sphere = new Sphere(orbSize);
				sphere.setMaterial(new PhongMaterial(Color.valueOf(this.color)));
				orb.getChildren().add(sphere);
			}
			else if(numOrbs==2)
			{
				Sphere sphere_1 = new Sphere(orbSize);
				sphere_1.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_1.setTranslateX((cellSize - (3.5)*(orbSize))/2 + orbSize);
				Sphere sphere_2 = new Sphere(orbSize);
				sphere_2.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_2.setTranslateX((cellSize - (3.5)*(orbSize))/2 + (2.5)*orbSize);
				orb.getChildren().addAll(sphere_1,sphere_2);
					
			}
			else if(numOrbs==3)
			{
				Sphere sphere_3 = new Sphere(orbSize);
				sphere_3.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_3.setTranslateX((cellSize - (3.5)*(orbSize))/2 + (2.5)*orbSize);
				Sphere sphere_4 = new Sphere(orbSize);
				sphere_4.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_4.setTranslateX((cellSize - (3.5)*(orbSize))/2 + orbSize);
				Sphere sphere_5 = new Sphere(orbSize);
				sphere_5.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_5.setTranslateX((cellSize - (3.5)*(orbSize))/2 + (1.75)*orbSize);
				sphere_5.setTranslateY(cellSize/4);
				orb.getChildren().addAll(sphere_3,sphere_4,sphere_5);
			}
			RotateTransition rotater = new RotateTransition();
			if(numOrbs==this.criticalMass-1)
				rotater.setDuration(Duration.seconds(2));
			else
				rotater.setDuration(Duration.seconds(8));
			rotater.setNode(orb);
			rotater.setFromAngle(0);
			rotater.setToAngle(360);
			rotater.setAutoReverse(false);
			rotater.setCycleCount(Timeline.INDEFINITE);
			rotater.setInterpolator(Interpolator.LINEAR);
			rotater.play();	
		}
		
		/**
		* addOrbs() gets called every time a player takes a turn and a ball needs to be added in the cell
		* @author  Anmol Agarwal
		* @version 1.0
		* @since   18-11-2017
		*/
		
		public void addOrb()
		{
			orb.getChildren().clear();
			if(numOrbs==0)
			{
				Sphere sphere = new Sphere(orbSize);
				sphere.setMaterial(new PhongMaterial(Color.valueOf(this.color)));
				orb.getChildren().add(sphere);
			}
			else if(numOrbs==1)
			{
				Sphere sphere_1 = new Sphere(orbSize);
				sphere_1.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_1.setTranslateX((cellSize - (3.5)*(orbSize))/2 + orbSize);
				Sphere sphere_2 = new Sphere(orbSize);
				sphere_2.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_2.setTranslateX((cellSize - (3.5)*(orbSize))/2 + (2.5)*orbSize);
				orb.getChildren().addAll(sphere_1,sphere_2);
					
			}
			else if(numOrbs==2)
			{
				Sphere sphere_3 = new Sphere(orbSize);
				sphere_3.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_3.setTranslateX((cellSize - (3.5)*(orbSize))/2 + (2.5)*orbSize);
				Sphere sphere_4 = new Sphere(orbSize);
				sphere_4.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_4.setTranslateX((cellSize - (3.5)*(orbSize))/2 + orbSize);
				Sphere sphere_5 = new Sphere(orbSize);
				sphere_5.setMaterial(new PhongMaterial(Color.valueOf(color)));
				sphere_5.setTranslateX((cellSize - (3.5)*(orbSize))/2 + (1.75)*orbSize);
				sphere_5.setTranslateY(cellSize/4);
				orb.getChildren().addAll(sphere_3,sphere_4,sphere_5);
			}
			numOrbs++;
			RotateTransition rotater = new RotateTransition();
			if(numOrbs==this.criticalMass-1)
				rotater.setDuration(Duration.seconds(2));
			else
				rotater.setDuration(Duration.seconds(8));
			rotater.setNode(orb);
			rotater.setFromAngle(0);
			rotater.setToAngle(360);
			rotater.setAutoReverse(false);
			rotater.setCycleCount(Timeline.INDEFINITE);
			rotater.setInterpolator(Interpolator.LINEAR);
			rotater.play();	
		}
		
		/**
		* getNeighbour() returns ArrayList<Cell> which are adjacent to a particular cell
		* @author  Rajat Bansal
		* @version 1.0
		* @since   18-11-2017
		*/
		
		public ArrayList<Cell> getNeighbour()
		{
			ArrayList<Cell> list= new ArrayList<Cell>();
			if((this.coordinate[1]==0))
			{
				list.add(grid[this.coordinate[0]][this.coordinate[1]+1]);
				if(this.coordinate[0]==0)
				{
					list.add(grid[this.coordinate[0]+1][this.coordinate[1]]);
				}
				else if(this.coordinate[0]== gridSize[0]-1)
				{
					list.add(grid[this.coordinate[0]-1][this.coordinate[1]]);
				}
				else
				{
					list.add(grid[this.coordinate[0]+1][this.coordinate[1]]);
					list.add(grid[this.coordinate[0]-1][this.coordinate[1]]);
				}
			}
			else if((this.coordinate[1]==gridSize[1]-1))
			{
				list.add(grid[this.coordinate[0]][this.coordinate[1]-1]);
				if(this.coordinate[0]==0)
				{
					list.add(grid[this.coordinate[0]+1][this.coordinate[1]]);
				}
				else if(this.coordinate[0]== gridSize[0]-1)
				{
					list.add(grid[this.coordinate[0]-1][this.coordinate[1]]);
				}
				else
				{
					list.add(grid[this.coordinate[0]+1][this.coordinate[1]]);
					list.add(grid[this.coordinate[0]-1][this.coordinate[1]]);
				}
			}
			else if(this.coordinate[0]== 0)
			{
				list.add(grid[this.coordinate[0]+1][this.coordinate[1]]);
				if(this.coordinate[1]==0)
				{
					list.add(grid[this.coordinate[0]][this.coordinate[1]+1]);
				}
				else if(this.coordinate[1]==gridSize[1]-1)
				{
					list.add(grid[this.coordinate[0]][this.coordinate[1]-1]);
				}
				else
				{
					list.add(grid[this.coordinate[0]][this.coordinate[1]+1]);
					list.add(grid[this.coordinate[0]][this.coordinate[1]-1]);
				}
			}
			else if(this.coordinate[0]== gridSize[0]-1)
			{
				list.add(grid[this.coordinate[0]-1][this.coordinate[1]]);
				if(this.coordinate[1]==0)
				{
					list.add(grid[this.coordinate[0]][this.coordinate[1]+1]);
				}
				else if(this.coordinate[1]== gridSize[1]-1)
				{
					list.add(grid[this.coordinate[0]][this.coordinate[1]-1]);
				}
				else
				{
					list.add(grid[this.coordinate[0]][this.coordinate[1]+1]);
					list.add(grid[this.coordinate[0]][this.coordinate[1]-1]);
				}
			}
			else
			{
				list.add(grid[this.coordinate[0]][this.coordinate[1]+1]);
				list.add(grid[this.coordinate[0]+1][this.coordinate[1]]);
				list.add(grid[this.coordinate[0]-1][this.coordinate[1]]);
				list.add(grid[this.coordinate[0]][this.coordinate[1]-1]);
			}
			return list;
		}
		
		/**
		* translate is called to show tansition animation onto the neighbouring cells 
		* when the number of balls in that cell increase its critical mass
		* @author  Anmol Agarwal
		* @version 1.0
		* @since   18-11-2017
		*/
		
		public void translate(Player cur,ArrayList<Cell> neighbours,Color color)
		{
			ParallelTransition mainTransition = new ParallelTransition();
			Sphere[] tempBall = new Sphere[neighbours.size()];
			
			for(int i=0 ; i<neighbours.size() ; i++)
			{
				tempBall[i] = new Sphere(this.orbSize);
				tempBall[i].setMaterial(new PhongMaterial(color));
				this.getChildren().add(tempBall[i]);
				TranslateTransition tt = new TranslateTransition(Duration.millis(400));
				tt.setNode(tempBall[i]);
				int dirX = neighbours.get(i).coordinate[1] - coordinate[1];
				int dirY = neighbours.get(i).coordinate[0] - coordinate[0];
				tt.setByX(cellSize*dirX);
				tt.setByY(cellSize*dirY);
				mainTransition.getChildren().add(tt);
			}
			mainTransition.play();
			mainTransition.setOnFinished(e ->{
				
				boolean endOfAnimation = true;
				for(int i=0 ; i<neighbours.size() ;i++)
				{
					this.getChildren().remove(tempBall[i]);
				}
				for(int i=0; i<neighbours.size(); i++)
				{
					if(neighbours.get(i).numOrbs==neighbours.get(i).criticalMass-1)
					{
						endOfAnimation = false;
					}
					
					cur.subtakeTurn(neighbours.get(i));
				}
				if(endOfAnimation)
				{
					doAtEndOfAnimation(cur);
					
				}
				
			});	
		}
	}
	
	/**
	* checkColor() returns the player whose balls are present in that cell
	* when the number of balls in that cell increase its critical mass
	* @author  Anmol Agarwal
	* @version 1.0
	* @since   18-11-2017
	*/
	
	public static int checkColor(Cell cell){
		int result= 0;
		for(int i=0; i<Grid.playerList.size(); i++)
		{
			if(cell.color== Grid.playerList.get(i).getHexColor())
			{
				result= i;
				break;
			}
		}
		return result;
	}
	
	/**
	* At the end of transition animation this function checks whether to remove a player
	* and removes it. It also checks whether game has ended and calls endGame()
	* @author  Anmol Agarwal
	* @version 1.0
	* @since   18-11-2017
	*/
	
	public static void doAtEndOfAnimation(Player cur)
	{
		int[] freqarray= new int[Grid.playerList.size()];
		for(int i=0; i<Grid.grid.length; i++)
		{
			for(int j=0; j<Grid.grid[0].length; j++)
			{
				if(Grid.grid[i][j].color!= null)
				{
					freqarray[Grid.checkColor(Grid.grid[i][j])]++;
				}
			}
		}
		//Removes dead players
		for(int i=Grid.playerList.size()-1; i>=0; i--)
		{
			if(freqarray[i]== 0)
			{
				Grid.playerList.remove(i);
			}
		}
		if(playerturn>=Grid.playerList.size())
		{
			playerturn=0;
		}
		//Checks for winner
		if(playerList.size()==1 && count==0)
		{
			count++;
			endGame(cur);
		}
		
	}
	
	/**
	* This function ends the game and declares the winner
	* @author  Anmol Agarwal
	* @version 1.0
	* @since   18-11-2017
	*/
	
	public static void endGame(Player cur)
	{	
		Stage winStage= new Stage();
		winStage.setTitle("Game Over");
		Pane endGamePane = new Pane();
		endGamePane.setPrefSize(300,300);
		endGamePane.setStyle("-fx-background-color: #D3D3D3");
		Label endMessage = new Label(cur.playerName + " has won the Game!!!");
		endMessage.setFont(Font.font("Roboto",20));
		endMessage.setTranslateX(20);
		endMessage.setTranslateY(100);
		endMessage.setTextAlignment(TextAlignment.CENTER);
		endMessage.setAlignment(Pos.CENTER);
		endMessage.setTextFill(Color.valueOf(cur.getHexColor()));
		endMessage.setWrapText(true);
		endGamePane.getChildren().add(endMessage);
		
		//Add buttons
		Button newGameBtn = new Button();
		newGameBtn.setText("New Game");
		newGameBtn.setLayoutX(50);
		newGameBtn.setLayoutY(200);
		newGameBtn.setMinWidth(75);
		newGameBtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) 
			{
				winStage.close();
				gridStage.close();
				try 
				{
					Grid.main(RunGame.gridSize, RunGame.listPlayers,0,new String[RunGame.gridSize[0]][RunGame.gridSize[1]],RunGame.listPlayers.get(0),false);
				}
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
			}
		});
		endGamePane.getChildren().add(newGameBtn);
				
		Button exitBtn = new Button();
		exitBtn.setText("Exit");
		exitBtn.setLayoutX(175);
		exitBtn.setLayoutY(200);
		exitBtn.setMinWidth(75);
		exitBtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				System.out.println("Stage is closing");
			    ObjectOutputStream writer = null;
			    RunGame.curState=null;
			    Grid.lastState=null;
				try{
		            	writer = new ObjectOutputStream(new FileOutputStream(new File("savedGame.txt")));
		            	writer.writeObject(curState);
		            }
		        catch(IOException e){
		        	
		        }
				finally
				{
					try 
					{
						writer.close();
					} catch (IOException e) 
					{
						e.printStackTrace();
					}
					winStage.close();
					gridStage.close();
				}
			}
		});
		endGamePane.getChildren().add(exitBtn);
		
		winStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) 
		    {

		        // consume event
		        event.consume();
		    }
		});
		
		Scene scene = new Scene(endGamePane);
		winStage.setScene(scene);
		winStage.show();
	}
	
	/**
	* main function to create a gridStage fill content into it and display it
	* @author  Anmol Agarwal
	* @version 1.0
	* @since   18-11-2017
	*/
	public static void main(int[] gridSize, ArrayList<Player> playerList,int playerTurn,String[][] grid,Player currentPlayer,boolean gameResumed) throws FileNotFoundException
	{
		gridStage= new Stage();
		Grid mainGrid= new Grid(gridSize, playerList,playerTurn,grid,currentPlayer,gameResumed);
		gridStage.setScene(new Scene(mainGrid.createContent(gridSize, playerList)));
        gridStage.show();
	}
	
	/**
	* converts the Cell[][] to a String[][] that is serializable
	* @author  Anmol Agarwal
	* @version 1.0
	* @since   18-11-2017
	*/
	
	public static String[][] getrawGrid(Cell[][] grid) 
	{
		String[][] rawGrid = new String[grid.length][grid[0].length];
		for(int i=0 ; i<grid.length ; i++)
		{
			for(int j=0 ; j<grid[0].length ; j++)
			{
				if(grid[i][j].color==null)
				{
					rawGrid[i][j]="-1 0"+" "+grid[i][j].criticalMass+" "+grid[i][j].X+" "+grid[i][j].Y;
				}
				else
				{
					rawGrid[i][j]=""+checkColor(grid[i][j])+" "+grid[i][j].numOrbs+" "+grid[i][j].criticalMass+" "+grid[i][j].X+" "+grid[i][j].Y;
				}
			}
		}
		return rawGrid;
	}
}