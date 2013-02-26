package at.sti2.streamingiris.rules;

import java.util.List;

import at.sti2.streamingiris.api.basics.IAtom;
import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.builtins.EqualBuiltin;

/**
 * An utility class for rule head equality.
 * 
 * @author Adrian Marte
 */
public class RuleHeadEquality {

	/**
	 * Checks if rule head equality appears in the head of the specified rule,
	 * e.g. ?X = ?Y :- p(?X, ?Y), q(?Y, ?X).
	 * 
	 * @param rule
	 *            The rule to check for occurrence of rule head equality.
	 * @return <code>true</code> if the given rule has rule head equality,
	 *         <code>false</code> otherwise.
	 */
	public static boolean hasRuleHeadEquality(IRule rule) {
		List<ILiteral> head = rule.getHead();

		for (ILiteral literal : head) {
			if (hasRuleHeadEquality(literal)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the specified literal represents rule head equality, e.g. ?X =
	 * ?Y.
	 * 
	 * @param rule
	 *            The literal to check for occurrence of the rule head equality
	 *            predicate.
	 * @return <code>true</code> if the given literal represents rule head
	 *         equality predicate, <code>false</code> otherwise.
	 */
	private static boolean hasRuleHeadEquality(ILiteral literal) {
		IAtom atom = literal.getAtom();

		return atom instanceof EqualBuiltin;
	}

}
