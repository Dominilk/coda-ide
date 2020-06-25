/**
 * 
 */
package at.dominik.coda.ide.plugins.standard;

import at.dominik.coda.ide.plugins.Plugin;

/**
 * @author Dominik Fluch
 *
 * Created on 26.06.2020
 *
 */
public final class StandardPlugin extends Plugin {

	/**
	 * 
	 */
	public StandardPlugin() {
		this.getEditorHighlightings().add(new JavaHighlighting());
		this.getEditorHighlightings().add(new CodaHighlighting());
	}
	
	@Override
	public String getAuthor() {
		return "Dominik Fluch";
	}

}
