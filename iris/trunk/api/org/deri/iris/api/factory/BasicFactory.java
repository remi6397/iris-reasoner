package org.deri.iris.api.factory;

import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.terms.ITerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   13.01.2006 13:27:34
 */
public interface BasicFactory {
	
	public IPredicate createPredicate(String symbol, int arity);
	
	public IAtom createAtom(IPredicate p, List<ITerm> terms);
	
	public ILiteral createLiteral(boolean isPositive,IAtom atom);
	
	public ILiteral createLiteral(boolean isPositive,IBuiltInAtom builtinAtom);
	
	public IBody createBody(List<ILiteral> literals);
	
	//public IHead createHead(ILiteral literal); 
	public IHead createHead(List<ILiteral> literals);
	
	public IRule createRule(IHead head, IBody body);
	
	public IQuery createQuery(List<ILiteral> literals);
}
