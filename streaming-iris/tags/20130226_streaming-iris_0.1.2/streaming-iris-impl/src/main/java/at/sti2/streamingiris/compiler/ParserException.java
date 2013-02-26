package at.sti2.streamingiris.compiler;

/**
 * Indicates a parsing error, i.e. a syntactically incorrect logic program.
 */
public class ParserException extends Exception {
	/**
	 * Constructor.
	 * 
	 * @param message
	 *            The exception message.
	 */
	public ParserException(String message) {
		super(message);
	}

	/** The serial ID */
	private static final long serialVersionUID = 1L;
}
