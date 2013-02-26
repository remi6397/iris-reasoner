package at.sti2.streamingiris;

/**
 * Indicates that evaluation could not continue, because of the logic program is
 * not stratified.
 */
public class ProgramNotStratifiedException extends EvaluationException {
	/**
	 * Constructor.
	 * 
	 * @param message
	 *            The exception message.
	 */
	public ProgramNotStratifiedException(String message) {
		super(message);
	}

	/** The serial ID */
	private static final long serialVersionUID = 1L;
}
