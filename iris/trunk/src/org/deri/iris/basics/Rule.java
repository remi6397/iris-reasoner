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
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;

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
	
	private IHead head = null;
	private IBody body = null;
	
	Rule(final IHead head, final IBody body) {
		if (body == null) {
			throw new IllegalArgumentException("The body must not be null");
		}
		this.head = head;
		this.body = body;
	}
	
	public IHead getHead()
	{
		return head;
	}
	
	public IBody getBody()
	{
		return body;
	}

	public boolean isRectified() {
		// TODO Auto-generated method stub
		return false;
	}

//	public int getHeadLenght() {
//		return head.getHeadLenght();
//	}
//
//	public ILiteral getHeadLiteral(int arg) {
//		return head.getHeadLiteral(arg);
//	}
//
//	public List<ILiteral> getHeadLiterals() {
//		return head.getHeadLiterals();
//	}
//
//	public List<IVariable> getHeadVariables() {
//		return head.getHeadVariables();
//	}
//
	public int getBodyLenght() {
		return body.getLength();
	}

	public ILiteral getBodyLiteral(int arg) {
		return body.getLiteral(arg);
	}

	public List<ILiteral> getBodyLiterals() {
		return body.getLiterals();
	}

	public List<IVariable> getBodyVariables() {
		return body.getVariables();
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
		if (!(o instanceof Rule)) {
			return false;
		}
		Rule r = (Rule) o;
		return body.equals(r.body) && head.equals(r.head);
	}
	
	public String toString() {
		return head + " :- " + body;
	}
}
