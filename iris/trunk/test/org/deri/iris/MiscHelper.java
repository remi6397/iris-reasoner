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
package org.deri.iris;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.LinkedList;
import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;

/**
 * @author richi
 *
 */
public final class MiscHelper {
	
	private MiscHelper() {
		// prevent subclassing
	}
	
	/**
	 * Creates a tuple consisting of IStringTerms of the submitted strings
	 * 
	 * @param s
	 *            the Strings to add to the tuple
	 * @return the tuple
	 */
	public static ITuple createTuple(final String... s) {
		List<ITerm> termList = new LinkedList<ITerm>();
		for (String str : s) {
			termList.add(TERM.createString(str));
		}
		return BASIC.createTuple(termList);
	}
	
	public static ITuple createTuple(final ITerm... t) {
		List<ITerm> termList = new LinkedList<ITerm>();
		for (ITerm term : t) {
			termList.add(term);
		}
		return BASIC.createTuple(termList);
	}
}
