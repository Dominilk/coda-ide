/**
 * 
 */
package at.dominik.coda.ide.plugins.standard;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import at.dominik.coda.ide.gui.workspace.editor.Editor;
import at.dominik.coda.ide.gui.workspace.editor.SimpleHighlighting;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;

/**
 * @author Dominik Fluch
 *
 * Created on 04.05.2020
 *
 */
public class CodaHighlighting implements SimpleHighlighting {

	private static final String[] KEYWORDS = new String[] {
			
			"abstract",
			"assert",
			"boolean",
			"break",
			"byte",
			"case",
			"catch",
			"char",
			"class",
			"continue",
			"default",
			"double",
			"else",
			"extends",
			"final",
			"for",
			"if",
			"implements",
			"import",
			"instanceof",
			"interface",
			"int",
			"long",
			"native",
			"new",
			"package",
			"private",
			"protected",
			"public",
			"return",
			"true",
			"false",
			"static",
			"super",
			"switch",
			"this",
			"throw",
			"string",
			"void",
			"as",
			"while",
			"load",
			"var",
			"null",
			"nullptr",
			"nothing",
			"empty",
			"try"
			
	};
	
	private static final Pattern PATTERN = Pattern.compile(

            "(?<KEYWORD>" + "\\b(" + String.join("|", CodaHighlighting.KEYWORDS) + ")\\b" + ")"

            + "|(?<PARENT>\\(|\\))"

            + "|(?<BRACE>\\{|\\})"

            + "|(?<BRACKET>\\[|\\])"

            + "|(?<SEMICOLON>\\;)"

            + "|(?<STRING>\"([^\"\\\\]|\\\\.)*\")"

            + "|(?<COMMENT>//[^\n]*" + "|" + "#[^\n]*" + "|" + "#[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/)"
            
			+ "|(?<INSTANCE>\\b(" + String.join("|", "new" + ")\\b)")
			
			+ "|(?<THIS>\\b(" + String.join("|", "this" + ")\\b)")

    );
	
	@Override
	public Pattern getPattern() {
		return CodaHighlighting.PATTERN;
	}
	
	@Override
	public void onKeyPress(Editor editor, KeyEvent event) {
		if(event.getCode() == KeyCode.SPACE && event.isControlDown()) {
			Popup popup = new Popup();
			final ListView<Object> view = new ListView<>();
			
			final Label l = new Label("Test");
			
			view.getItems().add(l);
			view.getItems().add(new Label("Lolol"));
			
			popup.getContent().add(view);
			Bounds pointer = editor.caretBoundsProperty().getValue().get();
			popup.show(editor.getScene().getWindow(), pointer.getMaxX(), pointer.getMinY());
		}
		
	}
	
	@Override
	public StyleSpans<Collection<String>> compute(String text) {
		final Matcher matcher = PATTERN.matcher(text);
		final StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		
		int lastKeywordEnd = 0;
		
		while(matcher.find()) {
			
			String styleClass =

					matcher.group("KEYWORD") != null ? "keyword" :
						
					matcher.group("PARENT") != null ? "parent" :

                    matcher.group("BRACE") != null ? "brace" :

                    matcher.group("BRACKET") != null ? "bracket" :

                    matcher.group("SEMICOLON") != null ? "semicolon" :

                    matcher.group("STRING") != null ? "string" :

                    matcher.group("COMMENT") != null ? "comment" :
                    	
                    matcher.group("INSTANCE") != null ? "instance" :
                    	
                    matcher.group("THIS") != null ? "this" :
                    	
                    null;
			
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());

			lastKeywordEnd = matcher.end();
		}

		spansBuilder.add(Collections.emptyList(), text.length() - lastKeywordEnd);
	       
		return spansBuilder.create();
	}

	@Override
	public URL getStylesheet() {
		return this.getClass().getResource("JavaHighlighting.css");
	}

	@Override
	public String[] getSupportedFileTypes() {
		return new String[] {"CODA"};
	}
	
}
