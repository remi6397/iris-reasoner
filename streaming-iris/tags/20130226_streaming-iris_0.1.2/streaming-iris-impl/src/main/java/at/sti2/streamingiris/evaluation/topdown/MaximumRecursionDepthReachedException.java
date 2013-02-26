package at.sti2.streamingiris.evaluation.topdown;

import at.sti2.streamingiris.EvaluationException;

/**
 * Exception to be thrown at a certain nesting level. Used by top-down
 * evaluation strategies (SLD, SLDNF) to break out of infinite/very big loops.
 * 
 * @author gigi
 * 
 */
public class MaximumRecursionDepthReachedException extends EvaluationException {

	public MaximumRecursionDepthReachedException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 6986765471629696124L;

}
