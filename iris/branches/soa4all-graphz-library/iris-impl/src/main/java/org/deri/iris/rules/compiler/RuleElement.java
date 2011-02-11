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
package org.deri.iris.rules.compiler;

import java.util.List;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.facts.IFacts;
import org.deri.iris.storage.IRelation;

/**
 * A base class for all compiled rule elements.
 */
public abstract class RuleElement
{
	/**
	 * Default constructor.
	 */
	public RuleElement()
	{
	}
	
	/**
	 * Called to process tuples from previous literals.
	 * @param previous The relation of tuples from the previous rule element.
	 * This should be null if this element represents the first literal.
	 * @return The output relation for this literal.
	 * @throws EvaluationException 
	 */
	public abstract IRelation process( IRelation input ) throws EvaluationException;
	
	/**
	 * Create a substitute rule element that will use the corresponding delta if it exists.
	 * @param deltas The Deltas from the last round of iterative evaluation.
	 * @return A substitute rule element if possible.
	 */
	public RuleElement getDeltaSubstitution( IFacts deltas )
	{
		return null;
	}
	
	/**
	 * Get the variable bindings for tuples output from this rule element.
	 * @return The list of variables in term order.
	 */
	public List<IVariable> getOutputVariables()
	{
		return mOutputVariables;
	}
	
	/** The variable bindings for tuples output from this rule element. */
	protected List<IVariable> mOutputVariables;
}
