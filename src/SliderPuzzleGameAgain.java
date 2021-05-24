import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SliderPuzzleGameAgain extends Application {

	private int selection;
	private int randomRow;
	private int randomCol;
	private String fileName;
	private int numSec;
	private int displayMin;
	private int displaySec;
	private int above;
	private int below;
	private int left;
	private int right;

	public void start(Stage primaryStage) {

		String[] names = { "Lego", "Numbers", "Pets", "Scenery" }; // name of puzzle
		selection = 0;

		Pane aPane = new Pane();
		aPane.setStyle("-fx-padding: 10 10");

		// creating the thumbnail
		Label thumbnail = new Label();
		thumbnail.setGraphic(
				new ImageView(new Image(getClass().getResourceAsStream(names[selection] + "_Thumbnail.png"))));
		thumbnail.setStyle("-fx-padding: 10 10");
		thumbnail.relocate(750, 10);

		// creating the options ListView
		ListView<String> nameList = new ListView<String>();
		nameList.setItems(FXCollections.observableArrayList(names));
		nameList.getSelectionModel().select(0); // initial = Lego
		nameList.setStyle("-fx-padding: 10 10");
		nameList.relocate(760, 200); // this seems off??????????
		nameList.setPrefSize(187, 200); // assumed the size for this one, not specified in the instructions

		nameList.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				String selectedName = nameList.getSelectionModel().getSelectedItem();
				for (int i = 0; i < 4; i++) {
					if (selectedName.equals(names[i])) {
						selection = i;
						thumbnail.setGraphic(new ImageView(
								new Image(getClass().getResourceAsStream(names[selection] + "_Thumbnail.png"))));
					}
				}
			}
		});

		// fullImage.setGraphic(
		// new ImageView(new Image(getClass().getResourceAsStream(names[selection] +
		// "_Thumbnail.png"))));

		aPane.getChildren().addAll(nameList, thumbnail);

		Button[][] bArray = new Button[4][4];
		for (int r = 0; r < 4; r++)
			for (int c = 0; c < 4; c++) {
				bArray[r][c] = new Button();
				bArray[r][c].setPrefSize(187, 187);
				bArray[r][c].setPadding(new Insets(0, 0, 0, 0));
				fileName = "BLANK.png";
				// String fileName = names[selection] + "_" + c + r + ".png";
				bArray[r][c].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(fileName))));
				bArray[r][c].relocate(r * 188, c * 188);

				aPane.getChildren().add(bArray[r][c]);
			}

		// creating the Start/Stop Button
		Button startStopButton = new Button();
		startStopButton.setText("Start");
		startStopButton.relocate(760, 410);
		startStopButton.setPrefSize(187, 30);
		startStopButton.setStyle("-fx-text-fill: WHITE; -fx-base: DARKGREEN");

		// creating the Time Label
		Label timeLabel = new Label();
		timeLabel.setText("Time: ");
		timeLabel.relocate(760, 450);

		// creating the time elapsed text field
		TextField timeElapsedField = new TextField();
		timeElapsedField.setText("0:00");
		displayMin = 0;
		displaySec = 0;
		timeElapsedField.setAlignment(Pos.CENTER_LEFT);
		timeElapsedField.relocate(800, 448);
		timeElapsedField.setPrefSize(148, 25);

		numSec = 0;
		Timeline updateTimer;
		updateTimer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// FILL IN YOUR CODE HERE THAT WILL GET CALLED ONCE PER SEC.
				// String text = startStopButton.getText();
				// System.out.printf("text = %s", text);
				numSec++;
				//System.out.printf("numSec = %d\n", numSec);
			}
		}));
		updateTimer.setCycleCount(Timeline.INDEFINITE);

		// pressed "Start"
		startStopButton.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				if ("Start".equals(startStopButton.getText())) {
					thumbnail.setDisable(true); // disable the thumbnail

					updateTimer.play();

					startStopButton.setText("Stop");
					startStopButton.setStyle("-fx-text-fill: WHITE; -fx-base: DARKRED");

					displayMin = (int) (numSec / 60);
					displaySec = displaySec - 60 * displayMin;
					timeElapsedField.setText(String.format("%d:%02d", displayMin, displaySec));

					randomRow = (int) (Math.random() * 4);
					// System.out.printf("randomRow = %d\n", randomRow);
					randomCol = (int) (Math.random() * 4);
					// System.out.printf("randomCol = %d\n", randomCol);
					for (int r = 0; r < 4; r++)
						for (int c = 0; c < 4; c++) {
							if (r == randomCol && c == randomRow) // weird but works............
								fileName = "BLANK.png";
							else
								fileName = names[selection] + "_" + c + r + ".png";
							bArray[r][c].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(fileName))));
							
							bArray[r][c].setOnAction(new EventHandler<ActionEvent>() {
								public void handle(ActionEvent event) {
									// Find the row and column of the pressed button
									for (int r = 0; r < 4; r++) {
										for (int c = 0; c < 4; c++) {
											if (event.getSource() == bArray[c][r]) {
												System.out.printf("b press: r = %d, c = %d\n", r, c);
												if (r > 0) {
													above = r - 1;
													System.out.println("above " + above);
//													System.out.printf("above = %d, c = %d\n", above, c);
//													System.out.printf("rRow = %d, rCol = %d\n", randomRow, randomCol);
													if (above == randomRow && c == randomCol) {	// might have to switch var again...
														System.out.println("randomCol " + randomCol);
														System.out.println("randomRow " + randomRow);
														bArray[c][r].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
														fileName = names[selection] + "_" + c + above + ".png";
														bArray[c][above].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(fileName))));
														randomRow = r;
														randomCol = c;
														System.out.println("randomCol after " + randomCol);
														System.out.println("randomRow after  " + randomRow);
													}
												}
												if (r < 3) {
													below = r + 1;
													System.out.println("below " + below);

													if (below == randomRow && c == randomCol) {// might have to switch var again...
														System.out.println("below");
														bArray[c][r].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
														fileName = names[selection] + "_" + c + below + ".png";
														bArray[c][below].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(fileName))));
														randomRow = r;
														randomCol = c;
													}
												}
												if (c > 0) {
													left = c - 1;
													System.out.println("left" + left);
													System.out.println("randomCol " + randomCol);
													System.out.println("randomRow " + randomRow);
													if (r == randomRow && left == randomCol) {// might have to switch var again...
														System.out.println("left");

														bArray[c][r].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
														fileName = names[selection] + "_" + left + r + ".png";
														bArray[left][r].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(fileName))));
														randomRow = c;
														randomCol = r;
													}
												}
												if (c < 3) {
													right = c + 1;
													System.out.println("randomCol " + randomCol);
													System.out.println("randomRow " + randomRow);
													System.out.println("right" + right);
													if (r == randomRow && right == randomCol) {// might have to switch var again...
														System.out.println("right");
														bArray[c][r].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
														fileName = names[selection] + "_" + right + r + ".png";
														bArray[right][r].setGraphic(new ImageView(new Image(getClass().getResourceAsStream(fileName))));
														randomRow = c;
														randomCol = r;
													}
												}
												
											}
										}
									}
								}
							});
						}

				} else { // text on button = Stop
					updateTimer.stop();

					thumbnail.setDisable(false); // enable the thumbnail

					startStopButton.setText("Start"); // not sure.............................
					startStopButton.setStyle("-fx-text-fill: WHITE; -fx-base: DARKGREEN");

					for (int r = 0; r < 4; r++)
						for (int c = 0; c < 4; c++)
							bArray[r][c]
									.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("BLANK.png"))));
				}
			}
		});

		/*
		 * if ("Start".equals(startStopButton.getText())) {
		 * startStopButton.setOnAction(new EventHandler<ActionEvent>() { public void
		 * handle(ActionEvent event) { // Find the row and column of the pressed button
		 * for (int r = 0; r < 4; r++) { for (int c = 0; c < 4; c++) { if
		 * (event.getSource() == bArray[r][c]) {
		 * System.out.printf("button pressed: r = %d, c = %d", r, c); } } } } }); }
		 */

		aPane.getChildren().addAll(startStopButton, timeLabel, timeElapsedField);

		primaryStage.setTitle("Slider Puzzle Game");
		primaryStage.setScene(new Scene(aPane, 1000, 900)); // size not certain.......
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}