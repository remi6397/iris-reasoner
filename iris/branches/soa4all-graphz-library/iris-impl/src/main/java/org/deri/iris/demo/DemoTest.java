/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.demo;
//
//import junit.framework.TestCase;
//import org.deri.iris.KnowledgeBaseFactory;
//
//public class DemoTest extends TestCase
//{
//	public void testSimpleProgram() throws Exception
//	{
//    	String program = 
//		    "p(1)." +
//		    "p(2)." +
//		    
//		    "q(?X) :- p(?X)." +
//		    "?- q(?x).";
//    	
//    	helperExecuteProgram( program );
//	}
//
//	public void testEmptyProgram() throws Exception
//	{
//    	helperExecuteProgram( "" );
//	}
//	
//	public void testProgramWithoutQuery() throws Exception
//	{
//    	String program = 
//		    "p(1)." +
//		    "p(2)." +
//		    
//		    "q(?X) :- p(?X).";
//    
//    	helperExecuteProgram( program );
//	}
//
//	public void testProgramwithTwoQueries() throws Exception
//	{
//    	String program = 
//		    "p(1)." +
//		    "p(2)." +
//		    
//		    "q(?X) :- p(?X)." +
//		    "?- p(?x)." +
//    		"?- q(?x).";
//    	
//    	helperExecuteProgram( program );
//	}
//
//	private void helperExecuteProgram( String program ) throws Exception
//	{
//    	Demo.main( new String[]{ Demo.PROGRAM + "=" + program, Demo.UNSAFE_RULES, Demo.WELL_FOUNDED } );
//    	Demo.execute( program, KnowledgeBaseFactory.getDefaultConfiguration() );
//	}
//}
