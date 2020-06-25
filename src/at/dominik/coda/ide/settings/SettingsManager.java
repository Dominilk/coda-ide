/**
 * 
 */
package at.dominik.coda.ide.settings;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.dominik.coda.ide.CodaIDE;
import at.dominik.coda.ide.exceptions.SettingsException;

/**
 * @author Dominik Fluch
 *
 * Created on 26.06.2020
 *
 */
public class SettingsManager {
	
	protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final CodaIDE codaIDE;
	private final File settingsFolder;
	
	/**
	 * @param codaIDE
	 */
	public SettingsManager(CodaIDE codaIDE) {
		this.codaIDE = codaIDE;
		this.settingsFolder = new File(codaIDE.getHomeFolder(), "settings");
		
		if(!this.getSettingsFolder().exists())this.getSettingsFolder().mkdirs();
	}
	
	/**
	 * @param name
	 * @return the {@link Settings} with the given name.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Settings getSettings(String name) {
		final File file = new File(this.getSettingsFolder(), name + ".json");
		
		if(file.exists()) {
			try {
				return SettingsManager.OBJECT_MAPPER.readValue(file, Settings.class);
			} catch (IOException exception) { throw new SettingsException(exception); }
		}else return new Settings(file);
	}
	
	/**
	 * @return the settingsFolder
	 */
	public File getSettingsFolder() {
		return settingsFolder;
	}
	
	/**
	 * @return the codaIDE
	 */
	public CodaIDE getCodaIDE() {
		return codaIDE;
	}
	
}
