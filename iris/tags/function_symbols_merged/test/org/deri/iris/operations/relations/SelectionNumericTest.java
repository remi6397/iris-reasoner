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

import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.RELATION_OPERATION;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.MiscHelper;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;


/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   20.09.2006 11:30:30
 */
public class SelectionNumericTest extends TestCase {
	static IMixedDatatypeRelation relation = null;
	
	public static Test suite() {
		return new TestSuite(SelectionNumericTest.class, SelectionNumericTest.class.getSimpleName());
	}
	
	private static void setNumRelation(){
		relation = RELATION.getMixedRelation(4);
	
		relation.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(2.3),
				CONCRETE.createDecimal(3.3),
				CONCRETE.createDecimal(4.3)));
		relation.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(5.3),
				CONCRETE.createDecimal(9.3),
				CONCRETE.createDecimal(9.3)));
		relation.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(7.3),
				CONCRETE.createDecimal(9.3),
				CONCRETE.createDecimal(9.3)));
	}
	
	/**
	 * Checks out createSelectionOperator(IMixedDatatypeRelation relation, ITuple pattern);
	 * 
	 * Also see:
	 * (non-Javadoc)
	 * @see org.deri.iris.api.factory.IRelationOperationsFactory#createSelectionOperator(IMixedDatatypeRelation relation, ITuple pattern)
	 */
	protected static void runSelection_0(final ITuple p, 
			final Collection<ITuple> e) {
		
		setNumRelation();
		ISelection selectionOperator 
			= RELATION_OPERATION.createSelectionOperator(relation, p);
			
		IMixedDatatypeRelation result = selectionOperator.select();
		assertResults(result, e);
	}
	
	/**
	 * Checks out createSelectionOperator(IMixedDatatypeRelation relation, int[] indexes);
	 * 
	 * Also see:
	 * (non-Javadoc)
	 * @see org.deri.iris.api.factory.IRelationOperationsFactory#createSelectionOperator(IMixedDatatypeRelation relation, int[] indexes)
	 */
	protected static void runSelection_1(final int[] indexes, 
			final Collection<ITuple> e) {
			
		setNumRelation();
		ISelection selectionOperator 
			= RELATION_OPERATION.createSelectionOperator(relation, indexes);
			
		IMixedDatatypeRelation result = selectionOperator.select();
		assertResults(result, e);
	}
	
	/**
	 * Checks out createSelectionOperator(IMixedDatatypeRelation relation, int[] indexes);
	 * 
	 * Also see:
	 * (non-Javadoc)
	 * @see org.deri.iris.api.factory.IRelationOperationsFactory#createSelectionOperator(IMixedDatatypeRelation relation, ITuple pattern, int[] indexes)
	 */
	protected static void runSelection_2(final ITuple p, final int[] indexes, 
			final Collection<ITuple> e) {
			
		setNumRelation();
		ISelection selectionOperator 
			= RELATION_OPERATION.createSelectionOperator(relation, p, indexes);
			
		IMixedDatatypeRelation result = selectionOperator.select();
		assertResults(result, e);
	}
	
	public void testSelect_1n9n_0() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(5.3),
				CONCRETE.createDecimal(9.3),
				CONCRETE.createDecimal(9.3)));
		e.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(7.3),
				CONCRETE.createDecimal(9.3),
				CONCRETE.createDecimal(9.3)));
		
		runSelection_0(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				null, 
				CONCRETE.createDecimal(9.3), 
				null), e);
	}
	
	public void testSelect_1234_0() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(2.3),
				CONCRETE.createDecimal(3.3),
				CONCRETE.createDecimal(4.3)));
		
		runSelection_0(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(2.3),
				CONCRETE.createDecimal(3.3), 
				CONCRETE.createDecimal(4.3)), e);
	}
	
	/**
	 * First and second tuple have to be different and
	 * third and fourth have to be the same ones.
	 */
	public void testSelect_m1m1p1p1_1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(5.3),
				CONCRETE.createDecimal(9.3),
				CONCRETE.createDecimal(9.3)));
		e.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(7.3),
				CONCRETE.createDecimal(9.3),
				CONCRETE.createDecimal(9.3)));
		
		runSelection_1(new int[]{-1, -1, 1, 1}, e);
	}
	
	/**
	 * First tuple has to have value: 1.3 and
	 * first and second tuple have to be different and
	 * third and fourth have to be the same ones.
	 */
	public void testSelect_1nnn_nn11_1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(5.3),
				CONCRETE.createDecimal(9.3),
				CONCRETE.createDecimal(9.3)));
		e.add(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				CONCRETE.createDecimal(7.3),
				CONCRETE.createDecimal(9.3),
				CONCRETE.createDecimal(9.3)));
		
		runSelection_2(MiscHelper.createTuple(
				CONCRETE.createDecimal(1.3),
				null,
				null, 
				null),
				new int[]{-1, -1, 1, 1}, e);
	}
	
	/**
	 * Tests the relation against a list of tuples using the assert methods
	 * of JUnit. The length of the relation and the list must be equal, 
	 * and the relation must contain all tuples of the list.
	 * 
	 * @param r
	 *            the relation to check
	 * @param e
	 *            the Collection containing all expected tuples
	 */
	protected static void assertResults(final IMixedDatatypeRelation r,
			final Collection<ITuple> e) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", e.size(), r.size());
		Assert.assertTrue("The relation must contain all expected tuples", r
				.containsAll(e));
	}
}

