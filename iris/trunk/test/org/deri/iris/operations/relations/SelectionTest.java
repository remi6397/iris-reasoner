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

import org.deri.iris.MiscHelper;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.basics.Tuple;
import org.deri.iris.storage.Relation;


/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.200611:34:30
 */
public class SelectionTest {

	public SelectionTest() {
		super();
	}

	public static void main(String[] args) throws Exception {
		SelectionTest i = new SelectionTest();
		i.test();
	}
	
	public void test() throws Exception {
		IRelation relation0 = new Relation(3);
		
		relation0.add(MiscHelper.createTuple("b","b","b"));
		relation0.add(MiscHelper.createTuple("a","c","c"));
		relation0.add(MiscHelper.createTuple("b","d","a"));
		relation0.add(MiscHelper.createTuple("c","a","b"));
		relation0.add(MiscHelper.createTuple("d","a","b"));
		
		Iterator i = relation0.iterator();
		while(i.hasNext()){
			System.out.println("relation0: " + ((ITuple)i.next()).toString());
		}

		ITuple pattern = MiscHelper.createTuple(null, null, "b");
		//ITuple pattern = MiscHelper.createTuple("b", "d", "a");
		
		ISelection selection = RELATION.createSelectionOperator(
				relation0, pattern);
		IRelation result = selection.select();
		
		i = result.iterator();
		while(i.hasNext()){
			System.out.println("result: " + i.next());
		}
	}
}
