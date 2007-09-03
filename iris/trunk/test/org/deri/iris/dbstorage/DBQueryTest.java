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
package org.deri.iris.dbstorage;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.factory.IBasicFactory;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.basics.BasicFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author federico
 *
 */

// NB: the db has to be filled before the test!

public class DBQueryTest extends TestCase {

	private static final int ARITY = 2;

	private static final String SYMBOL = "wsml-member-of";
	
	private static IBasicFactory bFactory=BasicFactory.getInstance();

	public static Test suite() {
		return new TestSuite(DBQueryTest.class, DBQueryTest.class
				.getSimpleName());
	}

	public void testBasic() throws DbStorageManagerException {
		DbStorageManager dbm= new DbStorageManager("conf1.properties");
		String tableName=dbm.getTable(SYMBOL, ARITY);
		if (tableName==null) return;
		IPredicate p=bFactory.createPredicate(SYMBOL, ARITY);
		String sqlQuery="SELECT term1, termType1, term2, termType2 FROM "+tableName+";";
		IMixedDatatypeRelation rel=dbm.computeIMixedDatatypeRelation(p, sqlQuery);
		System.out.println(rel.toString());
	}

}
