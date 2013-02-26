package at.sti2.streamingiris.api.terms;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * An interface for representing a constructed term (function symbol). A
 * constructed term is a term built from function-s and subter-s.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.11.2005 11:34:59
 */
public interface IConstructedTerm extends ITerm {

	public List<ITerm> getValue();

	/**
	 * Get the name of the constructed term (function symbol).
	 * 
	 * @return The name of the constructed term.
	 */
	public String getFunctionSymbol();

	/**
	 * Returns a list of all terms from this constructed term (function symbol).
	 * 
	 * @return List of all terms from this constructed term.
	 */
	public List<ITerm> getParameters();

	/**
	 * Returns all distinct variables from this tuple.
	 * 
	 * @return All distinct variables from this tuple.
	 */
	public Set<IVariable> getVariables();
}
