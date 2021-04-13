
package sample;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.ImagePattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.util.Duration;
import javafx.animation.KeyFrame;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PathTransition;
import javafx.scene.paint.Color;
import com.sun.source.tree.BreakTree;
import javafx.scene.control.Cell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;


public class Main extends Application {
    private Custom[][] custom;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Scanner input = new Scanner(new File("C:\\Users\\User\\Desktop\\sdu\\CSS108\\MyTank\\src\\sample\\map.txt"));
        Player player = new MyPlayer();
        Map map =null;
        Game game = null;
        BorderPane mainPane = new BorderPane();
        //GridPane gridPane = new GridPane();
        Custom custom = new Custom();

        try {
             map = new Map(input,custom);
            game = new Game(map,mainPane);
        } catch (InvalidMapException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        custom = map.getPane();
        mainPane.setCenter(custom);
        game.addPlayer(player);
        mainPane = player.getMainPane();
        Scene scene = new Scene(mainPane,400,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP: player.moveUp();  break;
                    case RIGHT: player.moveRight(); break;
                    case DOWN: player.moveDown(); break;
                    case LEFT: player.moveLeft(); break;
                }
            }
        });


    }


    public static void main(String[] args) {
        launch(args);
    }
}

class VariousBarrier{
    private Image image;
    private ImageView imageView;
    private int blockSize =40;
    private Rectangle rect;
    VariousBarrier(String wallType, int i,int j){
        rect = new Rectangle(blockSize,blockSize);
        if(wallType.equals("S")){
            this.image = new Image("sample/img/SteelWall.png");
            rect.setFill(new ImagePattern(image, 0,0,1,1,true));
            rect.setX(j*blockSize);
            rect.setY(i*blockSize);
        }else if(wallType.equals("B")){
            this.image = new Image("sample/img/BrickWall.png");
            rect.setFill(new ImagePattern(image, 0,0,1,1,true));
            rect.setX(j*blockSize);
            rect.setY(i*blockSize);
        }else if(wallType.equals("W")){
            this.image = new Image("sample/img/Water.png");
            rect.setFill(new ImagePattern(image, 0,0,1,1,true));
            rect.setX(j*blockSize);
            rect.setY(i*blockSize);
        }else if(wallType.equals("T")){
            this.image = new Image("sample/img/Trees.png");
            rect.setFill(new ImagePattern(image, 0,0,1,1,true));
            rect.setX(j*blockSize);
            rect.setY(i*blockSize);
        }




    }
    public Rectangle getRect(){
        return this.rect;
    }

}
class Bullet{}


class Custom extends Pane{

    public Custom(){
        setStyle("-fx-background-color: black;");
    }
}

//Game class
class Game {
    Scanner input;
    private Map map;
    private Player player;
    private BorderPane pane;

    public Game(Map map,BorderPane pane) {
        this.map = map;
        this.pane =pane;
    }

    public void setMap(Map newMap) {

    }

    public void addPlayer(Player player) {
        this.player = player;
        player.setMap(this.map);
        player.setMainPane(this.pane);
    }

}

// Map class
class Map {
    private Custom custom;
    private int size;
    private String[][] fill;
   // private GridPane pane;
    private String[] moves;
    private VariousBarrier barrier;
    private ArrayList<Rectangle> walls= new ArrayList<>();

    public Map(Scanner scanner,Custom pane) throws InvalidMapException {

        this.custom = pane;
        this.size = scanner.nextInt();
        while(scanner.hasNext()) {
            if (size == 0) {
                throw new InvalidMapException("Map size can not be zero");
            }
            fill = new String[this.size][this.size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    fill[i][j] = scanner.next();
                    barrier = new VariousBarrier(fill[i][j],i,j);
                    walls.add(barrier.getRect());
                    custom.getChildren().add(barrier.getRect());
                    if (!fill[i][j].equals("0") && !fill[i][j].equals("S") && !fill[i][j].equals("P") && !fill[i][j].equals("B") && !fill[i][j].equals("W") && !fill[i][j].equals("T")) {
                        throw new InvalidMapException("Not enough map elements");
                    }
                }
            }
        }

    }

    public int getSize() {
        return this.size;
    }
    public char getValueAt(int i, int j) {
        return fill[j][i].charAt(0);
    }
    public void print() {
        for (String[] i : fill) {
            for (String j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }

    }


    public Custom getPane() {
        return this.custom;
    }
}

