/**
 * 
 */
package at.dominik.coda.ide.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.dominik.coda.ide.CodaIDE;
import at.dominik.coda.ide.gui.workspace.editor.EditorHighlighting;
import at.dominik.coda.ide.plugins.standard.StandardPlugin;

/**
 * @author Dominik Fluch
 *
 * Created on 22.06.2020
 *
 */
public class PluginManager {

	private final List<Plugin> plugins;
	private final CodaIDE codaIDE;
	
	
	/**
	 * @param codaIDE
	 */
	public PluginManager(CodaIDE codaIDE) {
		this.plugins = new ArrayList<Plugin>(Arrays.asList(new StandardPlugin().setManager(this)));
		this.codaIDE = codaIDE;
	}
	
	/**
	 * Loads all plugins in the given folder.
	 * @param folder
	 */
	public void loadPlugins(File folder) {
		
	}
	
	/**
	 * @return the {@link EditorHighlighting}s from the plugins.
	 */
	public List<EditorHighlighting> getPluginEditorHighlightings() {
		final List<EditorHighlighting> highlightings = new ArrayList<EditorHighlighting>();
		
		for(Plugin plugin : this.getPlugins()) highlightings.addAll(plugin.getEditorHighlightings());
		
		return highlightings;
	}
	
	/**
	 * @return the plugins
	 */
	public List<Plugin> getPlugins() {
		return plugins;
	}
	
	/**
	 * @return the codaIDE
	 */
	public CodaIDE getCodaIDE() {
		return codaIDE;
	}
	
}
