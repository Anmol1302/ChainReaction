package Backend;
//as soon as the player keeps getting eliminated skip his turn!
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Grid extends MainPage implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static Stage gridStage;
	private Pane root = new Pane();
	public int[] gridSize;
	boolean playable;
	public static ArrayList<Player> playerList;
	public static Cell[][] grid;
	public Player currentPlayer;
	public static int playerturn= 0;
	
	public Grid(int[] gridSize, ArrayList<Player> playerList)
	{
		this.gridSize= gridSize;
		Grid.playerList= playerList;
		Grid.grid= new Cell[gridSize[0]][gridSize[1]];
	}
	
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
					Grid.main(MainPage.gridSize, MainPage.listPlayers);
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
				gridStage.close();
			}
		});
		root.getChildren().add(exitBtn);
		
		Button undoBtn = new Button();
		undoBtn.setText("Undo");
		undoBtn.setLayoutX(340);
		undoBtn.setLayoutY(10);
		undoBtn.setMinWidth(100);
		root.getChildren().add(undoBtn);
		
		//Add the cells
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
				if((i==0 || i==gridSize[0]-1) && (j==0 || j==gridSize[1]-1))
				{
					box = new Cell(2,i,j,X,Y);
					grid[i][j]=box;
				}
				else if(i==0 || i==gridSize[0]-1 || j==0 || j==gridSize[1]-1)
				{
					box = new Cell(3,i,j,X,Y);
					grid[i][j]=box;
				}
				else
				{
					box = new Cell(4,i,j,X,Y);
					grid[i][j]=box;
				}
				box.setTranslateX(X);
                box.setTranslateY(Y);
                
                root.getChildren().add(box);
			}
		}
		return root;
	}

	public class Cell extends StackPane
	{
		int criticalMass;
		int orbSize;
		int cellSize;
		boolean isEmpty;
		Color color;
		Group orb;
		int numOrbs=0;
		int[] coordinate = new int[2];
		int X;
		int Y;
		ArrayList<Cell> neighbours;
		Rectangle box;
		Cell(int criticalMass,int x,int y,int X,int Y)
		{
			orb = new Group();
			coordinate[0]=x;
			coordinate[1]=y;
			this.X=X;
			this.Y=Y;
			this.criticalMass=criticalMass;
			isEmpty=true;
			neighbours = new ArrayList<Cell>();
			if(gridSize[0]==15)
			{
				cellSize = 60;
				orbSize = 12;
			}
			else
			{
				cellSize = 100;
				orbSize = 20;
			}
			box = new Rectangle(cellSize,cellSize);
			box.setFill(null);
			box.setStroke(playerList.get(0).getColor());
			box.setStrokeWidth(2);
			setAlignment(Pos.CENTER);
			getChildren().add(box);
			
			setOnMouseClicked(event -> 
			{
				currentPlayer= playerList.get(playerturn);
				if(this.color== currentPlayer.getColor() || this.color== null)
				{
					this.color= currentPlayer.getColor();//okay
					currentPlayer.takeTurn(this);
				}	
            });
			getChildren().add(orb);
		}
		
		public ArrayList<Cell> getNeighbour()//okay
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
			else//means it lies somewhere in between
			{
				list.add(grid[this.coordinate[0]][this.coordinate[1]+1]);
				list.add(grid[this.coordinate[0]+1][this.coordinate[1]]);
				list.add(grid[this.coordinate[0]-1][this.coordinate[1]]);
				list.add(grid[this.coordinate[0]][this.coordinate[1]-1]);
			}
			return list;
		}
		
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
					removeDeadPlayers(cur);
				}
				
			});
			
		}
		public void addOrb()
		{
			orb.getChildren().clear();
			if(numOrbs==0)
			{
				Sphere sphere = new Sphere(orbSize);
				sphere.setMaterial(new PhongMaterial(this.color));
				orb.getChildren().add(sphere);
			}
			else if(numOrbs==1)
			{
				Sphere sphere_1 = new Sphere(orbSize);
				sphere_1.setMaterial(new PhongMaterial(this.color));
				sphere_1.setTranslateX((cellSize - (3.5)*(orbSize))/2 + orbSize);
				Sphere sphere_2 = new Sphere(orbSize);
				sphere_2.setMaterial(new PhongMaterial(this.color));
				sphere_2.setTranslateX((cellSize - (3.5)*(orbSize))/2 + (2.5)*orbSize);
				orb.getChildren().addAll(sphere_1,sphere_2);
					
			}
			else if(numOrbs==2)
			{
				Sphere sphere_3 = new Sphere(orbSize);
				sphere_3.setMaterial(new PhongMaterial(this.color));
				sphere_3.setTranslateX((cellSize - (3.5)*(orbSize))/2 + (2.5)*orbSize);
				Sphere sphere_4 = new Sphere(orbSize);
				sphere_4.setMaterial(new PhongMaterial(this.color));
				sphere_4.setTranslateX((cellSize - (3.5)*(orbSize))/2 + orbSize);
				Sphere sphere_5 = new Sphere(orbSize);
				sphere_5.setMaterial(new PhongMaterial(this.color));
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
	}
	public static int checkColor(Cell cell){
		int result= 0;
		for(int i=0; i<Grid.playerList.size(); i++)
		{
			if(cell.color== Grid.playerList.get(i).getColor())
			{
				result= i;
				break;
			}
		}
		return result;
	}
	public static void removeDeadPlayers(Player cur)
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
		for(int i=Grid.playerList.size()-1; i>=0; i--)
		{
			if(freqarray[i]== 0)
			{
				Grid.playerList.remove(i);
			}
		}
		if(playerList.size()==1)
		{
			endGame(cur);
		}
	}
	public static void endGame(Player cur)
	{	
		Stage winstage= new Stage();
		winstage.setTitle("Game Over");
		VBox vbox= new VBox();
		HBox hbox= new HBox();
		Label label= new Label(cur.playerName + " has won the Game!!!");
		label.setFont(Font.font(28));
		label.setAlignment(Pos.CENTER);
		label.setWrapText(true);
        hbox.setPadding(new Insets(20, 20, 20, 20));
		label.setTextFill(cur.getColor());
		label.setStyle("-fx-font-weight: bold");
		hbox.getChildren().add(label);
		vbox.getChildren().add(hbox);
		Scene scene= new Scene(vbox, 300, 250);
		vbox.setStyle("-fx-background-color: #D3D3D3");
		winstage.setScene(scene);
		winstage.show();
		try 
		{
			TimeUnit.SECONDS.sleep(2);
		} 
		catch (InterruptedException e) 
		{
			
			e.printStackTrace();
		}
		
		
	}
	public static void main(int[] gridSize, ArrayList<Player> playerList) throws FileNotFoundException
	{
		gridStage= new Stage();
		Grid mainGrid= new Grid(gridSize, playerList);
		gridStage.setScene(new Scene(mainGrid.createContent(gridSize, playerList)));
        gridStage.show();
	}
}