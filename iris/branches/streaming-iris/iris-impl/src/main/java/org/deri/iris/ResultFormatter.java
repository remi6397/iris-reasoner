package org.deri.iris;

import java.util.List;

import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.IRelation;

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
		// FIXME implement formatter.
		StringBuilder results = new StringBuilder();

		for (int i = 0; i < result.size(); i++) {
			results.append(result.get(i).toString());
			results.append("\n");
		}

		return results.toString();
	}
}
