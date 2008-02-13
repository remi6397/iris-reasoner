package org.deri.iris;

import junit.framework.TestCase;

public class DemoTest extends TestCase
{
	public void testSimpleProgram() throws Exception
	{
    	String program = 
		    "p(1)." +
		    "p(2)." +
		    
		    "q(?X) :- p(?X)." +
		    "?- q(?x).";
    	
    	helperExecuteProgram( program );
	}

	public void testEmptyProgram() throws Exception
	{
    	helperExecuteProgram( "" );
	}
	
	public void testProgramWithoutQuery() throws Exception
	{
    	String program = 
		    "p(1)." +
		    "p(2)." +
		    
		    "q(?X) :- p(?X).";
    
    	helperExecuteProgram( program );
	}

	private void helperExecuteProgram( String program ) throws Exception
	{
    	Demo.main( new String[]{ program, "2", "1000" } );
    	Demo.executeProgram( program, 2 );
	}
}
