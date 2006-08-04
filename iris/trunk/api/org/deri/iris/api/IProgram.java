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

import java.util.Set;

import org.deri.iris.api.basics.IQuery;

/**
 * Interface of a (logic) program used to promote modularity 
 * of the inference engine.
 * 
 * This interface defines an evaluation process. 
 * An implementation of this interface is supposed to get implementations
 * of the IEDB and the IEvaluator interfaces as input parameters and to 
 * produce a substitution or a set of substitutions as an output of 
 * the evaluation process.  
 * This interface also defines a procedure for analyzing a given program 
 * (particularly the entire EDB) and applies the best set of optimization 
 * and evaluation techniques, which are available in IRIS, in order to 
 * produce answers. 
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 10.11.2005
 */
public interface IProgram {

	// Correct it!
	public enum EvaluationMethod {
		SEMI_NAIVE_EVALUATION, DYNAMIC_FILTERING_EVALUATION,
		WELLFOUNDED_withAFP_EVALUATION, WELLFOUNDED_EVALUATION
	};

	/**
	 * Determines the evaluation method.<BR>
	 * 0: Naive Evaluator (only stratified prorgams)<BR>
	 * 1: Dynamic Filtering Evaluator (only stratified prorgams)<BR>
	 * 2: Wellfounded Evaluator with alternating fixed point<BR>
	 * 3: Wellfounded Evaluator
	 */
	public void setEvaluationMethod(final EvaluationMethod method);

	/**
	 * 
	 */
	public boolean execute();
	
	/**
	 * Compute substitutions of the evaluation result for a query q.<br>
	 * The result is a set where each object is a tuple.<br>
	 * Every tuple is one variable substitution for the query.<br>
	 */
	public Set computeSubstitution(final IQuery q);

	/**
	 * Returns all the evaluation results for all queries.<br>
	 * The result is a set where each object is a tuple.<br>
	 * Every tuple is one variable substitution for a certain query.<br>
	 */
	public Set computeSubstitutions();
	
}
