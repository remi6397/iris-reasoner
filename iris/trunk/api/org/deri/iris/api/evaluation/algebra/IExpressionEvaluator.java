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
package org.deri.iris.api.evaluation.algebra;

import java.util.Map;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.storage.IRelation;


/**
 * <p>
 * Represents an evaluator of a relational algebra expression.
 * This interface is supposed to be implemented whenever an 
 * evaluator for relational algebra expressions is needed, 
 * regardless whether the evaluation is conducted accessing
 * the DB data or in-memory data only.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date Dec 18, 2006
 * 
 */
public interface IExpressionEvaluator {

	/**
	 * Evaluates the relational algebra expression.
	 * 
	 * @param c
	 * 			A relational algebra expression to be evaluated/executed.
	 * @param p
	 * 			The entire program including idb and edb relations 
	 *          required for the evaluation of c.
	 * @return	Implicit tuples derived after the evaluation of c.
	 */
	public IRelation evaluate(IComponent c, IProgram p);

	/**
	 * Evaluates the relational algebra expression incrementally.
	 * 
	 * @param c
	 * 			A relational algebra expression to be evaluated/executed.
	 * @param p
	 * 			The entire program including idb and edb relations 
	 *          required for the evaluation of c.
	 * @param aq
	 * 			IDB predicated and corresponding relations with tuples 
	 * 			derived during the previous round.
	 * @return	Implicit tuples derived after the evaluation of c.
	 */
	public IRelation evaluateIncrementally (IComponent c, IProgram p, Map<IPredicate, IRelation> aq);
}
