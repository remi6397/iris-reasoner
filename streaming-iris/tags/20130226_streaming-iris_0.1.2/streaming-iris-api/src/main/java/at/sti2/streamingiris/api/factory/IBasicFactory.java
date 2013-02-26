package at.sti2.streamingiris.api.factory;

import java.util.List;

import at.sti2.streamingiris.api.basics.IAtom;
import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * <p>
 * An interface that can be used to create set of basic logical entities such as
 * predicates, atoms, rules, queries etc.
 * </p>
 * <p>
 * $Id: IBasicFactory.java,v 1.19 2007-10-30 09:15:07 bazbishop237 Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.19 $
 */
public interface IBasicFactory {

	public IAtom createAtom(IPredicate p, ITuple tuple);

	/**
	 * Creates a deep copy of an atom. The terms themselves will remain the same
	 * instances.
	 * 
	 * @param a
	 *            the atom to copy
	 * @return the copy
	 * @throws NullPointerException
	 *             if the atom is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the atom is a builtin one.
	 * @since 0.3
	 */
	public IAtom createAtom(final IAtom a);

	public ILiteral createLiteral(boolean isPositive, IAtom atom);

	public ILiteral createLiteral(boolean isPositive, IPredicate predicate,
			ITuple tuple);

	/**
	 * Creates a deep copy of a literal. The terms themselves will remain the
	 * same instances.
	 * 
	 * @param l
	 *            the tuple to copy
	 * @return the copy
	 * @throws NullPointerException
	 *             if the literal is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the underlying atom is a builtin one.
	 * @since 0.3
	 */
	public ILiteral createLiteral(final ILiteral l);

	public IPredicate createPredicate(String symbol, int arity);

	public IQuery createQuery(ILiteral... literals);

	public IQuery createQuery(List<ILiteral> literals);

	/**
	 * Creates a rule out of a list of head and a list of body literals.
	 * 
	 * @param head
	 *            the head literals
	 * @param body
	 *            the body literals
	 */
	public IRule createRule(List<ILiteral> head, List<ILiteral> body);

	public ITuple createTuple(ITerm... terms);

	public ITuple createTuple(List<ITerm> terms);
}
