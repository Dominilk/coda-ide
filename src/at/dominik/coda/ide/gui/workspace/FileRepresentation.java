/**
 * 
 */
package at.dominik.coda.ide.gui.workspace;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * @author Dominik Fluch
 *
 * Created on 02.05.2020
 *
 */
public class FileRepresentation {

	private static final JFileChooser FILE_CHOOSER = new JFileChooser();
	private static final String OPERATING_SYSTEM = System.getProperty("os.name");
	
	private final Image icon;
	private File file;
	
	/**
	 * @param file
	 */
	public FileRepresentation(File file) {
		this.file = file;
		
		if(file != null) {
			final Icon icon = (FileRepresentation.OPERATING_SYSTEM.toLowerCase().startsWith("win") ? FileSystemView.getFileSystemView().getSystemIcon(this.getFile()) : FileRepresentation.FILE_CHOOSER.getUI().getFileView(FileRepresentation.FILE_CHOOSER).getIcon(this.getFile()));
			final BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
			bufferedImage.flush();
			this.icon = SwingFXUtils.toFXImage(bufferedImage, null);
		}else this.icon = null;
	}
	
	/**
	 * Initializes the representation with null as file.
	 */
	protected FileRepresentation() {
		this(null);
	}
	
	@Override
	public String toString() {
		return this.getFile() != null ? this.getFile().getName() : "null";
	}
	
	/**
	 * @return the icon
	 */
	public Image getIcon() {
		return icon;
	}
	
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
}
