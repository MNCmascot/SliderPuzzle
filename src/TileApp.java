import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.Duration;

/*
958 X 751

When pressing start, give it some time to shuffle
 */
public class TileApp extends Application {
    public Pane pane = new Pane();
    public String keyword; //used to determine which puzzle is selected
    public int ranX;
    public int ranY;
    public int seconds = 0;
    public int minutes = 0;
    public Label thumbnail = new Label();
    public Button button = new Button();
    public TextField timer = new TextField();
    public ListView<String> boardList = new ListView<String>();
    public Button[][] gameButtons = new Button[4][4];
    public String[][] gameImages = new String[4][4];
    public Point2D[][] gameCoords = new Point2D[4][4];
    public Timeline updateTimer = new Timeline();
    public void start (Stage primaryStage) {




        //Setup listview
        String[] boards = {"Pets", "Scenery", "Numbers", "Lego"};
        boardList.setItems(FXCollections.observableArrayList(boards));
        boardList.relocate(771, 197);
        boardList.setPrefSize(187, 150);
        boardList.getSelectionModel().select(0);
        pane.getChildren().add(boardList);

        //Setup Thumbnail
        thumbnail.setPrefSize(187,187);
        thumbnail.relocate(771, 0);
        selectImage();
        pane.getChildren().add(thumbnail);

        //Handle listview press
        boardList.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                selectImage();
            }
        });

        //setup button
        button.setPrefSize(187, 30);
        button.relocate(771, 357);
        setStart();
        pane.getChildren().add(button);

        //setup Time Label
        Label time = new Label("Time: ");
        time.setStyle("-fx-font: 22 System;");
        time.relocate(771, 397);
        pane.getChildren().add(time);

        //Setup Time Textfield
        timer.setText("0:00");
        timer.setAlignment(Pos.CENTER_LEFT);
        timer.relocate(831, 397);
        timer.setPrefSize(127, 30);
        timer.setEditable(false);
        pane.getChildren().add(timer);

        //Timer

        updateTimer = new Timeline(new KeyFrame(Duration.millis(1000),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                       seconds += 1;
                       if (seconds >= 60){
                           minutes += 1;
                           seconds = 0;
                       }
                        if (seconds < 10) {
                            timer.setText(Integer.toString(minutes) + String.format(":0%d", seconds));
                        } else{
                            timer.setText(Integer.toString(minutes) + ":" + Integer.toString(seconds));
                        }
                    }
                }));
        updateTimer.setCycleCount(Timeline.INDEFINITE);


        //Setup image buttons
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++) {
                //System.out.println(Integer.toString(x) + Integer.toString(y));
                gameButtons[x][y] = new Button();
                gameButtons[x][y].setPrefSize(187, 187);
                gameButtons[x][y].relocate(y * 188, x * 188);
                gameButtons[x][y].setPadding(new Insets(0, 0, 0, 0));
                pane.getChildren().add(gameButtons[x][y]);
                gameImages[x][y] = "BLANK.png";
                gameCoords[x][y] = new Point2D(y, x);
                //System.out.println(gameCoords[x][y]);
                //handle game button press
                gameButtons[x][y].setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        for (int row = 0; row < 4; row++) {
                            for (int col = 0; col < 4; col++) {
                                if (event.getSource() == gameButtons[row][col] && gameImages[row][col] != "BLANK.png"){
                                    swap(row, col);
                                }
                            }
                        }
                    }
                });
            }
        }
        setupButtons();

        //Handle button press
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (button.getText() == "Start"){
                    thumbnail.setDisable(true);
                    boardList.setDisable(true);
                    seconds = 0;
                    minutes = 0;
                    timer.setText("0:00");
                    updateTimer.play();
                    setStop();
                    keyword = boardList.getSelectionModel().getSelectedItem();
                    ranX = (int) (Math.random() * 4);
                    ranY = (int) (Math.random() * 4);
                    setBlankImages();
                    setupImages();

                    int row, col;
                    //shuffle image (NOT 5000 TIMES, BECAUSE LAPTOP)
                    for (int i = 0; i < 1500; i++){
                        row = (int)(Math.random() * 4);
                        col = (int)(Math.random() * 4);
                        swap(row, col);
                    }
                    setupButtons();

                    //enableGameButtons
                    for (int x = 0; x < 4; x++){
                        for (int y = 0; y < 4; y++){
                            gameButtons[x][y].setDisable(false);
                        }
                    }
                } else{
                    updateTimer.stop();
                    thumbnail.setDisable(false);
                    boardList.setDisable(false);
                    setBlankImages();
                    setupButtons();
                    setStart();

                }
            }
        });




        primaryStage.setTitle("Slider Puzzle Game");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(pane, 958,751));
        primaryStage.show();

    }

