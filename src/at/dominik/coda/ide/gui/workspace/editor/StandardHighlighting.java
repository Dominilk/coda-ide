/**
 * 
 */
package at.dominik.coda.ide.gui.workspace.editor;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

/**
 * @author Dominik Fluch
 *
 * Created on 04.05.2020
 *
 */
public class StandardHighlighting implements EditorHighlighting {

	private static final Pattern PATTERN = Pattern.compile("");

	@Override
	public Pattern getPattern() {
		return StandardHighlighting.PATTERN;
	}

	@Override
	public StyleSpans<Collection<String>> compute(String text) {
		final StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<Collection<String>>();
		builder.add(Collections.emptyList(), 0);
		return builder.create();
	}

	@Override
	public URL getStylesheet() {
		return this.getClass().getResource("JavaHighlighting.css");
	}

	@Override
	public String[] getSupportedFileTypes() {
		return new String[] {};
	}
	
}
