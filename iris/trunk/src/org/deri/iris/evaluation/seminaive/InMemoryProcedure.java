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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.evaluation.seminaive.model.Component;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.evaluation.seminaive.model.DifferenceDescription;
import org.deri.iris.evaluation.seminaive.model.JoinDescription;
import org.deri.iris.evaluation.seminaive.model.ProjectionDescription;
import org.deri.iris.evaluation.seminaive.model.RuleDescription;
import org.deri.iris.evaluation.seminaive.model.SelectionDescription;
import org.deri.iris.evaluation.seminaive.model.UnionDescription;
import org.deri.iris.factory.Factory;

/**
 * @author Paco Garcia, University of Murcia
 * @author Darko Anicic, DERI Innsbruck
 * @date 08-sep-2006
 */
public class InMemoryProcedure implements IEvaluationProcedure {

	private ITree t;

	private IProgram edb;

	private Complementor complementor = null;

	public InMemoryProcedure(ITree t, IProgram edb) {
		this.t = t;
		this.edb = edb;
		this.complementor = new Complementor(this.edb);
	}

	/**
	 * 
	 * @param t
	 *            Rule to evaluate
	 * @param edb
	 *            Extensional Database tuples
	 * @return new tuples discovered for the rule evaluated
	 */
	public IRelation eval(ITree t, IProgram edb) {
		return evaluate(t, edb, null);
	}

	/**
	 * 
	 * @param t
	 *            Rule to evaluate
	 * @param edb
	 *            Extensional Database tuples
	 * @param AQ
	 *            Tuples discovered during the last iteration
	 * @return new tuples discovered for the rule evaluated
	 */
	public IRelation eval_incr(ITree t, IProgram edb, Map<IPredicate, IRelation> AQ) {

		return evaluate(t, edb, AQ);
	}

	private IRelation evaluate(ITree node, IProgram edb,
			Map<IPredicate, IRelation> AQ) {
		if (node instanceof DifferenceDescription) {
			DifferenceDescription d = (DifferenceDescription) node;
			IDifference diff = Factory.RELATION_OPERATION
					.createDifferenceOperator(evaluate((ITree) d.getChildren()
							.get(0), edb, AQ), evaluate((ITree) d.getChildren()
							.get(1), edb, AQ));
			return diff.difference();
		} else if (node instanceof JoinDescription) {
			JoinDescription j = (JoinDescription) node;
			IJoin join = Factory.RELATION_OPERATION.createJoinSimpleOperator(
					evaluate((ITree) j.getChildren().get(0), edb, AQ),
					evaluate((ITree) j.getChildren().get(1), edb, AQ), j
							.getIndexes(), j.getCondition(), j
							.getProjectIndexes());
			return join.join();
		} else if (node instanceof ProjectionDescription) {
			ProjectionDescription p = (ProjectionDescription) node;
			ITree t = (ITree) p.getChildren().get(0);

			// Case: projection just after join can be done in one step
			// (using join operation together with projectIndexes).
			if (t instanceof JoinDescription) {
				JoinDescription j = (JoinDescription) t;
				t = (JoinDescription) Factory.SEMINAIVE_MODEL.createJoin(j, p
						.getIndexes());

				return evaluate(t, edb, AQ);
			}
			IProjection projection = Factory.RELATION_OPERATION
					.createProjectionOperator(evaluate(t, edb, AQ), p
							.getIndexes());
			return projection.project();
		} else if (node instanceof RuleDescription) {
			RuleDescription r = (RuleDescription) node;
			if (r.isPositive()) {
				IPredicate pr = Factory.BASIC.createPredicate(r.getName(), r
						.getArity());

				// Return tuples from the last iteration only
				if (AQ != null && AQ.get(pr) != null) {
					return AQ.get(pr);
				} else {
					// Return all tuples from the KB
					return edb.getFacts(pr);
				}
			} else {
				return this.complementor.getComplement(Factory.BASIC
						.createPredicate(r.getName(), r.getArity()));
			}
		} else if (node instanceof SelectionDescription) {
			SelectionDescription s = (SelectionDescription) node;
			ISelection selection = Factory.RELATION_OPERATION
					.createSelectionOperator(evaluate((ITree) s.getChildren()
							.get(0), edb, AQ), s.getPattern(), s.getIndexes());
			return selection.select();
		} else if (node instanceof UnionDescription) {
			List<Component> ul = node.getChildren();
			List<IRelation> ulr = Collections.synchronizedList(
					new ArrayList<IRelation>());
			ITree t = null;

			for (Component c : ul) {
				if (c instanceof ITree)
					t = (ITree) c;
				ulr.add(evaluate(t, edb, AQ));
			}
			IUnion union = Factory.RELATION_OPERATION.createUnionOperator(ulr);
			return union.union();
		} else
			return null;
	}
}