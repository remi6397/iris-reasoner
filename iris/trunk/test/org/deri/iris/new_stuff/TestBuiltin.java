/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2007 Digital Enterprise Research Institute (DERI), 
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
package org.deri.iris.new_stuff;

import junit.framework.TestCase;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.AddBuiltin;
import org.deri.iris.new_stuff.storage.IRelation;
import org.deri.iris.new_stuff.storage.simple.SimpleRelation;
import org.deri.iris.new_stuff.storage.simple.SimpleRelationFactory;
import org.deri.iris.rules.compiler.Builtin;
import org.deri.iris.rules.compiler.View;

public class TestBuiltin extends TestCase
{

	protected void setUp() throws Exception
	{
	}

	public void testHasInputAllBound() throws Exception
	{
		Configuration configuration = new Configuration();
		IRelation inputRelation = configuration.relationFactory.createRelation();
		
		inputRelation.add( Helper.createTuple( 1, 2, 3 ) );
		inputRelation.add( Helper.createTuple( 4, 4, 8 ) );
		inputRelation.add( Helper.createTuple( 1, 2, 4 ) );
		inputRelation.add( Helper.createTuple( 1, 3, 3 ) );
		
		ITuple criteria = Helper.createTuple( "X", "Y", "Z" );
		View view = new View( inputRelation, criteria, configuration.relationFactory );
		
		IBuiltInAtom builtinPredicate = new AddBuiltin( criteria.toArray( new ITerm[0]) );
		
		Builtin builtin = new Builtin( view.variables(), builtinPredicate, true, configuration );
		
		IRelation result = builtin.process( view.getView() );
		
		assertEquals( 2, result.size() );
	}

	public void testTooManyUnbound()
	{
		Configuration configuration = new Configuration();
		IRelation inputRelation = configuration.relationFactory.createRelation();
		ITuple criteria = Helper.createTuple( "X" );
		View view = new View( inputRelation, criteria, configuration.relationFactory );
		
		ITuple builtinTuple = Helper.createTuple( "X", "Y", "Z" );
		IBuiltInAtom builtinPredicate = new AddBuiltin( builtinTuple.toArray( new ITerm[0]) );
		
		try
		{
			new Builtin( view.variables(), builtinPredicate, true, configuration );
			fail( "Builtin should have thrown an exception" );
		}
		catch( Exception e ) // TODO Choose the proper exception type later
		{
		}
	}

	public void testHasInputOneUnbound() throws Exception
	{
		Configuration configuration = new Configuration();
		IRelation inputRelation = configuration.relationFactory.createRelation();
		
		inputRelation.add( Helper.createTuple( 1, 2 ) );
		inputRelation.add( Helper.createTuple( 4, 4 ) );
		inputRelation.add( Helper.createTuple( 1, 2 ) );
		inputRelation.add( Helper.createTuple( 1, 3 ) );
		
		ITuple criteria = Helper.createTuple( "X", "Y" );
		View view = new View( inputRelation, criteria, configuration.relationFactory );
		

		ITuple builtinTuple = Helper.createTuple( "X", "Y", "Z" );
		IBuiltInAtom builtinPredicate = new AddBuiltin( builtinTuple.toArray( new ITerm[0]) );
		
		Builtin builtin = new Builtin( view.variables(), builtinPredicate, true, configuration );
		
		IRelation output = builtin.process( view.getView() );
		
		assertEquals( 3, output.size() );
		assertEquals( Helper.createTerm( "X" ), builtin.getOutputVariables().get( 0 ) );
		assertEquals( Helper.createTerm( "Y" ), builtin.getOutputVariables().get( 1 ) );
		assertEquals( Helper.createTerm( "Z" ), builtin.getOutputVariables().get( 2 ) );
	}

	public void testNoInput() throws Exception
	{
		Configuration configuration = new Configuration();
		ITuple builtinTuple = Helper.createTuple( 3, 4, "X" );
		IBuiltInAtom builtinPredicate = new AddBuiltin( builtinTuple.toArray( new ITerm[0]) );
		
		Builtin builtin = new Builtin( null, builtinPredicate, true, configuration );
		
		IRelation output = builtin.process( null );
		
		assertEquals( Helper.createTerm( "X" ), builtin.getOutputVariables().get( 0 ) );

		assertEquals( 1, output.size() );
		assertEquals( Helper.createTerm( 7 ), output.get( 0 ).get( 0 ) );
	}

}
