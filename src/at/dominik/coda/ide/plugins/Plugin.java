/**
 * 
 */
package at.dominik.coda.ide.plugins;

import java.util.ArrayList;
import java.util.List;

import at.dominik.coda.ide.CodaIDE;
import at.dominik.coda.ide.gui.workspace.editor.EditorHighlighting;
import javafx.scene.image.Image;

/**
 * @author Dominik Fluch
 *
 * Created on 22.06.2020
 *
 */
public abstract class Plugin {

	private final List<EditorHighlighting> editorHighlightings;
	private PluginManager manager;
	
	/**
	 * 
	 */
	public Plugin() {
		this.editorHighlightings = new ArrayList<EditorHighlighting>();
	}
	
	/**
	 * @return the version of the {@link Plugin}.
	 */
	public int getVersionID() {
		return 0;
	}
	
	/**
	 * @return the name of the version of the plugin.
	 */
	public String getVersion() {
		return "1.0";
	}
	
	/**
	 * @return the author of the plugin.
	 */
	public abstract String getAuthor();
	
	/**
	 * @return the name of the {@link Plugin}.
	 */
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * @return the manager
	 */
	public PluginManager getManager() {
		return manager;
	}
	
	/**
	 * @param manager the manager to set
	 * @return this
	 */
	protected Plugin setManager(PluginManager manager) {
		this.manager = manager;
		return this;
	}
	
	/**
	 * @return the icon of the plugin.
	 */
	public Image getIcon() {
		return new Image(CodaIDE.class.getResourceAsStream("gui/assets/plugins.png"));
	}
	
	/**
	 * @return the editorHighlightings
	 */
	public List<EditorHighlighting> getEditorHighlightings() {
		return editorHighlightings;
	}
	
}
