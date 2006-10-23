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
package org.deri.iris.evaluation.seminaive;

import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;
import org.deri.iris.api.IEDB;
import org.deri.iris.exception.DataModelException;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.evaluation.seminaive.model.ITree;

import java.util.Map;
import java.util.HashMap;

/**
 * Algorithm 3.3: Evaluation of Datalog Equations
 * 
 * INPUT: A collection of datalog rules with EDB predicates r1,...rk and
 * IDB predicates p1,...,pm. A list of relations R1, ..., Rk to serve as 
 * values of the EDB predicates.
 * 
 * OUTPUT: The least fixed point solution to the datalog equations obtained 
 * from these rules.
 * 
 * for i:=1 to m do
 *    Pi := 0;
 * repeat
 *    for i:= 1 to m do
 *       Qi := Pi; // save old values of Pi's
 *    for i := 1 to m do
 *       Pi := EVAL(pi, R1, ..., Rk, Q1,..., Qm);
 * until Pi = Qi for all i, 1 <= i <= m;
 * output Pi's 
 * 
 * @author Paco Garcia, University of Murcia
 * @date 01-sep-2006
 */
public class NaiveEvaluation extends GeneralSeminaiveEvaluation{

	NaiveEvaluation(IEvaluationProcedure e, IEDB EDB, Map<ITree, ITree> IDB) {
		super(e, EDB, IDB);
	}
	
	public Map<ITree, IRelation> evaluate() throws DataModelException {
		/*
		 * Input: IDB --> pi = ITree; Relevants Rs for each IDB are the leaves
		 * of the ITree
		 */
		Map<ITree, IRelation> P = new HashMap<ITree, IRelation>();
		Map<ITree, IRelation> Q = new HashMap<ITree, IRelation>();
		
		for (ITree head: IDB.keySet())
		{
			int arity = head.getArity();
			P.put(head, new Relation(arity));
			Q.put(head, new Relation(arity));
		}
		
		// 1st iteration
		for (ITree head: IDB.keySet())
		{
			// EVAL (pi, R1,..., Rk, Q1,..., Qm);
			P.put(head, method.eval(IDB.get(head), EDB, Q));
		}
		
		for (; !compare(P, Q);) {
			addRelations(P, Q);

			for (ITree head: IDB.keySet())
			{
				// EVAL (pi, R1,..., Rk, Q1,..., Qm);
				P.put(head, method.eval(IDB.get(head), EDB, Q));
			}			
		}
		return P;
		
	}
}
