/**
 * 
 */
package at.dominik.coda.ide.gui.dialogues;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Window;

/**
 * @author Dominik Fluch
 *
 * Created on 05.05.2020
 *
 */
public class ErrorDialogue extends Dialogue {

	/**
	 * @param owner
	 */
	public ErrorDialogue(Window owner) {
		super(owner);
		
		this.getOkayButton().setOnAction((actionEvent) -> this.close());
		
		this.setResizable(false);
	}
	
	/**
	 * Sets the given message.
	 * @param message
	 */
	public void setMessage(String message) {
		this.getMessageLabel().setText(message);
	}
	
	/**
	 * @return the currently set message.
	 */
	public String getMessage() {
		return this.getMessageLabel().getText();
	}
	
	/**
	 * @return the message label.
	 */
	public Label getMessageLabel() {
		return (Label) this.getScene().lookup("#message");
	}
	
	/**
	 * @return the okay button.
	 */
	public Button getOkayButton() {
		return (Button) this.getScene().lookup("#okay");
	}
	
	@Override
	public String getName() {
		return "Error occured";
	}
	
}
