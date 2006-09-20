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
package org.deri.iris.evaluation.qsq;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;

/**
 * 
 * <b>NOTE: At the moment this class only works with rules with one literal
 * in the head.</b><br/><br/>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   28.07.2006 12:51:07
 */
public class QSQRule {
	
	private AdornedRule adornedRule;
	
	private LinkedList<SupplementaryRelation> supplementaryRelations;

	private Set<ITuple> input = null;
	
	private Set<ITuple> output = null;
	
	public QSQRule(AdornedRule ar, LinkedList<SupplementaryRelation> sr) {
		if ((1 + ar.getBodyLiterals().size()) !=
				sr.size()) {
			throw new IllegalArgumentException(
					"Wrong input parameters! (Set of supplementary " +
					"relations contains: " + sr.size() + " instead of " +
					1 + ar.getBodyLiterals().size());
		}
		this.adornedRule = ar;
		this.supplementaryRelations = sr;
		this.input = new HashSet<ITuple>();
		this.output = new HashSet<ITuple>();
	}

	/**
	 * @return Returns the rule.
	 */
	public AdornedRule getAdornedRule() {
		return adornedRule;
	}

	/**
	 * @param ar The rule to set.
	 */
	/*public void setRule(AdornedRule ar) {
		this.adornedRule = ar;
	}*/

	/**
	 * @return Returns the supplementaryRelations.
	 */
	public LinkedList<SupplementaryRelation> getSupplementaryRelations() {
		return supplementaryRelations;
	}

	/**
	 * @return Returns the input.
	 */
	public Set<ITuple> getInput() {
		return input;
	}

	/**
	 * @return Returns the output.
	 */
	public Set<ITuple> getOutput() {
		return output;
	}
	
}
