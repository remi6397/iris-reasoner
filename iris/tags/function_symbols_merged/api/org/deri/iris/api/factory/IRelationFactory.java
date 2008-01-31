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

import org.deri.iris.api.storage_old.IMixedDatatypeRelation;
import org.deri.iris.api.storage_old.IRelation;

/**
 * <p>
 * The factory interface to obtain relations.
 * </p>
 * <p>
 * $Id: IRelationFactory.java,v 1.12 2007-07-10 10:00:33 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.12 $
 */
public interface IRelationFactory {

	/**
	 * Creates a new relation with a given arity.
	 * @param a the arity of the relation to create
	 * @return the newly created relation
	 * @throws IllegalArgumentException if the arity is negative
	 */
	public abstract IRelation getRelation(final int a);

	/**
	 * Creates a new relation which accepts all datatypes at any column.
	 * @param a the arity of the relation to create
	 * @return the newly created relation
	 * @throws IllegalArgumentException if the arity is negative
	 */
	public abstract IMixedDatatypeRelation getMixedRelation(final int a);
}
