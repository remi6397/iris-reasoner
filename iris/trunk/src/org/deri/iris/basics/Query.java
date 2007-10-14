/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.basics;

import java.util.List;

import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.terms.IVariable;

/**
 * @author richi
 *
 */
public class Query implements IQuery {
	
	private IBody body = null;
	
	Query(final IBody body) {
		this.body = body;
	}

	public int getLength() {
		return body.getLength();
	}

	public ILiteral getLiteral(int arg) {
		return body.getLiteral(arg);
	}

	public List<ILiteral> getLiterals() {
		return body.getLiterals();
	}

	public List<IVariable> getVariables() {
		return body.getVariables();
	}
	
	public int hashCode() {
		return body.hashCode();
	}
	
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Query)) {
			return false;
		}
		Query q = (Query) o;
		return body.equals(q.body);
	}
	
	public String toString() {
		return body.toString();
	}
}
