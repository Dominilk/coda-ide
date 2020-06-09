/**
 * 
 */
package at.dominik.coda.ide.gui.dialogues;

import java.io.IOException;

import at.dominik.coda.ide.exceptions.UnhandledException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author Dominik Fluch
 *
 * Created on 05.05.2020
 *
 */
public class Dialogue extends Stage {

	/**
	 * @param owner
	 */
	public Dialogue(Window owner) {
		this.initOwner(owner);
		this.initModality(Modality.WINDOW_MODAL);
		this.setTitle(this.getName());
		this.setScene(this.createScene());
	}
	
	/**
	 * @return a scene for the loaded content.
	 */
	protected Scene createScene() {
		try {
			return new Scene(this.loadContent());
		} catch (IOException exception) {
			throw new UnhandledException(exception);
		}
	}
	
	/**
	 * @return the loaded content.
	 */
	public Parent loadContent() throws IOException {
		return FXMLLoader.load(this.getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
	}
	
	/**
	 * @return the name of the dialogue.
	 */
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	
}
