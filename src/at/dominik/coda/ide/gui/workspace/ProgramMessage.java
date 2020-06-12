/**
 * 
 */
package at.dominik.coda.ide.gui.workspace;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * @author Dominik Fluch
 *
 * Created on 08.06.2020
 *
 */
public class ProgramMessage {
	
	private final Color color;
	private final String message;
	
	/**
	 * @param color
	 * @param message
	 */
	public ProgramMessage(Color color, String message) {
		this.color = color;
		this.message = message;
	}
	
	/**
	 * @param message
	 */
	public ProgramMessage(String message) {
		this(Color.BLACK, message);
	}
	
	/**
	 * @return the message converted to a javafx text.
	 */
	public Text toText() {
		final Text text = new Text(this.getMessage() + "\n");
		text.setFill(this.getColor());
		return text;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
}
