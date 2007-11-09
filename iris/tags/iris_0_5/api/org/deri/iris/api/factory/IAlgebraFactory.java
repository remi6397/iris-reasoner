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
package org.deri.iris.api.factory;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.evaluation.algebra.IBuiltinDescriptor;
import org.deri.iris.api.evaluation.algebra.IConstantDescriptor;
import org.deri.iris.api.evaluation.algebra.IDifferenceDescriptor;
import org.deri.iris.api.evaluation.algebra.IJoinDescriptor;
import org.deri.iris.api.evaluation.algebra.IProjectionDescriptor;
import org.deri.iris.api.evaluation.algebra.IRelationDescriptor;
import org.deri.iris.api.evaluation.algebra.ISelectionDescriptor;
import org.deri.iris.api.evaluation.algebra.IUnionDescriptor;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.operations.relations.JoinCondition;

/**
 * <p>
 * An interface that can be used to obtain references to set of 
 * descriptors for relational algebra operations.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 13.12.2006 17:212:34
 */
public interface IAlgebraFactory {
	
	public IConstantDescriptor createConstantDescriptor(
			final ITerm c, final IVariable v);
	
	public IDifferenceDescriptor createDifferenceDescriptor();
	
	public IJoinDescriptor createJoinDescriptor(final JoinCondition c);
	
	public IRelationDescriptor createRelationDescriptor(final boolean isPositive, 
			final IPredicate p);
	
	public IProjectionDescriptor createProjectionDescriptor(final int[] i);
	
	public ISelectionDescriptor createSelectionDescriptor(final int[] i);
	
	public ISelectionDescriptor createSelectionDescriptor(final ITuple p);
	
	public ISelectionDescriptor createSelectionDescriptor(
			final ITuple p, final int[] i);
	
	public IUnionDescriptor createUnionDescriptor();
	
	public IBuiltinDescriptor createBuiltinDescriptor(boolean isPositive, IBuiltInAtom builtin);
}
