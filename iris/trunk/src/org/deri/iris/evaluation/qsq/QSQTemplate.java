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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;

/**
 * 
 * <b>NOTE: At the moment
 * this class only works with rules with one literal in the head!</b>
 * <b>NOTE: Constructed terms (function symbols) currently not supported!</b>
 * @author Darko Anicic, DERI Innsbruck
 * @date   28.07.2006 09:10:51
 */
public class QSQTemplate {
	
	private AdornedProgram adornedProgram;
	
	private Set<QSQRule> qsqRules;
	
	//private Set<SupplementaryRelation> supplementaryRelations; 
	
	private LinkedList<SupplementaryRelation> supplementaryRelations;
	
	/**
	 * Set of variables of the first supplementary relation
	 */
	Set<IVariable> variables_0 = null;
		
	/**
	 * Set of variables of the last supplementary relation
	 */
	Set<IVariable> variables_n = null;
	
	
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
		int j = 0;
		while(i.hasNext()){
			supplementaryRelations = new LinkedList();
			adornedRule = (AdornedRule)i.next();
			createSupplementaryRelations(j, adornedRule);
			qsqRule = new QSQRule(adornedRule, supplementaryRelations);
			this.qsqRules.add(qsqRule);
			j++;
		}
		return this.qsqRules;
	}
	// Correct it!
	private void createSupplementaryRelations(int superscript, AdornedRule ar){
		SupplementaryRelation supplementaryRelation = null;
		ILiteral head = ar.getHeadLiteral(0);
		
		supplementaryRelation = getFirstAndLastSupplementaryRelations(
				superscript, ar.getBodyLiterals().size(), head);
		
		getSupplementaryRelations(superscript, ar.getBodyLiterals());
		this.supplementaryRelations.add(supplementaryRelation);
	}
	
	// Correct it!
	private SupplementaryRelation getFirstAndLastSupplementaryRelations(
			int superscript, int subscript, ILiteral h){
		
		this.variables_0 = new HashSet();
		this.variables_n = new HashSet();
		SupplementaryRelation supplementaryRelation = null;
		StringBuilder buffer = new StringBuilder();
		String head = h.getPredicate().toString();
		buffer.append(head.substring(head.lastIndexOf("^")+1, head.length()));
		char b = 'b';
		ITerm term;
		
		for(int i=0; i<buffer.length(); i++){
			term = h.getTuple().getTerm(i);
			if(term instanceof IVariable){
				if(buffer.charAt(i)==b){
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
		supplementaryRelation = new SupplementaryRelation(superscript, 0, variables_0);
		supplementaryRelations.add(supplementaryRelation);
		
		return supplementaryRelation = new SupplementaryRelation(
				superscript, subscript, variables_n);
	}
	
	// Correct it!
	private void getSupplementaryRelations(
			int superscript, Set<ILiteral> bodyLiterals){
		
		SupplementaryRelation supplementaryRelation = null;
		Set<IVariable> variables_before = null;
		Set<IVariable> variables_after = null;
		Set<IVariable> variables = null;
		IVariable variable = null;
		ILiteral literal = null;
		ILiteral[] body = new ILiteral[bodyLiterals.size()];
		body = bodyLiterals.toArray(body);
		Iterator iterator = null;
		
		for(int i=0; i<body.length-1; i++){
			variables_before = new HashSet();
			variables_before.addAll(variables_0);
			variables_after = new HashSet();
			variables_after.addAll(variables_n);
			
			for(int j=0; j<i+1; j++){
				literal = body[j];
				// only IVariable terms add
				variables_before.addAll(literal.getTuple().getTerms());
			}
			for(int j=i+1; j<body.length; j++){
				literal = body[j];
				variables_after.addAll(literal.getTuple().getTerms());
			}
			iterator = variables_after.iterator();
			variables = new HashSet();
			while(iterator.hasNext()){
				variable = (IVariable)iterator.next();
				if(variable != null && variables_before.contains(variable)){
					variables.add(variable);
				}
			}
			supplementaryRelation = new SupplementaryRelation(superscript, i+1, variables);
			supplementaryRelations.add(supplementaryRelation);
		}
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
