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
package org.deri.iris.api.evaluation;

import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.exception.DataModelException;
import org.deri.iris.storage.Relation;

import java.util.Map;

/**
 * Interface of a particular evaluation procedure used to promote
 * modularity of the inference engine.
 * 
 * Each evaluation method, or a set of evaluation methods that is bind 
 * in one evaluation procedure, needs to implement this interface. 
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   27.07.2006 16:04:43
 * @version $Id: IEvaluator.java,v 1.4 2006-10-23 08:41:54 adi Exp $
 */
public interface IEvaluator {
	
	/******************************** */
	/* query evaluation */
	/******************************** */

	/**
	 * Evaluates all queries which have been added to the entire EDB. <br>
	 * until answers have been derived.<br>
	 * Returns true if the evaluation has terminated, otherwise false is returned.
	 * All answers up to now are returned by "Result" or "Substitution".
	 */
	public Map<ITree, IRelation> evaluate() throws DataModelException;
 
}
 