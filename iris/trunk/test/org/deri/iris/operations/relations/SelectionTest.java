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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;
import org.deri.iris.api.storage_old.IRelation;

/**  
 * <p>
 * </p>
 * @author Darko Anicic, DERI Innsbruck
 * @date 11.06.2007 10:42:40
 */
public class SelectionTest extends TestCase {
	static IMixedDatatypeRelation relation = null;
	
	private static final ITuple[] tups = new ITuple[]{
		BASIC.createTuple(CONCRETE.createInteger(1), TERM.createString("b"), CONCRETE.createIri("http://aaa"), CONCRETE.createDouble(2d)), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com"), CONCRETE.createDouble(10d), CONCRETE.createInteger(8)), 
		BASIC.createTuple(CONCRETE.createInteger(2), TERM.createString("a"), CONCRETE.createIri("http://bbb"), CONCRETE.createDouble(1d)), 
		BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createInteger(3), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)), 
		BASIC.createTuple(TERM.createString("zzzzab"), CONCRETE.createInteger(4), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)),
		BASIC.createTuple(TERM.createString("aaa"), CONCRETE.createInteger(4), TERM.createString("aaa"), CONCRETE.createInteger(4)),
		BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(4), CONCRETE.createInteger(4), CONCRETE.createInteger(4)),
		BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(6), CONCRETE.createInteger(4), CONCRETE.createInteger(4))
	};
	
	public static Test suite() {
		return new TestSuite(SelectionTest.class, SelectionTest.class.getSimpleName());
	}

	private static void setRelation(){
		relation = RELATION.getMixedRelation(4);
		relation.addAll(Arrays.asList(tups));
	}
	
	/**
	 * Checks out select(IMixedDatatypeRelation relation, ITuple pattern);
	 */
	public void runSelection_0(final ITuple p, final Collection<ITuple> e) {
		setRelation();
		ISelection selectionOperator 
			= new GeneralSelection(relation, p);
			
		IRelation result = selectionOperator.select();
		assertResults(result, e);
	}
	
	/**
	 * Checks out select(IMixedDatatypeRelation relation, int[] indexes);
	 */
	protected static void runSelection_1(final int[] inds, final Collection<ITuple> e) {
		setRelation();
		ISelection selectionOperator 
			= new GeneralSelection(relation, inds);
		
		IRelation result = selectionOperator.select();
		assertResults(result, e);
	}
	
	/**
	 * Checks out select(IMixedDatatypeRelation relation, ITuple pattern, int[] indexes);
	 */
	protected static void runSelection_2(final ITuple p, final int[] indexes, 
			final Collection<ITuple> e) {
			
		setRelation();
		ISelection selectionOperator 
			= new GeneralSelection(relation, p, indexes);
	
		IRelation result = selectionOperator.select();
		assertResults(result, e);
	}
	
	public void testSelect_0() {
		ITuple selTuple = BASIC.createTuple(
				TERM.createString("zzzz"), null, null, null);
				
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createIri("http://www.google.com"), CONCRETE.createDouble(10d), CONCRETE.createInteger(8)));
		e.add(BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createInteger(3), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)));
		
		runSelection_0(selTuple, e);
	}
	
	public void testSelect_1() {
		ITuple selTuple = BASIC.createTuple(
				TERM.createString("zzzz"), null, null, CONCRETE.createInteger(10));
		
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(TERM.createString("zzzz"), CONCRETE.createInteger(3), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)));
		
		runSelection_0(selTuple, e);
	}
	
	public void testSelect_2() {
		ITuple selTuple = BASIC.createTuple(
				null, TERM.createString("b"), null, null);
		
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(CONCRETE.createInteger(1), TERM.createString("b"), CONCRETE.createIri("http://aaa"), CONCRETE.createDouble(2d)));
		
		runSelection_0(selTuple, e);
	}
	
	public void testSelect_3() {
		ITuple selTuple = BASIC.createTuple(
				null, CONCRETE.createInteger(4), null, null);
		
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(TERM.createString("zzzzab"), CONCRETE.createInteger(4), CONCRETE.createDouble(5d), CONCRETE.createInteger(10)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(4), CONCRETE.createInteger(4), CONCRETE.createInteger(4)));
		e.add(BASIC.createTuple(TERM.createString("aaa"), CONCRETE.createInteger(4), TERM.createString("aaa"), CONCRETE.createInteger(4)));
		
		runSelection_0(selTuple, e);
	}
	
	public void testSelect_4() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(TERM.createString("aaa"), CONCRETE.createInteger(4), TERM.createString("aaa"), CONCRETE.createInteger(4)));
		e.add(BASIC.createTuple(CONCRETE.createInteger(4), CONCRETE.createInteger(4), CONCRETE.createInteger(4), CONCRETE.createInteger(4)));
		
		runSelection_1(new int[]{1,2,1,2}, e);
	}
	
	public void testSelect_5() {
		ITuple selTuple = BASIC.createTuple(
				TERM.createString("aaa"), null, null, null);
		
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(BASIC.createTuple(TERM.createString("aaa"), CONCRETE.createInteger(4), TERM.createString("aaa"), CONCRETE.createInteger(4)));
		
		runSelection_2(selTuple, new int[]{1,2,1,2}, e);
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
	protected static void assertResults(final IRelation r,
			final Collection<ITuple> e) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", e.size(), r.size());
		Assert.assertTrue("The relation must contain all expected tuples", r
				.containsAll(e));
	}
}
