package at.sti2.streamingiris;

/**
 * Indicates that evaluation could not continue, because of an unsafe rule.
 */
public class RuleUnsafeException extends EvaluationException {
	/**
	 * Constructor.
	 * 
	 * @param message
	 *            The exception message.
	 */
	public RuleUnsafeException(String message) {
		super(message);
	}

	/** The serial ID */
	private static final long serialVersionUID = 1L;
}
