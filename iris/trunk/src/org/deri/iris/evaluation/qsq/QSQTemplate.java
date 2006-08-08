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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.common.Adornment;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;

/**
 * 
 * <b>NOTE: At the moment
 * this class only works with rules which have one literal in the head!</b>
 * <b>NOTE: Constructed terms (function symbols) currently not supported!</b>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   28.07.2006 09:10:51
 */
public class QSQTemplate {
	
	private AdornedProgram adornedProgram;
	
	private Set<QSQRule> qsqRules;
	
	/**
	 * List of variables of the first supplementary relation
	 */
	List<IVariable> variables_0 = null;
		
	/**
	 * List of variables of the last supplementary relation
	 */
	List<IVariable> variables_n = null;
	
	
	QSQTemplate(final AdornedProgram ap){
		if (ap == null) {
			throw new IllegalArgumentException(
					"Input argument must not be null");
		}
		this.adornedProgram = ap;
		this.qsqRules = new HashSet();
	}
	
	/**
	 * @return Returns the qsqRules (QSQ Template).
	 */
	public Set<QSQRule> getQSQTemplate() {
		Iterator<AdornedRule> i = this.adornedProgram.getAdornedRules().iterator();
		QSQRule qsqRule = null;
		AdornedRule adornedRule = null;
		LinkedList<SupplementaryRelation> supplementaryRelations = null;
		int j = 0;
		while(i.hasNext()){
			supplementaryRelations 
							= new LinkedList<SupplementaryRelation>();
			adornedRule = (AdornedRule)i.next();
			supplementaryRelations = createSupplementaryRelations(j, adornedRule);
			qsqRule = new QSQRule(adornedRule, supplementaryRelations);
			this.qsqRules.add(qsqRule);
			j++;
		}
		return this.qsqRules;
	}
	
	private LinkedList<SupplementaryRelation> createSupplementaryRelations(
			int superscript, AdornedRule ar){
		
		LinkedList<SupplementaryRelation> supplementaryRelations 
						= new LinkedList<SupplementaryRelation>();
		SupplementaryRelation supplementaryRelation = null;
		ILiteral head = ar.getHeadLiteral(0);
		
		supplementaryRelations.add(getFirstAndLastSupplementaryRelations(
				superscript, ar.getBodyLiterals().size(), head).getFirst());
		
		supplementaryRelations.addAll(
				getSupplementaryRelations(superscript, ar.getBodyLiterals()));
		
		supplementaryRelations.add(getFirstAndLastSupplementaryRelations(
				superscript, ar.getBodyLiterals().size(), head).getLast());
		
		return supplementaryRelations;
	}
	
	private LinkedList<SupplementaryRelation> getFirstAndLastSupplementaryRelations(
			int superscript, int subscript, ILiteral h){
		
		LinkedList<SupplementaryRelation> supplementaryRelations 
						= new LinkedList<SupplementaryRelation>();
		this.variables_0 = new ArrayList<IVariable>();
		this.variables_n = new ArrayList<IVariable>();
		SupplementaryRelation supplementaryRelation = null;
		AdornedPredicate predicate = null;
		ITerm term = null;
		
		if(h.getPredicate()instanceof AdornedPredicate)
			predicate = (AdornedPredicate)h.getPredicate();
		Adornment[] adornments = predicate.getAdornment();
		
		for(int i=0; i<adornments.length; i++){
			term = h.getTuple().getTerm(i);
			if(term instanceof IVariable){
				if(adornments[i].compareTo(Adornment.BOUND) == 0){
					variables_0.add((IVariable)term);
				}
				variables_n.add((IVariable)term);
			}else{
// 				Constructed terms (function symbols) currently not supported!
//				if(!(term instanceof ConstructedTerm)){
//					variables.add(term);
//				}
			}
		}
		supplementaryRelations.add(
				new SupplementaryRelation(superscript, 0, variables_0));
		supplementaryRelations.add(		
				new SupplementaryRelation(
						superscript, subscript, variables_n));
		return supplementaryRelations;
	}
	
	private LinkedList<SupplementaryRelation> getSupplementaryRelations(
			int superscript, List<ILiteral> body){
		
		LinkedList<SupplementaryRelation> supplementaryRelations 
						= new LinkedList<SupplementaryRelation>();
		List<IVariable> variables_before = null;
		List<IVariable> variables_after = null;
		List<IVariable> variables = null;
		ILiteral literal = null;
		List<IVariable> terms = null;
		
		for(int i=0; i<body.size()-1; i++){
			variables_before = new ArrayList<IVariable>();
			variables_before.addAll(variables_0);
			variables_after = new ArrayList<IVariable>();
			variables_after.addAll(variables_n);
			
			for(int j=0; j<i+1; j++){
				literal = body.get(j);
				terms = literal.getTuple().getTerms();
				for(ITerm term : terms){
					if(term instanceof IVariable && !variables_before.contains(term))
						variables_before.add((IVariable)term);
				}
			}
			for(int j=i+1; j<body.size(); j++){
				literal = body.get(j);
				terms = literal.getTuple().getTerms();
				for(ITerm term : terms){
					if(term instanceof IVariable && !variables_after.contains(term))
						variables_after.add((IVariable)term);
				}
			}
			variables = new ArrayList<IVariable>();
			for(IVariable variable : variables_after){
				if(variable != null && variables_before.contains(variable)){
					variables.add(variable);
				}
			}
			supplementaryRelations.add(
					new SupplementaryRelation(superscript, i+1, variables));
		}
		return supplementaryRelations;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		Iterator i = qsqRules.iterator();
		QSQRule qsqRule = null;
		while(i.hasNext()){
			qsqRule = (QSQRule)i.next();
			buffer.append("\n");
			buffer.append(qsqRule.getAdornedRule().toString());
			buffer.append("\n");
			buffer.append(qsqRule.getSupplementaryRelations().toString());
		}
		return buffer.toString();
	}
}