class InvalidMapException extends Exception {
    private static final long serialVersionUID = 1L;
    String string;

    public InvalidMapException(String string) {
        this.string = string;
    }

    @Override
    public String getMessage() {
        return this.string;
    }
}

interface Player {
    void setMap(Map newMap);

    void moveRight();

    void moveLeft();

    void moveUp();

    void moveDown();

    Position getPosition();

    void setMainPane(BorderPane pane);

    BorderPane getMainPane();

    void setScene(Scene scene);
    boolean isWallNex(double x);
}

class MyPlayer implements Player {
    private Map map;
    private Position position;
    private  Tank tank;
    private BorderPane mainPane;
    private Rectangle rectangle;
    private int blockSize =40;
    private int step = 3;
    Scene scene;
    @Override
    public void setScene(Scene scene){
        this.scene = scene;
    }

    @Override
    public boolean isWallNex(double x) {
        return false;
    }



    @Override
    public void setMap(Map map) {
        this.map = map;
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                if (map.getValueAt(i, j) == 'P') {
                    this.position = new Position(j, i);
                }
            }
        }


    }
    @Override
    public void setMainPane(BorderPane pane){
        this.mainPane = pane;
        this.tank = new Tank(this.position,pane);
        rectangle  = tank.getR();
    }
    @Override
    public BorderPane getMainPane(){
        return this.mainPane;
    }
    @Override
    public void moveRight() {

        if (rectangle.getTranslateX()<(map.getSize()-1)*blockSize ) {

        }
    }

    @Override
    public void moveLeft() {
        if (rectangle.getTranslateX()>0){
            int nextX = (int)((rectangle.getTranslateX()-step-3)/blockSize);
            int nextY =(int)(rectangle.getTranslateY()/blockSize);
            System.out.println(nextX+" "+nextY);
            System.out.println(map.getValueAt(nextX,nextY));
            char next= map.getValueAt(nextX,nextY);
            if(next == '0' || next == 'P' || next == 'T')
                rectangle.setTranslateX(rectangle.getTranslateX() -step);

        }
    }

    @Override
    public void moveUp() {
        if (rectangle.getTranslateY()>0){
            int nextX = (int)((rectangle.getTranslateX())/blockSize);
            int nextY =(int)((rectangle.getTranslateY()-step-3)/blockSize);
            System.out.println(nextX+" "+nextY);
            System.out.println(map.getValueAt(nextX,nextY));
            char next= map.getValueAt(nextX,nextY);
            if(next == '0' || next == 'P' || next == 'T')
                rectangle.setTranslateY(rectangle.getTranslateY() -step);

        }
    }

    @Override
    public void moveDown() {
        if (rectangle.getTranslateY() < (map.getSize()-1)*blockSize ){
            int nextX = (int)((rectangle.getTranslateX())/blockSize);
            int nextY =(int)((rectangle.getTranslateY()+blockSize)/blockSize);
            System.out.println(nextX+" "+nextY);
            System.out.println(map.getValueAt(nextX,nextY));
            char next= map.getValueAt(nextX,nextY);
            if(next == '0' || next == 'P' || next == 'T')
                rectangle.setTranslateY(rectangle.getTranslateY() +step);

        }
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

}
class Tank extends MyPlayer{
    private static final int      KEYBOARD_MOVEMENT_DELTA = 5;
    private static final Duration TRANSLATE_DURATION = Duration.seconds(2);
    private Rectangle rectangle;
    public Tank(Position position, BorderPane mainPane){
        this.rectangle = new Rectangle(32,32,Color.WHITE);
        rectangle.setTranslateX(position.getX()*40+4);
        rectangle.setTranslateY(position.getY()*40+4);
        TranslateTransition transition = new TranslateTransition(TRANSLATE_DURATION, rectangle);
        mainPane.getChildren().add(rectangle);
    }
    public Rectangle getR(){
        return this.rectangle;
    }


    public void moveCircleOnKeyPress(Scene scene) {

    }
}
class Position {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean equals(Position position) {
        if (this.x == position.getX() && this.y == position.getY()) {
            return true;
        } else
            return false;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

}

   