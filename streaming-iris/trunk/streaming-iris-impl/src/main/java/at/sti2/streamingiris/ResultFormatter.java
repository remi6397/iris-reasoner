package at.sti2.streamingiris;

import java.util.List;

import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.ITuple;
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
		StringBuilder results = new StringBuilder();
		StringBuffer resultStringBuffer;

		for (int i = 0; i < result.size(); i++) {
			ITuple resultTuple = result.get(i);
			for (ILiteral literal : query.getLiterals()) {
				String literalString = literal.toString();
				for (int j = 0; j < resultTuple.size(); j++) {
					String resultString = resultTuple.get(j).toString();
					if (!resultString.startsWith("'")) {
						resultStringBuffer = new StringBuffer();
						resultStringBuffer.append("'");
						resultStringBuffer.append(resultString);
						resultStringBuffer.append("'");
						resultString = resultStringBuffer.toString();
					}
					literalString = literalString.replace(
							variableBindings.get(j).toString(), resultString);
				}
				results.append(literalString);
				results.append(".\n");
			}
		}

		String res = results.toString();
		return res;
	}
}
