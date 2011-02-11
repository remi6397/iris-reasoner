/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.basics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;

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
	
	public List<ILiteral> getBody()
	{
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
			if( first )
				first = false;
			else
				buffer.append( ", " );
			buffer.append(l);
		}

		buffer.append(" :- ");

		first = true;
		for (final ILiteral l : body) {
			if( first )
				first = false;
			else
				buffer.append(", ");
			buffer.append(l);
		}
		buffer.append('.');
		return buffer.toString();
	}
}
