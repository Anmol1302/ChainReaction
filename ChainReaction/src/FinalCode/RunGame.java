/**
 * This class is the class for the Main Page
 * it makes the GUI for grid and also takes input from the user 
 * for gridSize and number of players
 * @author Rajat Bansal
 */
package FinalCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import FinalCode.Settings;

public class RunGame extends Application
{
	public static int numPlayers= 0;
	public static int[] gridSize= new int[2];
	public static Color[] colorList= new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.WHITE};
	public static ArrayList<Player> listPlayers= new ArrayList<Player>();
	public static GameState curState = null;
	
	/**
	 * 
	 * @param args inputs
	 * @throws Exception General type exception catching
	 * @throws NullPointerException Null Pointer Exception catching 
	 */
	public static void main(String[] args) throws Exception, BadColorSelectionException{
		launch(args);
	}
	
	/**
	 * Abstract method executed by the Application thread(takes input via GUI from user) check for resume Game or starting a newGame
	 */
	@Override
	public void start(Stage mainstage) throws Exception, BadColorSelectionException{
		mainstage.setTitle("Main Page");

		ChoiceBox<String> cb= new ChoiceBox<String>();
		cb.setItems(FXCollections.observableArrayList("2", "3", "4", "5", "6", "7", "8"));
		cb.getSelectionModel().selectFirst();

		ChoiceBox<String> cb2= new ChoiceBox<String>();
		cb2.setItems(FXCollections.observableArrayList("9X6", "15X10"));
		cb2.getSelectionModel().selectFirst();
		
		Button btn2= new Button("Resume Game!");
		btn2.setTranslateX(50);
		btn2.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) 
			{
				ObjectInputStream reader = null;
				try 
				{
					reader = new ObjectInputStream(new FileInputStream(new File("savedGame.txt")));
					GameState loadState = (GameState) reader.readObject();					
					int[] gridSize = loadState.gridSize;
					ArrayList<Player> playerList = loadState.activePlayers;
					int playerTurn = loadState.playerTurn;
					String[][] grid = loadState.grid;
					Player currentPlayer = loadState.currentPlayer;
					boolean gameResumed = loadState.gameResumed;
					Grid.main(gridSize, playerList,playerTurn,grid,currentPlayer,gameResumed);
				} 
				 catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (NullPointerException e) 
				{
					listPlayers= new ArrayList<Player>();
	            	String choice= cb.getSelectionModel().getSelectedItem().toString();
	        		numPlayers= Integer.parseInt(choice);
	        		String choice2= cb2.getSelectionModel().getSelectedItem().toString();
	        		int xflag= -1;
	        		if(!choice2.equals(""))
	        		{
	        			for(int i=0; i<choice2.length(); i++){
	        				if(choice2.charAt(i)== 'X'){
	        					xflag= i;
	        				}
	        			}
	        			gridSize[0]= Integer.parseInt(choice2.substring(0, xflag));
	        			gridSize[1]= Integer.parseInt(choice2.substring(xflag+1, choice2.length()));
	        		}
	        		else
	        		{
	        			gridSize[0]= 9;
	        			gridSize[1]= 6;
	        		}

	        		for(int i=0; i<numPlayers; i++)
	        		{
	        			Player p= new Player(i+1,"Player " + Integer.toString(i+1), colorList[i], gridSize);
	        			listPlayers.add(p);
	        		}
	        		try 
	        		{
	        			Grid.main(RunGame.gridSize, RunGame.listPlayers,0,new String[RunGame.gridSize[0]][RunGame.gridSize[1]],RunGame.listPlayers.get(0),false);
					} 
	        		catch (FileNotFoundException exc) 
	        		{
						
						exc.printStackTrace();
					}
				} 
				catch(IOException e)
				{
					e.printStackTrace();
				}
				catch (ClassNotFoundException e) 
				{
					e.printStackTrace();
				}
				finally
				{
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		
		Button btn3= new Button("Settings");
		btn3.setTranslateX(100);
		btn3.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0)
			{
				numPlayers= Integer.parseInt(cb.getSelectionModel().getSelectedItem());
				gridSize[0]= Character.getNumericValue(cb2.getSelectionModel().getSelectedItem().charAt(0));
				gridSize[1]= Character.getNumericValue(cb2.getSelectionModel().getSelectedItem().charAt(2));
				String[] args= new String[]{"1", "2"};//NO USE.
				try {
					Settings.main(args);
				} catch (BadColorSelectionException e) 
				{
					e.printStackTrace();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});

		Button btn = new Button("Play Game");
		
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	listPlayers= new ArrayList<Player>();
            	String choice= cb.getSelectionModel().getSelectedItem().toString();
        		numPlayers= Integer.parseInt(choice);
        		String choice2= cb2.getSelectionModel().getSelectedItem().toString();
        		int xflag= -1;
        		if(!choice2.equals("")){//means no grid size selected(provide a default grid-size)!
        			for(int i=0; i<choice2.length(); i++){
        				if(choice2.charAt(i)== 'X'){
        					xflag= i;
        				}
        			}
        			gridSize[0]= Integer.parseInt(choice2.substring(0, xflag));
        			gridSize[1]= Integer.parseInt(choice2.substring(xflag+1, choice2.length()));
        		}
        		else
        		{
        			gridSize[0]= 9;
        			gridSize[1]= 6;
        		}

        		for(int i=0; i<numPlayers; i++)
        		{
        			Player p= new Player(i+1,"Player " + Integer.toString(i+1), colorList[i], gridSize);
        			listPlayers.add(p);
        		}
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

        VBox root= new VBox();
        HBox hbox2= new HBox();
        Label heading = new Label("CHAIN REACTION");
        heading.setStyle("-fx-font-weight: bold");
        heading.setFont(Font.font(36));
        hbox2.setPadding(new Insets(20, 20, 20, 20));
        hbox2.setAlignment(Pos.TOP_CENTER);
        hbox2.getChildren().add(heading);
        root.getChildren().add(hbox2);
        HBox hbox3= new HBox();
        hbox3.setPadding(new Insets(30, 30, 30, 30));
        hbox3.setSpacing(40);
        Label label1= new Label("Number of Players: ");
        hbox3.getChildren().add(label1);
        hbox3.getChildren().add(cb);
        root.getChildren().add(hbox3);
        HBox hbox4= new HBox();
        hbox4.setPadding(new Insets(30, 30, 30, 30));
        hbox4.setSpacing(40);
        Label label2= new Label("Select Grid Size: ");
        hbox4.getChildren().add(label2);
        hbox4.getChildren().add(cb2);
        root.getChildren().add(hbox4);
        HBox hbox= new HBox();
        hbox.setPadding(new Insets(20, 20, 20, 20));
        btn.setMaxWidth(200);
        btn2.setMaxWidth(200);
        btn3.setMaxWidth(200);
        hbox.getChildren().addAll(btn, btn2, btn3);
        root.getChildren().add(hbox);
        Scene scene= new Scene(root, 470, 480);
        root.setStyle("-fx-background-color: #D3D3D3");
        mainstage.setScene(scene);
        mainstage.show();
	}

}
