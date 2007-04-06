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
package org.deri.iris.operations.relations;

import static org.deri.iris.factory.Factory.RELATION;

import java.util.Iterator;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.operations.tuple.Concatenation;
import org.deri.iris.operations.tuple.SimpleIndexComparator;

/**
 * <p>
 * Implementation of the sort-merge join operation. 
 * Reference: JoinSimpleExtended Processing in Relational Databases, 
 * PRITI MISHRA and MARGARET H. EICH
 * </p>
 *
 * @author Darko Anicic, DERI Innsbruck
 * @date   24.05.2006 09:26:43
 */
public class JoinSimple implements IJoin{

	private IRelation relation0 = null;

	private IRelation relation1 = null;

	private int[] indexes = null;

	private int[] projectIndexes = null;

	private JoinCondition CONDITION = null;

	/** Default value for the form of join operation. */
	private JoinForm FORM = JoinForm.THETA_JOIN;
	
	/**
	 * If true, make Cartesian product (a special
	 * case of join operation), otherwise join
	 * regularly.
	 */
	private boolean isCartesian = false;
	
	JoinSimple(IRelation arg0, IRelation arg1, int[] inds, JoinForm form) {
		if (arg0 == null || arg1 == null || inds == null) {
			throw new IllegalArgumentException(
					"Input parameters must not be null!");
		}
		this.relation0 = arg0;
		this.relation1 = arg1;
		this.indexes = inds;
		this.CONDITION = JoinCondition.EQUALS;
		if(form != null){
			this.FORM = form;
		}
	}

	JoinSimple(IRelation arg0, IRelation arg1, int[] inds) {
		if (arg0 == null || arg1 == null || inds == null) {
			throw new IllegalArgumentException(
					"Input parameters must not be null!");
		}
		this.relation0 = arg0;
		this.relation1 = arg1;
		this.indexes = inds;
		this.CONDITION = JoinCondition.EQUALS;
	}
	
