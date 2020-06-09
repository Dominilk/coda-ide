/**
 * 
 */
package at.dominik.coda.ide.exceptions;

/**
 * @author Dominik Fluch
 *
 * Created on 05.05.2020
 *
 */
public class UnhandledException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param cause
	 */
	public UnhandledException(Throwable cause) {
		super(cause);
	}
	
}
