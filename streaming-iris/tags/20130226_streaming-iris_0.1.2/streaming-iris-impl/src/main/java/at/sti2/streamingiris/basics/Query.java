package at.sti2.streamingiris.basics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.sti2.streamingiris.VariableExtractor;
import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.terms.IVariable;

/**
 * <p>
 * The query implementation.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Query implements IQuery {

	private List<ILiteral> literals = null;

	Query(final List<ILiteral> literals) {
		if (literals == null) {
			throw new IllegalArgumentException("The literals must not be null");
		}
		if (literals.contains(null)) {
			throw new IllegalArgumentException(
					"The literals must not contain null");
		}
		this.literals = Collections.unmodifiableList(new ArrayList<ILiteral>(
				literals));
	}

	public List<ILiteral> getLiterals() {
		return literals;
	}

	public List<IVariable> getVariables() {
		return VariableExtractor.getLiteralVariablesList(literals);
	}

	public int hashCode() {
		return literals.hashCode();
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof IQuery)) {
			return false;
		}
		IQuery q = (IQuery) o;
		return literals.equals(q.getLiterals());
	}

	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("?- ");
		boolean first = true;
		for (final ILiteral l : literals) {
			if (first)
				first = false;
			else
				buffer.append(", ");
			buffer.append(l);
		}
		buffer.append('.');
		return buffer.toString();
	}
}
