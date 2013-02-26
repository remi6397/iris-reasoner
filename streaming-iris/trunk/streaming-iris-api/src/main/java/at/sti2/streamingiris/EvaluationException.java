package at.sti2.streamingiris;

/**
 * Indicates any problem that halts the evaluation process. Subclasses are
 * expected for such things as: unsafe rule detected, non-stratified negation,
 * etc
 */
public class EvaluationException extends Exception {
	/**
	 * Constructor.
	 * 
	 * @param message
	 *            The exception message.
	 */
	public EvaluationException(String message) {
		super(message);
	}

	/** The serial ID */
	private static final long serialVersionUID = 1L;
}
