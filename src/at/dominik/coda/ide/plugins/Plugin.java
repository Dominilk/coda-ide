/**
 * 
 */
package at.dominik.coda.ide.plugins;

import at.dominik.coda.ide.CodaIDE;
import javafx.scene.image.Image;

/**
 * @author Dominik Fluch
 *
 * Created on 22.06.2020
 *
 */
public abstract class Plugin {

	private PluginManager manager;
	
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
	 */
	protected void setManager(PluginManager manager) {
		this.manager = manager;
	}
	
	/**
	 * @return the icon of the plugin.
	 */
	public Image getIcon() {
		return new Image(CodaIDE.class.getResourceAsStream("gui/assets/plugins.png"));
	}
	
}
