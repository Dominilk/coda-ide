/**
 * 
 */
package at.dominik.coda.ide.gui.workspace;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.dominik.coda.ide.gui.dialogues.ErrorDialogue;


/**
 * @author Dominik Fluch
 *
 * Created on 08.06.2020
 *
 */
public class WorkbenchSettings {
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private Map<String, Object> rawSettings;
	protected File lastFile;
	
	/**
	 * 
	 */
	public WorkbenchSettings() {
		this.rawSettings = new HashMap<String, Object>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		
			@Override
			public Object put(String key, Object value) {
				final Object result = super.put(key, value);
				
				WorkbenchSettings.this.save();
				
				return result;
			}
			
		};
	}
	
	/**
	 * Saves the settings to the given file.
	 * @param file
	 */
	public void save(File file) {
		
		if(file != null)try {
			Files.write(file.toPath(), WorkbenchSettings.OBJECT_MAPPER.writeValueAsBytes(this), StandardOpenOption.CREATE);
			return;
		} catch (IOException exception) {}
		
		final ErrorDialogue dialogue = new ErrorDialogue(null);
		dialogue.setMessage("Could not save workbench settings.");
		dialogue.show();
	}
	
	/**
	 * Saves the settings to the file that was used at the last save.
	 */
	public void save() {
		this.save(this.lastFile);
	}
	
	/**
	 * @return the raw settings.
	 */
	public Map<String, Object> getRawSettings() {
		return rawSettings;
	}
	
	/**
	 * @param rawSettings the rawSettings to set
	 */
	protected void setRawSettings(Map<String, Object> rawSettings) {
		this.rawSettings = rawSettings;
	}
	
}