	JoinSimple(IRelation arg0, IRelation arg1, int[] inds,
			JoinCondition condition) {
		if (arg0 == null || arg1 == null || inds == null
				|| condition == null) {
			throw new IllegalArgumentException("All constructor "
					+ "parameters must be specified non null values!");
		}
		this.relation0 = arg0;
		this.relation1 = arg1;
		this.indexes = inds;
		this.CONDITION = condition;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param inds
	 * @param condition
	 * @param projectIndexes
	 *            define indexes which the projection operation will be applied
	 *            on. For example, if set of tuples of arity 3 needs to be
	 *            projected on the first and last term, the projectIndexes will
	 *            look like: [0, -1, 1]. -1 means that terms with that index
	 *            will be omitted. Note that for an array with the projection 
	 *            indexes [1, -1, 0], we would also have the projection on the 
	 *            first and last term, but in this case, the term which was 
	 *            initially last one (3rd  one with index 0) would be placed on the 
	 *            first position, and one which was initially first one, would be 
	 *            placed on the second position (index 1). The middle one 
	 *            (index -1) would be omitted.
	 *            If not specified join tuples will be simple merged.
	 */
	JoinSimple(IRelation arg0, IRelation arg1, int[] inds,
			JoinCondition condition, int[] projectIndexes) {
		if (arg0 == null || arg1 == null || inds == null
				|| condition == null || projectIndexes == null) {
			throw new IllegalArgumentException("Constructor "
					+ "parameters are not specified correctly!");
		}
		this.relation0 = arg0;
		this.relation1 = arg1;
		this.indexes = inds;
		this.projectIndexes = projectIndexes;
		this.CONDITION = condition;
	}

	private void setJoinOperator() {
		int[] inds0 = null;
		int[] inds1 = null;
		inds0 = this.transformIndexes0(this.relation0.getArity(), this.indexes);
		if(! checkIndexes(inds0, this.relation0.getArity()))
			throw new IllegalArgumentException("Indexes are not specified" +
					" correctly.");
		inds1 = this.transformIndexes1(this.relation1.getArity(), this.indexes);
		if(! checkIndexes(inds1, this.relation1.getArity()))
			throw new IllegalArgumentException("Indexes are not specified" +
					" correctly.");
		
		// TODO remoce isCartesian, use only JoinForm.NATURAL_JOIN
		this.isCartesian = checkCartesian();
		if(! this.isCartesian){
			// Sort arg0 on those tupples defined by sort indexes
			IRelation rel0 = RELATION.getRelation(
					new SimpleIndexComparator(inds0));
			rel0.addAll(this.relation0);
			this.relation0 = rel0;
			
			// Sort arg1 on those tupples defined by sort indexes
			IRelation rel1 = RELATION.getRelation(
					new SimpleIndexComparator(inds1));
			rel1.addAll(this.relation1);
			this.relation1 = rel1;		
		}else{
			this.FORM = JoinForm.NATURAL_JOIN;
		}
		/*
		 * If project indexes are not specified, 
		 * joined tuples will be simple merged.
		 */
		// TODO: comment this out
		int a = this.getRelationArity();
		if (this.projectIndexes == null) {
			this.projectIndexes = this.transformIndexes0(a, new int[a]);
		}
	}

	public IRelation join() {
		/**
		 * Return an empty join relation either if empty input relation/s are empty
		 * or data types of relations which corresponds to join arguments do not match.
		 */
		if (this.relation0.size()==0 || relation1.size() == 0 || !checkDatatypes()) {
			return RELATION.getRelation(this.getRelationArity());
		}
		this.setJoinOperator();
		/**
		 * If true make Cartesian product (a special
		 * case of join operation), otherwise join
		 * regularly
		 */
		// TODO: put this together with case: NATURAL_JOIN
		if(this.isCartesian){
			return joinCartesian();
		}
		switch (this.FORM){
			case NATURAL_JOIN:
				return joinCartesian();
			case SEMIJOIN:
				return this.sortMergeJoin();
			case THETA_JOIN:
				return this.sortMergeJoin();
		}
		return null;
	}

	private IRelation joinCartesian() {
		IRelation joinRelation = RELATION.getRelation(this.getRelationArity());
		Concatenation concatenator = new Concatenation();
		Iterator<ITuple> it0, it1;
		ITuple t1, t0, concatenatedTuple;
		it0 = this.relation0.iterator();
		
		while(it0.hasNext()){
			t0 = it0.next();
			it1 = this.relation1.iterator();
			while(it1.hasNext()){
				t1 = it1.next();
				concatenatedTuple = concatenator.concatenate(
						t0, t1, this.projectIndexes);
						
				joinRelation.add(concatenatedTuple);
			}
		}
		return joinRelation;
	}
	
	/**
	 * the sort-merge join operation:
	 * (JoinSimpleExtended Processing in Relational Databases, 
	 * PRITI MISHRA and MARGARET H. EICH)
	 */
	private IRelation sortMergeJoin() {
		IRelation joinRelation = RELATION.getRelation(this.getRelationArity());
		Concatenation concatenator = new Concatenation();
		Iterator<ITuple> it0, it1;
		ITuple t1, t0, resTuple = null;
		int comp = 0;
		it0 = this.relation0.iterator();
		
		while(it0.hasNext()){
			t0 = it0.next();
			it1 = this.relation1.iterator();
			while(it1.hasNext()){
				t1 = it1.next();
				comp = checkJointness(t0, t1);
				comp = isConditionSatisfied(comp);
				if(comp == 0){
					switch (this.FORM){
					case SEMIJOIN:
						resTuple = t0;
						break;
					case THETA_JOIN:
						resTuple = concatenator.concatenate(
								t0, t1, this.projectIndexes);
						break;
					}
					joinRelation.add(resTuple);
				}else if(comp < 0) break;
			}
		}
		return joinRelation;
	}
	
	/**
	 * Transforms indexes according to the following examples: 
	 * [-1,-1, 0] to [-1,-1, 2]; 
	 * [-1, 1, 0] to [-1, 1, 2];
	 * [ 2, 1, 0] to [ 0, 1, 2].
	 * 
	 * @param a    relation arity
	 * @param inds join indexes
	 * @return     transformed join indexes
	 */
	private int[] transformIndexes0(int a, int[] inds) {
		int[] tranformedIndexes = new int[a];
		for (int i = 0; i < a; i++) {
			if (inds[i] != -1)
				tranformedIndexes[i] = i;
			else
				tranformedIndexes[i] = -1;
		}
		return tranformedIndexes;
	}
	
	/**
	 * Transforms indexes according to the following examples: [-1,-1, 0] to
	 * [0,-1,-1] [-1, 1, 0] to [0, 1,-1] [ 2, 1, 0] to [0, 1, 2]
	 * 
	 * @param a    relation arity
	 * @param inds join indexes
	 * @return     transformed join indexes
	 */
	private int[] transformIndexes1(int a, int[] inds) {
		int[] tranformedIndexes = new int[a];
		for (int i = 0; i < a; i++) {
			tranformedIndexes[i] = -1;
		}
		for (int i = 0; i < a; i++) {
			if (indexes[i] != -1) {
				tranformedIndexes[indexes[i]] = indexes[i];
			}
		}
		return tranformedIndexes;
	}
	
	private int getRelationArity(){
		int j=0;
		
		switch (this.FORM){
		case NATURAL_JOIN:
			j = this.relation0.getArity() + this.relation1.getArity();
			break;
		case SEMIJOIN:
			j = this.relation0.getArity();
			break;
		case THETA_JOIN:
			if(this.projectIndexes == null){
				return this.relation0.getArity() + this.relation1.getArity();
			}
			for(int i=0; i<this.projectIndexes.length; i++){
				if(this.projectIndexes[i] != -1) j++;
			}
			break;
		}
		return j;
	}

	/**
	 * @param inds join indexes
	 * @param a arity
	 * @return true for consistant join indexes (each index is 
	 * 	not bigger than the relation arity), otherwise false
	 */
	private boolean checkIndexes(int[] inds, int a){
		int j=0;
		for(int i=0; i<inds.length; i++){
			if(inds[i]+1 > a) return false;
		}
		return true;
	}
	
	private int checkJointness(ITuple t0, ITuple t1){
		int[] indexes = this.indexes;
		int comp = 0, tempComp0 = 0, tempComp1 = 0;
		
		if(t0 != null && t1 != null){
			for(int i=0; i<indexes.length; i++){
				if(indexes[i] != -1){
					comp = t0.getTerm(i).compareTo(t1.getTerm(indexes[i]));
					if(comp != 0){ 	
						tempComp0 = comp;
						if(tempComp0 > 0)  tempComp1 = tempComp0;
					}	
				}
			}
			if(tempComp1 != 0) return tempComp1;
			if(tempComp0 != 0) return tempComp0;
			return 0;
		}
		if(t0 != null && t1 == null)
			return 1;
		if(t0 == null && t1 != null)
			return -1;
		
		return 0;
	}
	
	/**
	 * Checks whether join indexes are Cartesian indexes
	 * 
	 * @return true if Cartesian Product will be applied,
	 * 		   as a special case of join operation, 
	 * 	       otherwise false
	 */
	private boolean checkCartesian(){
		for(int i=0; i<this.indexes.length; i++){
			if(indexes[i] != -1) return false;
		}
		return true;
	}
	
	/**
	 * Checks whether data types which corresponds to join arguments 
	 * (of joining relation) are of the same type. 
	 * 
	 * @return true if data types which corresponds to join arguments 
	 * 			are of the same type, otherwise false.
	 */
	private boolean checkDatatypes(){
		for(int i=0; i<this.indexes.length; i++){
			if(indexes[i] != -1){
				ITerm t0 = this.relation0.first().getTerm(i);
				ITerm t1 = this.relation1.first().getTerm(indexes[i]);
				if(! t0.getClass().equals(t1.getClass()))
				return false;
			}
		}
		return true;
	}
	
	/**
	 * if boolean order = true then 
	 * isConditionSatisfied = 
	 *  0	    means join condition is satisfied for the two compared tupels
	 *  -1 or 1	means join condition is not satisfied for the two compared tupels
	 *  
	 * @param comparison
	 * @return
	 */
	private int isConditionSatisfied(int comparison){
		switch (CONDITION){
			case EQUALS:
				if(comparison == 0) return 0;
				if(comparison < 0) return -1;
				break;
			case NOT_EQUAL:
				if(comparison != 0) return 0;
				else return 1;
			case LESS_THAN:
				if(comparison < 0) return 0;
				if(comparison >= 0) return 1;
				break;
			case GREATER_THAN:
				if(comparison > 0) return 0;
				if(comparison <= 0) return -1;
				break;
			case LESS_OR_EQUAL:
				if(comparison <= 0) return 0;
				if(comparison > 0) return 1;
				break;
			case GREATER_OR_EQUAL:
				if(comparison >= 0) return 0;
				if(comparison < 0) return -1;
				break;
		}
		return 1;
	}
	
	/**
	 * <p>Creates a default join indexes.</p>
	 *  
	 * @param arity	Arity of the default join indexes.
	 * @return		The default join indexes.
	 */
	public static int[] getInitIndexes(int arity) {
		int[] i = new int[arity];
		for (int j = 0; j < arity; j++){
			i[j] = -1;
		}
		return i;
	}
}
