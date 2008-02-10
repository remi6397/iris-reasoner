package org.deri.iris;

import junit.framework.TestCase;

public class DemoTest extends TestCase
{
	public static String TIMEOUT = "10000";	// Milliseconds
	
	public static String EVALUATION = "2";	// Semi-naive
	
	public void testSimpleProgram()
	{
    	String program = 
		    "p(1)." +
		    "p(2)." +
		    
		    "q(?X) :- p(?X)." +
		    "?- q(?x).";
    	
    	String args[] = new String[] { program, EVALUATION, TIMEOUT };

    	Demo.main( args );
	}

	public void testEmptyProgram()
	{
    	String args[] = new String[] { "", EVALUATION, TIMEOUT };

    	Demo.main( args );
	}
	
	public void testProgramWithoutQuery()
	{
    	String program = 
		    "p(1)." +
		    "p(2)." +
		    
		    "q(?X) :- p(?X).";
    	
    	String args[] = new String[] { program, EVALUATION, TIMEOUT };

    	Demo.main( args );
	}

}
