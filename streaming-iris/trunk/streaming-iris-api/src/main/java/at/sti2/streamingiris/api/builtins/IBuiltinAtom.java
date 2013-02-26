package at.sti2.streamingiris.api.builtins;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IAtom;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.utils.equivalence.IEquivalentTerms;

/**
 * <p>
 * Defines a Builtin.
 * </p>
 * <p>
 * $Id: IBuiltInAtom.java,v 1.11 2007-10-19 13:27:39 bazbishop237 Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.11 $
 */
public abstract interface IBuiltinAtom extends IAtom {

	/**
	 * <p>
	 * Runs the evaluation.
	 * </p>
	 * <p>
	 * This method takes as input a tuple for which it should run the
	 * evaluation. The tuple must contain the substitutes for the variables of
	 * this built-in at the corresponding position. Substitutes where this
	 * built-in already has a constant might be ignored.
	 * </p>
	 * <p>
	 * The returned tuple contains the calculated substitutions for the
	 * remaining variables (after replacing the variables with the passed in
	 * constants) in the built-in in the order their variables appear in the
	 * built-in. If there are no variables left to calculate the built-in will
	 * check the correctness of the terms and return a tuple containing all
	 * constants if it is correct, otherwise <code>null</code>. For example if
	 * you evaluate <code>4 + X = 9</code> you would get back
	 * <code>&lt;5&gt;</code>. The only difference are the binary built-ins: For
	 * a binary built-in if you try to evaluate <code>3 &lt; 4</code> you will
	 * get back <code>&lt;3, 4&gt;</code>, but if you try to evaluate
	 * <code>3 &lt; 2</code> you will get back <code>null</code>.
	 * </p>
	 * 
	 * @param t
	 *            the substitutes for the variables of the builtin
	 * @return the calculated constants or <code>null</code> if the built-in
	 *         isn't evaluable
	 * @throws EvaluationException
	 * @throws IllegalArgumentException
	 *             if the built-in couldn't be evaluated
	 * @throws NullPointerException
	 *             if the collection was <code>null</code>
	 */
	public ITuple evaluate(final ITuple t) throws EvaluationException;

	/**
	 * The maximum number of unknown variables allowed such that the predicate
	 * can still be evaluated.
	 * 
	 * @return The maximum number of unknown variables.
	 */
	public int maxUnknownVariables();

	/**
	 * Sets the equivalence classes, which can be used for the evaluation of the
	 * built-in.
	 * 
	 * @param equivalenceClasses
	 *            The equivalence classes.
	 * @throws NullPointerException
	 *             If equivalenceClasses is <code>null</code>.
	 */
	public void setEquivalenceClasses(IEquivalentTerms equivalenceClasses);

	/**
	 * Returns the equivalence classes determining the equivalent terms.
	 * 
	 * @return The equivalence classes.
	 */
	public IEquivalentTerms getEquivalenceClasses();

}
