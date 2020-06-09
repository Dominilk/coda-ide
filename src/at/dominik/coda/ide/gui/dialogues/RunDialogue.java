/**
 * 
 */
package at.dominik.coda.ide.gui.dialogues;

import java.io.File;

import at.dominik.coda.ide.gui.workspace.Workbench;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * @author Dominik Fluch
 *
 * Created on 08.05.2020
 *
 */
public class RunDialogue extends Dialogue {

	/**
	 * @param workbench
	 */
	public RunDialogue(Workbench workbench) {
		super(workbench.getGround().getScene().getWindow());
		
		this.getRunFileButton().setOnAction((event) -> {
			
			if(workbench.canRun()) {
				workbench.runFile(new File(this.getFilePath()));
				
				this.close();
			}else{
				final ErrorDialogue dialogue = new ErrorDialogue(this);
				
				dialogue.setMessage("There is already a task running.");
				
				dialogue.show();
			}
			
		});
		
		this.setResizable(false);
	}
	
	@Override
	public String getName() {
		return "Run";
	}
	
	/**
	 * Sets the single file run path.
	 * @param path
	 */
	public void setFilePath(String path) {
		this.getFilePathField().setText(path);
	}
	
	/**
	 * @return the single file run path.
	 */
	public String getFilePath() {
		return this.getFilePathField().getText();
	}
	
	/**
	 * @return the file path {@link TextField}.
	 */
	public TextField getFilePathField() {
		return (TextField) this.getScene().lookup("#file");
	}

	/**
	 * Sets the project run path.
	 * @param path
	 */
	public void setProjectPath(String path) {
		this.getProjectPathField().setText(path);
	}
	
	/**
	 * @return the single file run path.
	 */
	public String getProjectPath() {
		return this.getProjectPathField().getText();
	}
	
	/**
	 * @return the project path {@link TextField}.
	 */
	public TextField getProjectPathField() {
		return (TextField) this.getScene().lookup("#project");
	}
	
	/**
	 * @return the run file button.
	 */
	public Button getRunFileButton() {
		return (Button) this.getScene().lookup("#run");
	}

}
