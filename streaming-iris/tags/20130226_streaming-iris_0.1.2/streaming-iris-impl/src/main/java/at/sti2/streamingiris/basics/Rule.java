package at.sti2.streamingiris.basics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IRule;

/**
 * <p>
 * Represents a datalog rule.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Rule implements IRule {

	private final List<ILiteral> head;

	private final List<ILiteral> body;

	Rule(final List<ILiteral> head, final List<ILiteral> body) {
		if (head == null) {
			throw new IllegalArgumentException("The head must not be null");
		}
		if (head.contains(null)) {
			throw new IllegalArgumentException("The head must not contain null");
		}
		if (body == null) {
			throw new IllegalArgumentException("The body must not be null");
		}
		if (body.contains(null)) {
			throw new IllegalArgumentException("The body must not contain null");
		}
		this.head = Collections.unmodifiableList(new ArrayList<ILiteral>(head));
		this.body = Collections.unmodifiableList(new ArrayList<ILiteral>(body));
	}

	public List<ILiteral> getHead() {
		return head;
	}

	public List<ILiteral> getBody() {
		return body;
	}

	public boolean isRectified() {
		// TODO Auto-generated method stub
		return false;
	}

	public int hashCode() {
		int result = 37;
		result = result * 17 + body.hashCode();
		result = result * 17 + head.hashCode();
		return result;
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof IRule)) {
			return false;
		}
		IRule r = (IRule) o;
		return body.equals(r.getBody()) && head.equals(r.getHead());
	}

	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		boolean first = true;
		for (final ILiteral l : head) {
			if (first)
				first = false;
			else
				buffer.append(", ");
			buffer.append(l);
		}

		buffer.append(" :- ");

		first = true;
		for (final ILiteral l : body) {
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
