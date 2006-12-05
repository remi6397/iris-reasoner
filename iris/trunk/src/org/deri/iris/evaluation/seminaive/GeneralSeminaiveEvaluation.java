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

import static org.deri.iris.factory.Factory.EVALUATION;

import java.util.Map;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.evaluation.IEvaluator;
import org.deri.iris.api.evaluation.IResultSet;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.exception.DataModelException;

/**
 * 
 * @author Paco Garcia, University of Murcia
 * @author Darko Anicic, DERI Innsbruck
 * @date 08-sep-2006
 */
public abstract class GeneralSeminaiveEvaluation implements IEvaluator {
	protected IEvaluationProcedure method;

	protected IProgram edb;

	protected Map<IPredicate, ITree> idb;

	protected Map<IPredicate, ITree> queries;

	private IResultSet results = null;

	GeneralSeminaiveEvaluation(IEvaluationProcedure e, IProgram edb,
			Map<IPredicate, ITree> idb, Map<IPredicate, ITree> q) {

		this.method = e;
		this.edb = edb;
		this.idb = idb;
		this.queries = q;
		this.results = EVALUATION.createResultSet();
	}

	public abstract boolean evaluate() throws DataModelException;

	public IResultSet getResultSet() {
		return this.results;
	}
}
