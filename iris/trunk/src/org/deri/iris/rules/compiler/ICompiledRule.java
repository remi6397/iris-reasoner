/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2007 Digital Enterprise Research Institute (DERI), 
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
package org.deri.iris.rules.compiler;

import java.util.List;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.new_stuff.facts.IFacts;
import org.deri.iris.new_stuff.storage.IRelation;

/**
 * Interface for a compiled rule.
 */
public interface ICompiledRule
{
	/**
	 * Evaluate rule with all known facts.
	 * @return The result relation for this rule.
	 */
	IRelation evaluate();

	/**
	 * Evaluate the rule using deltas (see semi-naive evaluation) to more intelligently seek out
	 * tuples that have not already been computed.
	 * @param deltas The collection of recently discovered facts.
	 * @return The result relation for this rule.
	 */
	IRelation evaluateIteratively( IFacts deltas );
	
	/**
	 * If this compiled rule represents a rule, then return the head predicate.
	 * @return The head predicate.
	 */
	IPredicate headPredicate();
	
	/**
	 * If this compiled rule represents a query, then return the variables bindings of the
	 * result relation.
	 * @return The list of variables in the order in which they are bound to terms of the result relation. 
	 */
	List<IVariable> getVariablesBindings();
}
