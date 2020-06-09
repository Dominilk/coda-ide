/**
 * 
 */
package at.dominik.coda.ide.gui.workspace.editor;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.beans.value.ObservableValue;

/**
 * @author Dominik Fluch
 *
 * Created on 04.05.2020
 *
 */
public class Editor extends CodeArea {

	private EditorHighlighting highlighting;
	
	/**
	 * 
	 */
	public Editor() {
		this.setParagraphGraphicFactory(LineNumberFactory.get(this));
		
		this.getStylesheets().add(this.getClass().getResource("Editor.css").toExternalForm());
		
		this.textProperty().addListener(this::updateHighlighting);
		
		this.setOnKeyPressed(event -> {
			
			if(Editor.this.getHighlighting() != null)Editor.this.getHighlighting().onKeyPress(Editor.this, event);
			
		});
		
		this.setOnKeyReleased(event -> {
			
			if(Editor.this.getHighlighting() != null)Editor.this.getHighlighting().onKeyRelease(Editor.this, event);
			
		});
	}
	
	/**
	 * Updates the text highlighting.
	 * @param observable
	 * @param oldValue
	 * @param newValue
	 */
	private void updateHighlighting(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if(this.getHighlighting() != null)this.setStyleSpans(0, this.getHighlighting().compute(newValue));
	}
	
	/**
	 * Updates the text highlighting.
	 */
	public void updateHighlighting() {
		this.updateHighlighting(null, null, this.getText());
	}
	
	/**
	 * @return the highlighting
	 */
	public EditorHighlighting getHighlighting() {
		return highlighting;
	}
	
	/**
	 * @param highlighting the highlighting to set
	 */
	public void setHighlighting(EditorHighlighting highlighting) {
		
		if(this.getHighlighting() != null)this.getStylesheets().remove(this.getHighlighting().getStylesheet().toExternalForm());
		
		this.highlighting = highlighting;
		
		this.getStylesheets().add(highlighting.getStylesheet().toExternalForm());
	}
	
}
