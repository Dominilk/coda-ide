/**
 * 
 */
package at.dominik.coda.ide.gui.workspace;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import at.dominik.coda.CodaInteractor;
import at.dominik.coda.ide.gui.dialogues.ErrorDialogue;
import at.dominik.coda.ide.gui.dialogues.FileCreationDialogue;
import at.dominik.coda.ide.gui.dialogues.RunDialogue;
import at.dominik.coda.ide.gui.workspace.editor.CodaHighlighting;
import at.dominik.coda.ide.gui.workspace.editor.Editor;
import at.dominik.coda.ide.gui.workspace.editor.EditorHighlighting;
import at.dominik.coda.ide.gui.workspace.editor.JavaHighlighting;
import at.dominik.coda.ide.gui.workspace.editor.StandardHighlighting;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * @author Dominik Fluch
 *
 * Created on 01.05.2020
 *
 */
public class Workbench {

	private final BorderPane ground;
	private final List<EditorHighlighting> knownHighlightings;
	private final Editor editor;
	private final CodaInteractor coda;
	private final WorkbenchSettings settings;
	private File projectFolder;
	protected WorkbenchTerminalTask currentTask;
	
	
	/**
	 * @param projectFolder
	 * @throws IOException 
	 */
	public Workbench(File projectFolder) throws IOException {
		this.coda = new CodaInteractor();
		this.ground = FXMLLoader.load(this.getClass().getResource("Workbench.fxml"));
		this.knownHighlightings = new ArrayList<EditorHighlighting>();
		this.editor = new Editor();
		this.currentTask = null;
		this.settings = new WorkbenchSettings();
		
		this.getSettings().lastFile = new File(projectFolder, ".settings");
		
		this.setProjectFolder(projectFolder);
		
		this.getKnownHighlightings().add(new StandardHighlighting());
		this.getKnownHighlightings().add(new JavaHighlighting());
		this.getKnownHighlightings().add(new CodaHighlighting());
		
		this.getMainContent().addEventFilter(KeyEvent.KEY_PRESSED, (keyEvent) -> {
			
			if(keyEvent.isShortcutDown() && keyEvent.getCode() == KeyCode.S)this.getSave().fire();
			
		});
		
		this.getEditor().textProperty().addListener((observable, oldValue, newValue) -> this.getSave().setDisable(false));
		
		this.getTerminateButton().setOnAction((event) -> {
			
			if(this.getCurrentTask() != null)this.getCurrentTask().stop();
			
		});
		
		
		final TreeView<FileRepresentation> packageExplorer = this.getPackageExplorer();
		
		packageExplorer.setOnMouseClicked((mouseEvent) -> {
			
			if(mouseEvent.getClickCount() == 2) {
				final TreeItem<FileRepresentation> treeItem = packageExplorer.getSelectionModel().getSelectedItem();
		        	
				if(treeItem == null || treeItem.getValue() == null || treeItem.getValue().getFile().isDirectory())return;
		        
				this.openFile(treeItem.getValue().getFile());
			}
		    
		});
		
		final ContextMenu contextMenu = new ContextMenu();
		final MenuItem create = new MenuItem("Create file/folder");
		final MenuItem refresh = new MenuItem("Refresh");
		final MenuItem reveal = new MenuItem("Reveal in explorer");
		final MenuItem run = new MenuItem("Run");
		final MenuItem delete = new MenuItem("Delete");
		
		create.setOnAction((event) -> this.getFileCreate().fire());
		run.setOnAction((event) -> this.getRun().fire());
		refresh.setOnAction((event) -> {
			
			final TreeItem<FileRepresentation> treeItem = packageExplorer.getSelectionModel().getSelectedItem();
        	
			if(treeItem != null)this.update((treeItem.getValue().getFile().isDirectory() ? treeItem : treeItem.getParent()));
			
		});
		reveal.setOnAction((event) -> {
			
			final TreeItem<FileRepresentation> treeItem = packageExplorer.getSelectionModel().getSelectedItem();
        	
			if(treeItem != null) {
				try {
					// not using browseFileDirectory because it is since Java 9.
					Desktop.getDesktop().browse(treeItem.getValue().getFile().isDirectory() ? treeItem.getValue().getFile().toURI() : treeItem.getValue().getFile().getParentFile().toURI());
				}catch(IOException exception) {
					final ErrorDialogue dialogue = new ErrorDialogue(this.getGround().getScene().getWindow());
					dialogue.setMessage(exception.getMessage());
					dialogue.show();
				}
			}
			
		});
		delete.setOnAction((event) -> {
			
			final TreeItem<FileRepresentation> treeItem = packageExplorer.getSelectionModel().getSelectedItem();
        	
			if(treeItem != null) {
				try {
					Files.walk(treeItem.getValue().getFile().toPath())
					  .sorted(Comparator.reverseOrder())
					  .map(Path::toFile)
					  .forEach(File::delete);
					
					final TreeItem<FileRepresentation> createdParent = treeItem.getParent();
					
					if(createdParent != null)this.update(createdParent);
				}catch(IOException exception) {
					final ErrorDialogue dialogue = new ErrorDialogue(this.getGround().getScene().getWindow());
					dialogue.setMessage(exception.getMessage());
					dialogue.show();
				}
			}
			
		});
		
		contextMenu.getItems().addAll(create, refresh, reveal, run, delete);
		
		packageExplorer.setContextMenu(contextMenu);
		
		this.getFileCreate().setOnAction((actionEvent) -> {
			final FileCreationDialogue dialogue = new FileCreationDialogue(this.getGround().getScene().getWindow());
			
			final TreeItem<FileRepresentation> treeItem = packageExplorer.getSelectionModel().getSelectedItem();
        	
			if(treeItem != null && treeItem.getValue() != null) {
				dialogue.setCurrentPath(treeItem.getValue().getFile().getPath());
			}
	        
			dialogue.showAndWait();
			
			if(dialogue.getLatestCreation() != null) {
				final TreeItem<FileRepresentation> createdParent = this.lookupTreeItem(dialogue.getLatestCreation().getParentFile());
				
				if(createdParent != null)this.update(createdParent);
				
			}
		});
		
		/*
		 * TODO:
		 * Multiple code tabs.
		 * Parse code line on error in console.
		 */
		
		this.getRun().setOnAction((actionEvent) -> {
			final RunDialogue dialogue = new RunDialogue(this);
			
			final TreeItem<FileRepresentation> treeItem = packageExplorer.getSelectionModel().getSelectedItem();
        	
			if(treeItem != null && treeItem.getValue() != null) {
				if(treeItem.getValue().getFile().isDirectory())dialogue.setProjectPath(treeItem.getValue().getFile().getPath());
				else dialogue.setFilePath(treeItem.getValue().getFile().getPath());
			}
			
			dialogue.show();
		});
		
	}
	
