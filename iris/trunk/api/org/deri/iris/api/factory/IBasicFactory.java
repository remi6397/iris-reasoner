package org.deri.iris.api.factory;

import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.terms.ITerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date 13.01.2006 13:27:34
 */
public interface IBasicFactory {

	public IAtom createAtom(IPredicate p);

	public IAtom createAtom(IPredicate p, ITerm... terms);

	public IAtom createAtom(IPredicate p, List<ITerm> terms);

	public IBody createBody(ILiteral... literals);

	public IBody createBody(List<ILiteral> literals);

	public IHead createHead(ILiteral... literals);

	public IHead createHead(List<ILiteral> literals);

	public ILiteral createLiteral(boolean isPositive, IAtom atom);

	public ILiteral createLiteral(boolean isPositive, IBuiltInAtom builtinAtom);

	public ILiteral createLiteral(boolean isPositive, IPredicate predicate);

	public ILiteral createLiteral(boolean isPositive, IPredicate predicate,
			ITerm... terms);

	public ILiteral createLiteral(boolean isPositive, IPredicate predicate,
			List<ITerm> terms);

	public IPredicate createPredicate(String symbol, int arity);

	public IQuery createQuery(ILiteral... literals);

	public IQuery createQuery(List<ILiteral> literals);

	public IRule createRule(IHead head, IBody body);

	public ITuple createTuple(ITerm... terms);

	public ITuple createTuple(List<ITerm> terms);
}
