/**
 * 
 */
package at.dominik.coda.ide;

import at.dominik.coda.ide.gui.dialogues.WorkbenchChooseDialogue;
import at.dominik.coda.ide.gui.workspace.Workbench;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Dominik Fluch
 *
 * Created on 01.05.2020
 *
 */
public class CodaIDE extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		
		final WorkbenchChooseDialogue dialogue = new WorkbenchChooseDialogue(stage);
		dialogue.showAndWait();
		
		final Stage loading = this.showLoadingScreen();
		stage.setOnShown((windowEvent) -> loading.hide());
		stage.getIcons().add(new Image(this.getClass().getResourceAsStream("gui/assets/logo.png")));
		stage.setOnCloseRequest((windowEvent) -> {
			
			stage.hide();
			System.exit(0);
			
		});
		
		stage.setTitle("codaIDE");
		stage.setMinWidth(100D);
		stage.setMinHeight(100D);
		
		final Task<Workbench> task = new Task<Workbench>() {

			@Override
			protected Workbench call() throws Exception {
				try {
					return new Workbench(dialogue.getChosenWorkspace());
				}catch(Exception exception) {exception.printStackTrace();
					return null;
				}
			}
			
		};
		
		task.setOnSucceeded((workerStateEvent) -> {
			stage.setScene(task.getValue().createScene());
			stage.show();
		});
		

		new Thread(task).start();
		
		
	}
	
	/**
	 * Creates and shows a loading screen.
	 * @return the created loading screen.
	 */
	private Stage showLoadingScreen() {
		final Stage stage = new Stage();
		
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setAlwaysOnTop(true);
		
		final ImageView imageView = new ImageView(new Image(this.getClass().getResourceAsStream("gui/assets/logo.png")));
		imageView.setFitWidth(200D);
		imageView.setFitHeight(200D);
		
		final Text text = new Text("codaIDE");
		text.setFill(Color.WHITE);
		text.setFont(Font.font(30F));
		
		text.setEffect(new DropShadow(2D, Color.BLACK));
		
		final VBox pane = new VBox(imageView, text);
		pane.setSpacing(7D);
		pane.setStyle("-fx-background-color: transparent;");
		pane.setAlignment(Pos.CENTER);
		
		final Scene scene = new Scene(pane);
		scene.setFill(Color.TRANSPARENT);
		
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		
		return stage;
	}
	
	/**
	 * Fallback option to run codaIDE.
	 * @param args
	 */
	public static void main(String[] args) {
		CodaIDE.launch(args);
	}
	
}
