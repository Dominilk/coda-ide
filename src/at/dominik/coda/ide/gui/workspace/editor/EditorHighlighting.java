/**
 * 
 */
package at.dominik.coda.ide.gui.workspace.editor;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.fxmisc.richtext.model.StyleSpans;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;

/**
 * @author Dominik Fluch
 *
 * Created on 04.05.2020
 *
 */
public interface EditorHighlighting {

	/**
	 * Gets called when a key is pressed inside the editor.
	 * @param editor		The editor they key is pressed in.
	 * @param event
	 */
	public default void onKeyPress(Editor editor, KeyEvent event) {}
	
	/**
	 * Gets called when a key is pressed inside the editor.
	 * @param editor		The editor they key is pressed in.
	 * @param event
	 */
	public default void onKeyRelease(Editor editor, KeyEvent event) {}
	
	/**
	 * @return all the event handlers the highlighting needs.
	 */
	public default <T extends Event> Map<EventType<T>, EventHandler<T>> getEventHandlers() {
		return null;
	}
	
	/**
	 * @return the Pattern of the highlighting.
	 */
	public Pattern getPattern();
	
	/**
	 * @param text
	 * @return the highlighting of the given text.
	 */
	public StyleSpans<Collection<String>> compute(String text);
	
	/**
	 * @return the style sheet containing all the style of
	 * the patterns.
	 */
	public URL getStylesheet();
	
	/**
	 * @return all supported file types of the highlighting.
	 */
	public String[] getSupportedFileTypes();
	
}
