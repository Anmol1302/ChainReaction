package Backend;

import java.io.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Grid extends Application implements Serializable
{
	private Pane root = new Pane();
	private int[] gridSize={15,10};
	boolean playable;
	private Color gridColor;
	
	/*Grid(int[] size)
	{
		gridSize=size;
	}*/
	private Parent createContent() throws FileNotFoundException
	{
		root.setPrefSize(40+gridSize[1]*50,95+gridSize[0]*50);
		
		//Add the blue separator line
		Line topLine = new Line();
		topLine.setStroke(Color.BLUE);
		topLine.setStartX(0); 
		topLine.setStartY(50); 
		topLine.setEndX(40+gridSize[1]*50); 
		topLine.setEndY(50);
		topLine.setStrokeWidth(4);
		root.getChildren().add(topLine);
		
		//Add heading
		Text heading = new Text();
		heading.setText("Chain Reaction");
		heading.setTextOrigin(VPos.TOP);
		heading.setFont(Font.font(36));
		root.getChildren().add(heading);
		
		//Add the cells
		for(int i=0 ; i<gridSize[0] ; i++)
		{
			for(int j=0 ; j<gridSize[1] ; j++)
			{
				Cell box;
				if((i==0 || i==gridSize[0]-1) && (j==0 || j==gridSize[1]-1))
				{
					box = new Cell(2);
				}
				else if(i==0 || i==gridSize[0]-1 || j==0 || j==gridSize[1]-1)
				{
					box = new Cell(3);
				}
				else
				{
					box = new Cell(4);
				}
				box.setTranslateX(20+(j * 50));
                box.setTranslateY(75+(i * 50));

                root.getChildren().add(box);
			}
		}
		return root;
	}

	public class Cell extends StackPane implements Serializable
	{
		int criticalMass;
		boolean isEmpty;
		Group orb;
		int numOrbs=0;
		Cell(int criticalMass)
		{
			orb = new Group();
			this.criticalMass=criticalMass;
			isEmpty=true;
			
			Rectangle box = new Rectangle(50,50);
			box.setFill(null);
			box.setStroke(Color.BLACK);
			setAlignment(Pos.CENTER);
			getChildren().add(box);
			
			setOnMouseClicked(event -> 
			{
				if(numOrbs<this.criticalMass-1)
				{
					addOrb();
				}	
				else
					return;
            });
			getChildren().add(orb);
		}
		private void addOrb()
		{
			orb.getChildren().clear();
			if(numOrbs==0)
			{
				Sphere sphere = new Sphere(10);
				sphere.setMaterial(new PhongMaterial(Color.RED));
				orb.getChildren().add(sphere);
			}
			else if(numOrbs==1)
			{
				Sphere sphere_1 = new Sphere(10);
				sphere_1.setMaterial(new PhongMaterial(Color.RED));
				sphere_1.setTranslateX(32.5);
				Sphere sphere_2 = new Sphere(10);
				sphere_2.setMaterial(new PhongMaterial(Color.RED));
				sphere_2.setTranslateX(17.5);
				orb.getChildren().addAll(sphere_1,sphere_2);
					
			}
			else if(numOrbs==2)
			{
				Sphere sphere_3 = new Sphere(10);
				sphere_3.setMaterial(new PhongMaterial(Color.RED));
				sphere_3.setTranslateX(32.5);
				Sphere sphere_4 = new Sphere(10);
				sphere_4.setMaterial(new PhongMaterial(Color.RED));
				sphere_4.setTranslateX(17.5);
				Sphere sphere_5 = new Sphere(10);
				sphere_5.setMaterial(new PhongMaterial(Color.RED));
				sphere_5.setTranslateX(25);
				sphere_5.setTranslateY(12.5);
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
	
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }
	
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
