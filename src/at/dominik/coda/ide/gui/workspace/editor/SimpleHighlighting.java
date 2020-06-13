/**
 * 
 */
package at.dominik.coda.ide.gui.workspace.editor;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author Dominik Fluch
 *
 * Created on 12.06.2020
 *
 */
public interface SimpleHighlighting extends EditorHighlighting {
	
	@Override
	public default void onKeyPress(Editor editor, KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER && editor.getCaretPosition() >= 2 && editor.getCurrentParagraph() >= 1) {
			final String indent = SimpleHighlighting.indent(editor.getText(editor.getCurrentParagraph() - 1));
			
			final int caretPosition = editor.getCaretPosition();
			
			editor.insertText(caretPosition, SimpleHighlighting.indent(editor.getText(editor.getCurrentParagraph() - 1)));
			
			if(editor.getText(caretPosition - 2, caretPosition - 1).equals("{") && (editor.getParagraphs().size() >= editor.getCurrentParagraph() + 1 || !SimpleHighlighting.indent(editor.getText(editor.getCurrentParagraph())).equals(SimpleHighlighting.indent(editor.getText(editor.getCurrentParagraph()))))) {
				editor.insertText(editor.getCaretPosition(), "\n" + indent);
				editor.insertText(editor.getCurrentParagraph() - 1, 0, "\t");
				editor.moveTo(editor.getCurrentParagraph(), editor.getText(editor.getCurrentParagraph()).length());
			}
			
		}else if(event.getText().equals("(")) {
			editor.insertText(editor.getCaretPosition(), ")");
			editor.moveTo(editor.getCaretPosition() - 1);
		}else if(event.getText().equals("{")) {
			editor.insertText(editor.getCaretPosition(), "}");
			editor.moveTo(editor.getCaretPosition() - 1);
		}else if(event.getText().equals("[")) {
			editor.insertText(editor.getCaretPosition(), "]");
			editor.moveTo(editor.getCaretPosition() - 1);
		}else if(event.getText().equals("\"")) {
			editor.insertText(editor.getCaretPosition(), "\"");
			editor.moveTo(editor.getCaretPosition() - 1);
		}
	}
	
	/**
	 * @param above
	 * @return the indent of the given text.
	 */
	public static String indent(String above) {
		String indent = "";
		
		for(char c : above.toCharArray()) if(c == '\t')indent += c;
		
		return indent;
	}
}
