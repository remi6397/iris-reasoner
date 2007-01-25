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
package org.deri.iris.api;

import java.util.Map;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.storage.IRelation;

/**
 * <p>
 * This interface defines an evaluation process. An implementation of this
 * interface is supposed to get implementations of the IProgram and the
 * IEvaluator interfaces as input parameters and to produce an output of the
 * evaluation process.
 * </p>
 * <p>
 * Apart from that, this interface also defines a procedure for analyzing a
 * given program (particularly the entire IDB and EDB) and applies the best set
 * of optimization and evaluation techniques, which are available in IRIS, in
 * order to produce answers.
 * </p>
 * <p>
 * This interface is used to promote modularity of the inference engine.
 * </p>
 * <p>
 * $Id: IExecutor.java,v 1.5 2007-01-25 12:51:12 darko Exp $
 * </p>
 * 
 * @author Richard PÃ¶ttler
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision: 1.5 $
 */
public interface IExecutor {

	public enum EvaluationMethod {
		NAIVE_EVALUATION, 
		SEMI_NAIVE_EVALUATION, 
		QSQ, 
		SEMI_NAÏVE_WITH_MAGIC_SETS
	};

	/**
	 * <p>
	 * Executes the evaluation.
	 * </p>
	 * 
	 * @return True if the evaluation has been successfully terminated,
	 *         otherwise false.
	 */
	public boolean execute();

	/**
	 * <p>
	 * Compute substitutions of the evaluation result for a query.
	 * </p>
	 * 
	 * @param q
	 *            The query for which to compute the substitutions.
	 * @return A set of substitutions (IRelation) for the given query.
	 */
	public IRelation computeSubstitution(final IQuery q);

	/**
	 * <p>
	 * Returns the evaluation results for all queries from the knowledgebase.
	 * </p>
	 * 
	 * @return A map with query predicates (a query with only 
	 * 			one literal) as a key, and the retrieved substitutions 
	 * 			as values for those queries.
	 */
	public Map<IPredicate, IRelation> computeSubstitutions();	
}
