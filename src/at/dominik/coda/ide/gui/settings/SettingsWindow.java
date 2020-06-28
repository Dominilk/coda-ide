/**
 * 
 */
package at.dominik.coda.ide.gui.settings;

import java.io.IOException;

import org.controlsfx.control.ToggleSwitch;

import at.dominik.coda.ide.gui.dialogues.ErrorDialogue;
import at.dominik.coda.ide.gui.workspace.Workbench;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Dominik Fluch
 *
 * Created on 12.06.2020
 *
 */
public class SettingsWindow extends Stage {

	private BorderPane ground;
	private VBox general;
	private VBox plugins;
	
	/**
	 * @param workbench
	 */
	public SettingsWindow(Workbench workbench) {
		this.initOwner(workbench.getGround().getScene().getWindow());
		this.initStyle(StageStyle.UNDECORATED);
		this.setTitle("Settings");
		
		try {
			this.setScene(new Scene(this.ground = FXMLLoader.load(this.getClass().getResource("Settings.fxml"))));
			
			this.general = (VBox) this.getGround().getCenter();
			this.plugins = FXMLLoader.load(this.getClass().getResource("categories/Plugins.fxml"));
		} catch (IOException exception) {
			final ErrorDialogue dialogue = new ErrorDialogue(workbench.getGround().getScene().getWindow());
			dialogue.setMessage(exception.getMessage());
			dialogue.show();
		}
		
		double[] mouse = new double[2];
		
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
			mouse[0] = event.getSceneX();
			mouse[1] = event.getSceneY();
		});
		
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, (event) -> {
			this.setX(event.getScreenX() - mouse[0]);
			this.setY(event.getScreenY() - mouse[1]);
		});
		
		this.getCloseButton().setOnAction((event) -> this.hide());
		
		for(Node node : this.getSideBar().getChildren()) {
			if(node instanceof Button) {
				final Button button = (Button)node;
				
				button.setOnAction((event) -> {
					
					for(Node node2 : this.getSideBar().getChildren()) node2.getStyleClass().remove("selected");
					
					switch(button.getId()) {
					
					case "general":
						this.getGround().setCenter(this.getGeneral());
						break;
					case "plugins":
						this.getGround().setCenter(this.getPlugins());
						break;
					}
					
					button.getStyleClass().add("selected");
				});
			}
		}
		
		this.centerOnScreen();
	}
	
	/**
	 * @return the side bar.
	 */
	public VBox getSideBar() {
		return (VBox) this.getGround().lookup("#sidebar");
	}
	
	/**
	 * @return the close button.
	 */
	public Button getCloseButton() {
		return (Button) this.getScene().lookup("#close");
	}
	
	/**
	 * @return the plugins button.
	 */
	public Button getPluginsButton() {
		return (Button) this.getScene().lookup("#plugins");
	}
	
	/**
	 * @return the general button.
	 */
	public Button getGeneralButton() {
		return (Button) this.getScene().lookup("#general");
	}
	
	/**
	 * @return the ground
	 */
	public BorderPane getGround() {
		return ground;
	}
	
	/**
	 * @return the general
	 */
	public VBox getGeneral() {
		return general;
	}
	
	/**
	 * @return the plugins
	 */
	public VBox getPlugins() {
		return plugins;
	}
	
	/**
	 * @return the stickyTerminal
	 */
	public boolean isTerminalSticky() {
		return ((ToggleSwitch)this.getScene().lookup("#stickyTerm")).isSelected();
	}
	
}
