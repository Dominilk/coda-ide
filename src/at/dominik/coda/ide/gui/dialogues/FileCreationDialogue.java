/**
 * 
 */
package at.dominik.coda.ide.gui.dialogues;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Window;

/**
 * @author Dominik Fluch
 *
 * Created on 05.05.2020
 *
 */
public class FileCreationDialogue extends Dialogue {

	private File latestCreation;
	
	/**
	 * @param owner
	 */
	public FileCreationDialogue(Window owner) {
		super(owner);
		this.setResizable(false);
		
		this.getCreateButton().setOnMousePressed((mouseEvent) -> {
			
			final File file = new File(this.getCurrentPath());
			
			try {
				
				this.getCurrentPathField().setStyle("-fx-border-color: lightgrey;");
				
				if(file.getName().contains(".")) {
					if(mouseEvent.isPrimaryButtonDown()) {
						if(!file.getParentFile().exists())file.getParentFile().mkdirs();
						file.createNewFile();
					}else if(!file.mkdirs())throw new IOException("Could not create directory.");
				}else if(mouseEvent.isPrimaryButtonDown()) {
					if(!file.mkdirs())throw new IOException("Could not create directory.");
				}else{
					if(!file.getParentFile().exists())file.getParentFile().mkdirs();
					file.createNewFile();
				}
				
				this.setLatestCreation(file);
				
				this.close();
			}catch(IOException exception) {
				this.getCurrentPathField().setStyle("-fx-border-color: red;");
				
				final ErrorDialogue dialogue = new ErrorDialogue(this);
				
				dialogue.setMessage(exception.getMessage());
				
				dialogue.show();
			}
		});
	}
	
	/**
	 * @return the latestCreation
	 */
	public File getLatestCreation() {
		return latestCreation;
	}
	
	/**
	 * @param latestCreation the latestCreation to set
	 */
	public void setLatestCreation(File latestCreation) {
		this.latestCreation = latestCreation;
	}
	
	/**
	 * Sets the current path.
	 * @param path
	 */
	public void setCurrentPath(String path) {
		this.getCurrentPathField().setText(path);
		this.getCurrentPathField().deselect();
	}
	
	/**
	 * @return the current path.
	 */
	public String getCurrentPath() {
		return this.getCurrentPathField().getText();
	}
	
	/**
	 * @return the current path {@link TextField}.
	 */
	public TextField getCurrentPathField() {
		return (TextField) this.getScene().lookup("#path");
	}

	/**
	 * @return the create button.
	 */
	public Button getCreateButton() {
		return (Button) this.getScene().lookup("#create");
	}
	
	@Override
	public String getName() {
		return "File Creation";
	}
	
}
