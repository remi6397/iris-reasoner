package at.sti2.streamingiris.api.factory;

import java.util.Collection;

import at.sti2.streamingiris.api.terms.IConstructedTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;

/**
 * <p>
 * An interface that can be used to create a term as a logical entity. Different
 * types of terms may be constructed.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 20.02.2006 16:05:29
 */
public interface ITermFactory {

	public IConstructedTerm createConstruct(final String name,
			Collection<ITerm> terms);

	public IConstructedTerm createConstruct(final String name, ITerm... terms);

	public IStringTerm createString(String arg);

	public IVariable createVariable(String name);
}
