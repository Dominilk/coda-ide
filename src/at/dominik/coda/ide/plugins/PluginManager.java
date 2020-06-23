/**
 * 
 */
package at.dominik.coda.ide.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import at.dominik.coda.ide.CodaIDE;

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
		this.plugins = new ArrayList<Plugin>();
		this.codaIDE = codaIDE;
	}
	
	/**
	 * Loads all plugins in the given folder.
	 * @param folder
	 */
	public void loadPlugins(File folder) {
		
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
