/**
 * 
 */
package at.dominik.coda.ide.settings;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.dominik.coda.ide.exceptions.SettingsException;

/**
 * @author Dominik Fluch
 *
 * Created on 26.06.2020
 *
 */
public class Settings {

	private Map<String, Object> values;
	private File file;
	
	/**
	 * 
	 */
	protected Settings() {}
	
	/**
	 * @param file
	 */
	protected Settings(File file) {
		this.file = file;
	}
	
	/**
	 * Updates the settings.
	 */
	private void update() {
		try {
			SettingsManager.OBJECT_MAPPER.writeValue(this.getFile(), this);
		} catch (IOException exception) {
			throw new SettingsException(exception);
		}
	}
	
	/**
	 * Sets the given value to the given key.
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
		this.getValues().put(key, value);
		this.update();
	}
	
	/**
	 * @param <T>
	 * @param key
	 * @param type
	 * @return the value at the given key and casts it to the given type.
	 */
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public <T> T get(String key, Class<T> type) {
		return (T) this.getValues().get(key);
	}
	
	/**
	 * @param key
	 * @return the value at the given key.
	 */
	@JsonIgnore
	public Object get(String key) {
		return this.getValues().keySet();
	}
	
	/**
	 * @deprecated Should use {@link Settings#set(String, Object)} method for setting values.
	 * @return the values
	 */
	@Deprecated
	public Map<String, Object> getValues() {
		return values;
	}
	
	/**
	 * @param values the values to set
	 */
	protected void setValues(Map<String, Object> values) {
		this.values = values;
	}
	
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * @param file the file to set
	 * @return this
	 */
	public Settings setFile(File file) {
		this.file = file;
		return this;
	}
	
}
