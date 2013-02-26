package at.sti2.streamingiris.api.basics;

import java.util.List;

import at.sti2.streamingiris.api.terms.IVariable;

/**
 * A query is a rule without the head.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 19.11.2005 15:35:44
 */

public interface IQuery {

	public List<ILiteral> getLiterals();

	public List<IVariable> getVariables();
}
