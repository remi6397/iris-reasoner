package at.sti2.streamingiris;

import java.util.List;


import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.storage.IRelation;

public class ResultFormatter {

	/**
	 * Formats the results of a query of IRIS.
	 * 
	 * @param query
	 *            The query that has been executed.
	 * @param variableBindings
	 *            The variable bindings of the query.
	 * @param result
	 *            The result of the execution.
	 * @return The newly created datalog facts.
	 */
	public static String format(IQuery query, List<IVariable> variableBindings,
			IRelation result) {
		// FIXME Norbert: implement formatter.
		StringBuilder results = new StringBuilder();

		for (int i = 0; i < result.size(); i++) {
			results.append(result.get(i).toString());
			results.append("\n");
		}

		return results.toString();
	}
}
