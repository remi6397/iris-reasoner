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

import org.deri.iris.VariableExtractor;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.terms.IVariable;

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
			throw new IllegalArgumentException("The literals must not contain null");
		}
		this.literals = Collections.unmodifiableList(new ArrayList<ILiteral>(literals));
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
			if( first )
				first = false;
			else
				buffer.append( ", " );
			buffer.append(l);
		}
		buffer.append('.');
		return buffer.toString();
	}
}