//OTHER METHODS



    //Select Thumbnail image
    public void selectImage(){
        String image = boardList.getSelectionModel().getSelectedItem();
        if (image == "Pets"){
            thumbnail.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Pets_Thumbnail.png"))));
        } else if (image == "Scenery"){
            thumbnail.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Scenery_Thumbnail.png"))));
        } else if (image == "Lego"){
            thumbnail.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Lego_Thumbnail.png"))));
        } else {
            thumbnail.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Numbers_Thumbnail.png"))));
        }
    }

    //Set button to Start
    public void setStart(){
        button.setText("Start");
        button.setStyle("-fx-text-fill: WHITE; -fx-base: DARKGREEN");
    }

    //set Button to Stop
    public void setStop(){
        button.setText("Stop");
        button.setStyle("-fx-text-fill: WHITE; -fx-base: DARKRED");
    }

    //Setup game buttons based on game images
    public void setupButtons(){
        for (int x = 0; x < 4; x ++){
            for (int y = 0; y < 4; y++){
                gameButtons[x][y].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(gameImages[x][y]))));
            }
        }
    }


    //setup game images
    public void setupImages(){
      //  System.out.println(ranX);
       // System.out.println(ranY);
        for (int x = 0; x < 4; x ++) {
            for (int y = 0; y < 4; y++) {
                //reversed to account for order
                if (y == ranX && x == ranY) {
                    continue;
                } else {
                    gameImages[x][y] = keyword + "_" + Integer.toString(x) + Integer.toString(y) + ".png";
                }
            }
        }
    }

    //setImagesBlank
    public void setBlankImages(){
        for (int x = 0; x < 4; x ++) {
            for (int y = 0; y < 4; y++) {
                //System.out.println(gameImages[x][y]);
                gameImages[x][y] = "BLANK.png";
            }
        }
    }

    //swap picture clicked on with blank spot
    public void swap(int row,int col){
       // System.out.println("swap call");
        int rowCheck = row, colCheck = col;

        //check up
        if (row > 0){
            rowCheck = row-1;
           // System.out.println(gameImages[rowCheck][colCheck]);
            if (gameImages[rowCheck][colCheck] == "BLANK.png"){
                gameImages[rowCheck][colCheck] = gameImages[row][col];
                gameImages[row][col] = "BLANK.png";
                setupButtons();
                Point2D temp = gameCoords[rowCheck][colCheck];
                gameCoords[rowCheck][colCheck] = gameCoords[row][col];
                gameCoords[row][col] = temp;
                checkWin();
                return;
            }
        }

        //check down
        if (row < 3){
            rowCheck = row+1;
            colCheck = col;
            //System.out.println(gameImages[rowCheck][colCheck]);
            if (gameImages[rowCheck][colCheck] == "BLANK.png"){
                gameImages[rowCheck][colCheck] = gameImages[row][col];
                gameImages[row][col] = "BLANK.png";
                setupButtons();
                Point2D temp = gameCoords[rowCheck][colCheck];
                gameCoords[rowCheck][colCheck] = gameCoords[row][col];
                gameCoords[row][col] = temp;
                checkWin();
                return;
            }
        }
        //check left
        if (col > 0){
            colCheck = col-1;
            rowCheck = row;
           // System.out.println(gameImages[rowCheck][colCheck]);
            if (gameImages[rowCheck][colCheck] == "BLANK.png"){
                gameImages[rowCheck][colCheck] = gameImages[row][col];
                gameImages[row][col] = "BLANK.png";
                setupButtons();
                Point2D temp = gameCoords[rowCheck][colCheck];
                gameCoords[rowCheck][colCheck] = gameCoords[row][col];
                gameCoords[row][col] = temp;
                checkWin();
                return;
            }
        }

        //check right
        if (col < 3){
            colCheck = col+1;
            rowCheck = row;
         //   System.out.println(gameImages[rowCheck][colCheck]);
            if (gameImages[rowCheck][colCheck] == "BLANK.png"){
                gameImages[rowCheck][colCheck] = gameImages[row][col];
                gameImages[row][col] = "BLANK.png";
                setupButtons();
                Point2D temp = gameCoords[rowCheck][colCheck];
                gameCoords[rowCheck][colCheck] = gameCoords[row][col];
                gameCoords[row][col] = temp;

                checkWin();
                return;
            }
        }

    }

    //Check if player won
    public void checkWin(){
        //System.out.println("Check win");
        Boolean win = true;
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++){
                Point2D temp = new Point2D(x, y);
               // System.out.println("x: " + x);
               // System.out.println("y: " + y);
               // System.out.println(gameCoords[y][x]);
              //  System.out.println(temp);
                if (!gameCoords[y][x].equals(temp)){
                    win = false;
                }
            }
        }
        //System.out.println(win);
        if (win){
            updateTimer.stop();
            setStart();
            thumbnail.setDisable(false);
            boardList.setDisable(false);
            gameImages[ranY][ranX] = keyword + "_" + Integer.toString(ranY) + Integer.toString(ranX) + ".png";
            //disable buttons
            for (int x = 0; x < 4; x++){
                for (int y = 0; y < 4; y++) {
                    gameButtons[x][y].setDisable(true);
                }
            }
            setupButtons();
        }
    }




    //Main start Stage
    public static void main(String[] args){
        launch(args);
    }


}