	/**
	 * Runs the file at the given path.
	 * @param file
	 */
	public void runFile(File file, File... dependencies) {
		
		if(file.isDirectory()) {
			final File exportedFiles = new File(this.getProjectFolder(), ".exported");
			final File exported = new File(file.getName() + ".ca");
			
			if(!exportedFiles.exists())exportedFiles.mkdirs();
			
			//this.listen(this.getCoda().export(file, FileType.CA, dependencies));
			
			final File target = new File(exportedFiles, exported.getName());
			
			try {
				target.createNewFile();
				Files.move(exported.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
				this.runFile(target);
			} catch (IOException exception) {
				final ErrorDialogue dialogue = new ErrorDialogue(this.getGround().getScene().getWindow());
				dialogue.setMessage(exception.getMessage());
				dialogue.show();
			}
			
		}else{
			this.getTaskState().setProgress(ProgressBar.INDETERMINATE_PROGRESS);
			this.getTaskState().setVisible(true);
			
			this.getTerminal().getChildren().clear();
			

			final TextField prompt = new TextField();
			prompt.setPromptText(">");
			
			prompt.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
				
				if(event.getCode() == KeyCode.ENTER) {
					
					event.consume();
					
					final Text input = new Text(prompt.getText() + "\n");
					input.setFill(Color.GREY);
					
					this.getTerminal().getChildren().add(this.getTerminal().getChildren().size() - 1, input);
					
					try {
						this.getCurrentTask().getProcess().getOutputStream().write(input.getText().getBytes());
						this.getCurrentTask().getProcess().getOutputStream().flush();
					}catch(IOException exception) {
						final ErrorDialogue dialogue = new ErrorDialogue(this.getGround().getScene().getWindow());
						dialogue.setMessage(exception.getMessage());
						dialogue.show();
					}
					
					prompt.setText("");
					
					final ScrollPane scrollPane = ((ScrollPane)this.getTerminal().getParent().getParent().getParent());
					
					scrollPane.setVvalue(scrollPane.getVmax());
				}
				
			});
			
			prompt.setStyle("-fx-background-color: white; -fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color: lightgrey;");
			
			prompt.prefWidthProperty().bind(this.getTerminal().widthProperty().subtract(10D));
			this.getTerminal().getChildren().add(prompt);
			
			this.getTerminateButton().setDisable(false);
			
			this.currentTask = new WorkbenchTerminalTask(this, this.getCoda().run(file));
		}
	}
	
	/**
	 * @return true if a task can be run.
	 */
	public boolean canRun() {
		return this.getCurrentTask() == null;
	}
	
	/**
	 * @param file
	 * @return the {@link TreeItem} having the given file or null.
	 */
	public TreeItem<FileRepresentation> lookupTreeItem(File file) {
		return this.lookupTreeItem(this.getPackageExplorer().getRoot(), file);
	}
	
	/**
	 * @param parent
	 * @param file
	 * @return the {@link TreeItem} having the given file or null.
	 */
	private TreeItem<FileRepresentation> lookupTreeItem(TreeItem<FileRepresentation> parent, File file) {
		for(TreeItem<FileRepresentation> child : parent.getChildren()) if(child.getValue().getFile().equals(file))return child;
		else {
			final TreeItem<FileRepresentation> result = this.lookupTreeItem(child, file);
			if(result != null)return result;
		}
		return null;
	}
	
	/**
	 * Opens the given file.
	 * @param file
	 */
	public void openFile(File file) {
		this.getEditor().setHighlighting(this.getHightlighting(file.getName().contains(".") && !file.getName().endsWith(".") ? file.getName().substring(file.getName().lastIndexOf('.') + 1) : file.getName()));
		
		if(this.getMainContent().getItems().get(1) instanceof Editor) {
			this.getSave().fire();
		}else{
			final Double divider = this.getMainContent().getDividerPositions()[0];
			this.getMainContent().getItems().remove(1);
			this.getMainContent().getItems().add(this.getEditor());
			this.getMainContent().setDividerPosition(0, divider);
		}

		this.getSave().setOnAction((event) -> {
			try {
				Files.write(file.toPath(), this.getEditor().getText().getBytes());
				this.getSave().setDisable(true);
			} catch (IOException exception) {
				final ErrorDialogue dialogue = new ErrorDialogue(this.getGround().getScene().getWindow());
				dialogue.setMessage(exception.getMessage());
				dialogue.show();
			}
		});
		
		try {
			this.getEditor().replaceText(new String(Files.readAllBytes(file.toPath())));
			this.getEditor().updateHighlighting();
			this.getSave().setDisable(true);
		} catch (IOException exception) {
			this.getEditor().replaceText(exception.getClass().getName() + ": " + exception.getMessage());
		}
	}
	
	/**
	 * @return the projectFolder
	 */
	public File getProjectFolder() {
		return projectFolder;
	}
	
	/**
	 * @param projectFolder the projectFolder to set
	 */
	public void setProjectFolder(File projectFolder) {
		this.projectFolder = projectFolder;
		
		final TreeView<FileRepresentation> packageExplorer = this.getPackageExplorer();
		
		packageExplorer.setShowRoot(false);
		
		final TreeItem<FileRepresentation> root = new TreeItem<FileRepresentation>(new FileRepresentation(projectFolder));
		
		packageExplorer.setRoot(root);
		
		for(File file : projectFolder.listFiles())this.addFile(root, file);
		
	}
	
	/**
	 * Adds the given file to the given item.
	 * @param item.
	 * @param file
	 */
	private void addFile(TreeItem<FileRepresentation> item, File file) {
		
		if(file.isHidden())return;
		
		final FileRepresentation representation = new FileRepresentation(file);
		
		final TreeItem<FileRepresentation> element = representation.createItem();
		
		item.getChildren().add(element);
		
		this.update(element);
		
	}
	
	/**
	 * Updates the given item.
	 * @param item
	 */
	private void update(TreeItem<FileRepresentation> item) {
		final Boolean expanded = item.isExpanded();
		item.getChildren().clear();
		
		if(item.getValue().getFile().isDirectory()) for(File file : item.getValue().getFile().listFiles())this.addFile(item, file);
		
		item.setExpanded(expanded);
	}
	
	/**
	 * @return the package explorer.
	 */
	@SuppressWarnings("unchecked")
	public TreeView<FileRepresentation> getPackageExplorer() {
		return (TreeView<FileRepresentation>) this.getMainContent().getItems().get(0);
	}
	
	/**
	 * @return the file create button.
	 */
	public Button getFileCreate() {
		return (Button) this.getGround().lookup("#create");
	}
	
	/**
	 * @return the file create button.
	 */
	public Button getSave() {
		return (Button) this.getGround().lookup("#save");
	}
	
	/**
	 * @return the run button.
	 */
	public Button getRun() {
		return (Button) this.getGround().lookup("#run");
	}
	
	/**
	 * @return the main content.
	 */
	public SplitPane getMainContent() {
		return ((SplitPane)((SplitPane)((SplitPane)this.getGround().getCenter()).getItems().get(0)).getItems().get(0));
	}
	
	/**
	 * @return the terminal.
	 */
	public TextFlow getTerminal() {
		return (TextFlow) ((ScrollPane)((VBox)((SplitPane)((SplitPane)this.getGround().getCenter()).getItems().get(0)).getItems().get(1)).getChildren().get(1)).getContent();
	}
	
	/**
	 * @return the terminate button.
	 */
	public Button getTerminateButton() {
		return (Button) ((HBox)((VBox)((SplitPane)((SplitPane)this.getGround().getCenter()).getItems().get(0)).getItems().get(1)).getChildren().get(0)).lookup("#terminate");
	}
	
	/**
	 * @return the sticky box.
	 */
	public CheckBox getStickyBox() {
		return (CheckBox) ((HBox)((VBox)((SplitPane)((SplitPane)this.getGround().getCenter()).getItems().get(0)).getItems().get(1)).getChildren().get(0)).lookup("#sticky");
	}
	
	
	/**
	 * @return the task state.
	 */
	public ProgressIndicator getTaskState() {
		return (ProgressIndicator) ((VBox)((SplitPane)((SplitPane)this.getGround().getCenter()).getItems().get(0)).getItems().get(1)).lookup("#state");
	}
	
	/**
	 * @return the editor
	 */
	public Editor getEditor() {
		return editor;
	}
	
	/**
	 * @return the ground
	 */
	public BorderPane getGround() {
		return ground;
	}

	/**
	 * @return a newly created Workbench scene.
	 */
	public Scene createScene() {
		return new Scene(this.getGround());
	}
	
	/**
	 * @param fileType
	 * @return the highlighting suitable for the given file type.
	 */
	public EditorHighlighting getHightlighting(String fileType) {
		for(EditorHighlighting highlighting : this.getKnownHighlightings()) {
			for(String type : highlighting.getSupportedFileTypes()) if(type.toUpperCase().equals(fileType.toUpperCase()))return highlighting;
		}
		return !this.getKnownHighlightings().isEmpty() ? this.getKnownHighlightings().get(0) : null;
	}
	
	/**
	 * @return the knownHighlightings
	 */
	public List<EditorHighlighting> getKnownHighlightings() {
		return knownHighlightings;
	}
	
	/**
	 * @return the coda
	 */
	public CodaInteractor getCoda() {
		return coda;
	}
	
	/**
	 * @return the settings
	 */
	public WorkbenchSettings getSettings() {
		return settings;
	}
	
	/**
	 * @return the currentTask
	 */
	public WorkbenchTerminalTask getCurrentTask() {
		return currentTask;
	}
	
}
