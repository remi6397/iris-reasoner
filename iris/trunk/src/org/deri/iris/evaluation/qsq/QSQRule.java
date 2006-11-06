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
import java.util.List;
import java.util.NoSuchElementException;

import org.deri.iris.api.evaluation.common.IAdornedRule;
import org.deri.iris.evaluation.common.Adornment;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;

/**
 * 
 * <b>NOTE: At the moment this class only works with rules with one literal
 * in the head.</b><br/><br/>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   28.07.2006 12:51:07
 */
public class QSQRule {
	/**
	 * adorned rule
	 */
	private IAdornedRule adRule;
	
	private List<SupplementaryRelation> supRels;

	public QSQRule(final IAdornedRule ar, final List<SupplementaryRelation> sr) {
		if ((1 + ar.getBodyLiterals().size()) !=
				sr.size()) {
			throw new IllegalArgumentException(
					"Wrong input parameters! (Set of supplementary " +
					"relations contains: " + sr.size() + " instead of " +
					1 + ar.getBodyLiterals().size());
		}
		this.adRule = ar;
		this.supRels = sr;	
	}

	/**
	 * @return Returns the rule.
	 */
	public IAdornedRule getAdornedRule() {
		return adRule;
	}

	/**
	 * @return list of supplementary relations for the entire rule
	 */
	public List<SupplementaryRelation> getSupRels() {
		return supRels;
	}

	/**
	 * @return Returns copz of the supplementary relation list 
	 * 	       (copy means that the structure of a supplementary 
	 * 		   relation will remain the same but none of its properties
	 * 		   will be set to any non default value).
	 */
	public List<SupplementaryRelation> cloneSupRels() {
		List<SupplementaryRelation> rels = new ArrayList();
		for(SupplementaryRelation sr : this.supRels){
			rels.add(sr.clone());
		}
		return rels;
	}

	/**
	 * @param fromSR starting supplementary relation
	 * @return       Returns first sub sequential supplementary relation after fromSR,
	 *  		     or null if sr is the last one (meant for retriveing a 
	 *  			 supplementary relation whose subscript is j).
	 */
	public SupplementaryRelation getSup_1(SupplementaryRelation fromSR) {
		int i = this.supRels.indexOf(fromSR);
		if(i == -1) 
			return null;
		if(i+1<this.supRels.size())
			return this.supRels.get(i+1);
		else
			return null;
	}
	
	/**
	 * @param fromSR starting supplementary relation
	 * @return		 true if the iteration has more elements after fromSR
	 */
	public boolean hasNext(SupplementaryRelation fromSR) {
		int i = this.supRels.lastIndexOf(fromSR);
		if(i == -1) 
			 throw new NoSuchElementException(fromSR.toString() + 
					" doesn't exist in the list of supplementary relations");
		
		if(i+1 == this.supRels.size())
			return false;
		else
			return true;
	}
	
	/**
	 * @return Returns supplementary relation that currently
	 * 		   represents sup_0 (supplementary relation whose 
	 * 		   subscript is j-1).
	 */
	public SupplementaryRelation getSup_0() {
		for(SupplementaryRelation sr : this.supRels){
			if(! sr.isHandled() && this.hasNext(sr)) return sr;
		}
		return null;
	}
	
	/**
	 * @param ap adorned predicate
	 * @return   relation's arity
	 */
	public int getInputArity(AdornedPredicate ap){
		int arity = 0;
		Adornment[] ads = ap.getAdornment();
		for(int i=0; i<ads.length; i++){
			if(ads[i] == Adornment.BOUND)
				arity++;
		}
		return arity;
	}
	
}
