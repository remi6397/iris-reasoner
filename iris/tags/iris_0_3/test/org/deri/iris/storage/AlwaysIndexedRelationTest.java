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
package org.deri.iris.storage;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests the Allways indexed relation.
 * </p>
 * <p>
 * $Id: AlwaysIndexedRelationTest.java,v 1.1 2007-02-16 09:02:38 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.1 $
 */
public class AlwaysIndexedRelationTest extends
		GenericRelationTest<AlwaysIndexedRelation> {

	public static Test suite() {
		return new TestSuite(AlwaysIndexedRelationTest.class,
				AlwaysIndexedRelationTest.class.getSimpleName());
	}

	@Override
	public void setUp() {
		r = new AlwaysIndexedRelation();
	}
}
