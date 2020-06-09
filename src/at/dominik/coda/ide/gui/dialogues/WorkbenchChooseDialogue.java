/**
 * 
 */
package at.dominik.coda.ide.gui.dialogues;

import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * @author Dominik Fluch
 *
 * Created on 08.06.2020
 *
 */
public class WorkbenchChooseDialogue extends Dialogue {

	private File chosenWorkspace;
	
	/**
	 * @param owner
	 */
	public WorkbenchChooseDialogue(Window owner) {
		super(owner);
		this.setResizable(false);
		
		this.setOnCloseRequest((event) -> {
			
			this.hide();
			System.exit(0);
			
		});
		
		this.getOpenButton().setOnAction((event) -> {
			
			final File workspace = new File(this.getWorkbenchPath().getText());
			
			if(workspace.exists() && workspace.isDirectory()) {
				this.chosenWorkspace = workspace;
				this.hide();
			}else{
				final ErrorDialogue dialogue = new ErrorDialogue(this);
				
				dialogue.setMessage("Invalid workspace location.");
				
				dialogue.show();
			}
			
			
		});
		
		this.getBrowseButton().setOnAction((event) -> {
			
			final FileChooser chooser = new FileChooser();
			
			final File file = chooser.showOpenDialog(this);
			
			if(file != null)this.getWorkbenchPath().setText(file.getAbsolutePath());
			
		});
	}

	/**
	 * @return the project path {@link TextField}.
	 */
	public TextField getWorkbenchPath() {
		return (TextField) this.getScene().lookup("#path");
	}
	
	/**
	 * @return the open button.
	 */
	public Button getOpenButton() {
		return (Button) this.getScene().lookup("#open");
	}

	/**
	 * @return the browse button.
	 */
	public Button getBrowseButton() {
		return (Button) this.getScene().lookup("#browse");
	}
	
	/**
	 * @return the chosenWorkspace
	 */
	public File getChosenWorkspace() {
		return chosenWorkspace;
	}
	
}
