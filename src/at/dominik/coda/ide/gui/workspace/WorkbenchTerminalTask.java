/**
 * 
 */
package at.dominik.coda.ide.gui.workspace;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import at.dominik.coda.ide.gui.dialogues.ErrorDialogue;
import at.dominik.coda.utils.ProcessListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author Dominik Fluch
 *
 * Created on 09.05.2020
 *
 */
public class WorkbenchTerminalTask {

	private final Workbench workbench;
	private final Process process;
	private final ProcessListener listener;
	private final Queue<ProgramMessage> messageQueue;
	private final Timeline terminalUpdater;
	
	
	/**
	 * @param workbench
	 * @param process
	 */
	protected WorkbenchTerminalTask(Workbench workbench, Process process) {
		this.workbench = workbench;
		this.process = process;
		this.messageQueue = new ConcurrentLinkedDeque<ProgramMessage>();
		
		this.listener = new ProcessListener(process) {

			private String currentError = "";
			private String currentMessage = "";
			
			@Override
			public void onErrorMessage(byte data) {
				
				if(data == 10) {
					WorkbenchTerminalTask.this.getMessageQueue().add(new ProgramMessage(Color.ORANGERED, this.currentError));
					
					this.currentError = "";
				}else this.currentError += (char)data;
			}

			@Override
			public void onInputMessage(byte data) {
				
				if(data == 10) {
					
					WorkbenchTerminalTask.this.getMessageQueue().add(new ProgramMessage(this.currentMessage));
					
					this.currentMessage = "";
				}else this.currentMessage += (char)data;
				
			}
			
			@Override
			public void onExit() {
				WorkbenchTerminalTask.this.stop();
			}
			
		};
		
		this.terminalUpdater = new Timeline(new KeyFrame(Duration.millis(1D), new EventHandler<ActionEvent>() {
			
			
			@Override
			public void handle(ActionEvent event) {
				if(!WorkbenchTerminalTask.this.getMessageQueue().isEmpty())this.work(50);
				else if(!WorkbenchTerminalTask.this.getProcess().isAlive()) {
					this.work(WorkbenchTerminalTask.this.getMessageQueue().size());
					WorkbenchTerminalTask.this.getTerminalUpdater().stop();
					WorkbenchTerminalTask.this.getMessageQueue().add(new ProgramMessage(Color.web("#27ccb1"), "\nProcess exited."));
				}
			}
			
			/**
			 * Works through the given amount of values.
			 */
			private void work(int amount) {
				for(int i = 0; i < amount && !WorkbenchTerminalTask.this.getMessageQueue().isEmpty(); i++) {
					final ProgramMessage message = WorkbenchTerminalTask.this.getMessageQueue().poll();
					
					final Text text = new Text(message.getMessage() + "\n");
					text.setFill(message.getColor());
					
					if(workbench.getTerminal().getChildren().size() > 500)workbench.getTerminal().getChildren().remove(0, 300);
					
					workbench.getTerminal().getChildren().add(workbench.getTerminal().getChildren().isEmpty() ? 0 : workbench.getTerminal().getChildren().size() - 1, text);
				}
				
				if(workbench.getStickyBox().isSelected()) {
					final ScrollPane scrollPane = ((ScrollPane)workbench.getTerminal().getParent().getParent().getParent());
					
					scrollPane.setVvalue(scrollPane.getVmax());
				}
			}
			
		}));
		
		this.getTerminalUpdater().setCycleCount(Timeline.INDEFINITE);
		this.getTerminalUpdater().play();
		
		this.getListener().start();
	}
	
	/**
	 * Stops the task.
	 */
	public void stop() {
		try {
			
			this.getListener().close();
			if(this.getProcess().isAlive())this.getProcess().destroy();
			if(this.getProcess().isAlive())this.getProcess().destroyForcibly();
			
			this.getWorkbench().getTaskState().setVisible(false);
			this.getWorkbench().getTerminateButton().setDisable(true);
			
			Platform.runLater(() -> this.getWorkbench().getTerminal().getChildren().remove(this.getWorkbench().getTerminal().getChildren().size() - 1));
			
			this.getWorkbench().currentTask = null;
		} catch (IOException exception) {
			final ErrorDialogue dialogue = new ErrorDialogue(this.getWorkbench().getGround().getScene().getWindow());
			dialogue.setMessage(exception.getMessage());
			dialogue.show();
		}
		
	}
	
	/**
	 * @return the listener
	 */
	public ProcessListener getListener() {
		return listener;
	}
	
	/**
	 * @return the process
	 */
	public Process getProcess() {
		return process;
	}
	
	/**
	 * @return the workbench
	 */
	public Workbench getWorkbench() {
		return workbench;
	}
	
	/**
	 * @return the terminalUpdater
	 */
	public Timeline getTerminalUpdater() {
		return terminalUpdater;
	}
	
	/**
	 * @return the messageQueue
	 */
	public Queue<ProgramMessage> getMessageQueue() {
		return messageQueue;
	}
	
}
