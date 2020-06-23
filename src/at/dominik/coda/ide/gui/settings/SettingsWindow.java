/**
 * 
 */
package at.dominik.coda.ide.gui.settings;

import java.io.IOException;

import org.controlsfx.control.ToggleSwitch;

import at.dominik.coda.ide.gui.dialogues.ErrorDialogue;
import at.dominik.coda.ide.gui.workspace.Workbench;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Dominik Fluch
 *
 * Created on 12.06.2020
 *
 */
public class SettingsWindow extends Stage {

	/**
	 * @param workbench
	 */
	public SettingsWindow(Workbench workbench) {
		this.initOwner(workbench.getGround().getScene().getWindow());
		this.initStyle(StageStyle.UNDECORATED);
		this.setTitle("Settings");
		
		try {
			this.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("Settings.fxml"))));
		} catch (IOException exception) {
			final ErrorDialogue dialogue = new ErrorDialogue(workbench.getGround().getScene().getWindow());
			dialogue.setMessage(exception.getMessage());
			dialogue.show();
		}
		
		double[] mouse = new double[2];
		
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
			mouse[0] = event.getSceneX();
			mouse[1] = event.getSceneY();
		});
		
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, (event) -> {
			this.setX(event.getScreenX() - mouse[0]);
			this.setY(event.getScreenY() - mouse[1]);
		});
		
		this.getClose().setOnAction((event) -> this.hide());
		
		this.centerOnScreen();
	}
	
	/**
	 * @return the close button.
	 */
	public Button getClose() {
		return (Button) this.getScene().lookup("#close");
	}
	
	/**
	 * @return the stickyTerminal
	 */
	public boolean isTerminalSticky() {
		return ((ToggleSwitch)this.getScene().lookup("#stickyTerm")).isSelected();
	}
	
}
