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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.evaluation.common.IAdornedProgram;
import org.deri.iris.api.evaluation.common.IAdornedRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.common.Adornment;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;

/**
 * QSQTemplate consists of adorned program where each rule from the program
 * is associated with corresponding supplementary relations (a total of (n+1) 
 * supplementary relations are associated to a rule which has a body consisting
 * of n atoms).
 * 
 * <b>NOTE: At the moment
 * this class only works with rules which have one literal in the head!</b>
 * <b>NOTE: Constructed terms (function symbols) currently not supported!</b>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   28.07.2006 09:10:51
 */
public class QSQTemplate {
	
	private IAdornedProgram adornedProgram;
	
	private Map<AdornedPredicate, Set<QSQRule>> qsqMap;
	
	/**
	 * List of variables of the first supplementary relation
	 */
	List<IVariable> variables_0 = null;
		
	/**
	 * List of variables of the last supplementary relation
	 */
	List<IVariable> variables_n = null;
	
	
	QSQTemplate(final IAdornedProgram ap){
		if (ap == null) {
			throw new IllegalArgumentException(
					"Input argument must not be null");
		}
		this.adornedProgram = ap;
		this.qsqMap = new HashMap<AdornedPredicate, Set<QSQRule>>();
	}
	
	/**
	 * @return the qsqRules (QSQ Template).
	 */
	public Map<AdornedPredicate, Set<QSQRule>> getQSQTemplate() {
		Iterator<IAdornedRule> i = this.adornedProgram.getAdornedRules().iterator();
		QSQRule qsqRule = null;
		IAdornedRule adornedRule = null;
		List<SupplementaryRelation> supplementaryRelations = null;
		AdornedPredicate ap = null;
		Set<QSQRule> qsqRules = null;
		int j = 0;
		
		while(i.hasNext()){
			supplementaryRelations 
							= new ArrayList<SupplementaryRelation>();
			adornedRule = (IAdornedRule)i.next();
			supplementaryRelations = createSupplementaryRelations(j, adornedRule);
			qsqRule = new QSQRule(adornedRule, supplementaryRelations);
			ap = (AdornedPredicate)adornedRule.getHeadLiteral(0).getPredicate();
			qsqRules = this.qsqMap.get(ap);
			if(qsqRules == null){
				qsqRules = new HashSet<QSQRule>();
			}
			qsqRules.add(qsqRule);
			this.qsqMap.put(ap, qsqRules);
			j++;
		}
		return this.qsqMap;
	}
	
	private List<SupplementaryRelation> createSupplementaryRelations(
			int superscript, IAdornedRule ar){
		
		List<SupplementaryRelation> supplementaryRelations 
				= new ArrayList<SupplementaryRelation>();
		SupplementaryRelation supplementaryRelation = null;
		ILiteral head = ar.getHeadLiteral(0);
		
		List<SupplementaryRelation> sr = getFirstAndLastSupplementaryRelations(
				superscript, ar.getBodyLiterals().size(), head);
				
		supplementaryRelations.add(sr.get(0));
		
		supplementaryRelations.addAll(
				getSupRels(superscript, ar.getBodyLiterals()));
		
		supplementaryRelations.add(sr.get(sr.size()-1));
		
		return supplementaryRelations;
	}
	
	private List<SupplementaryRelation> getFirstAndLastSupplementaryRelations(
			int superscript, int subscript, ILiteral h){
		
		List<SupplementaryRelation> supRels 
			= new ArrayList<SupplementaryRelation>();
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
// 				TODO: Constructed terms (function symbols) currently not supported!
//				if(!(term instanceof ConstructedTerm)){
//					variables.add(term);
//				}
			}
		}
		supRels.add(
				new SupplementaryRelation(superscript, 0, variables_0));
		supRels.add(		
				new SupplementaryRelation(
						superscript, subscript, variables_n));
		return supRels;
	}

	/**
	 * maybe slightly more efficient implementation of the above method 
	 * (that is commented).
	 * But the order of variables in each supplementary relation is not
	 * preserved. 
	 */
	private List<SupplementaryRelation> getSupRels(
			int superscript, List<ILiteral> body){
		
		List<SupplementaryRelation> supRels 
			= new ArrayList<SupplementaryRelation>();
		List<IVariable> variables_before = null;
		List<IVariable> variables = null;
		ILiteral literal = null;
		List<ITerm> terms = null;
		
		for(int i=0; i<body.size()-1; i++){
			variables_before = new ArrayList<IVariable>();
			variables_before.addAll(variables_0);
			variables = new ArrayList<IVariable>();
			
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
					if(term instanceof IVariable && variables_before.contains(term))
						variables.add((IVariable)term);
				}
			}
			for(IVariable variable : variables_n){
				if(variables_before.contains(variable) && !variables.contains(variable))
					variables.add(variable);
			}
			supRels.add(
					new SupplementaryRelation(superscript, i+1, variables));
		}
		return supRels;
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		Set<QSQRule> qsqRules = null;
		Iterator i = null;
		QSQRule qsqRule = null;
		if(this.qsqMap.isEmpty()){
			
			Map.Entry entry = null;
			for (Iterator iter = this.qsqMap.entrySet().iterator(); iter.hasNext();){ 
			    entry = (Map.Entry)iter.next();
			    qsqRules = (Set<QSQRule>)entry.getValue();
			    i = qsqRules.iterator();
				while(i.hasNext()){
					qsqRule = (QSQRule)i.next();
					buffer.append("\n");
					buffer.append(qsqRule.getAdornedRule().toString());
					buffer.append("\n");
					buffer.append(qsqRule.getSupRels().toString());
				}
			}
			
			/*Iterator it = this.qsqMap.keySet().iterator();
			while(i.hasNext()){
				qsqRules = this.qsqMap.get(i.next());
				i = qsqRules.iterator();
				while(i.hasNext()){
					qsqRule = (QSQRule)i.next();
					buffer.append("\n");
					buffer.append(qsqRule.getAdornedRule().toString());
					buffer.append("\n");
					buffer.append(qsqRule.getSupRels().toString());
				}
			}*/
		}
		return buffer.toString();
	}
}
