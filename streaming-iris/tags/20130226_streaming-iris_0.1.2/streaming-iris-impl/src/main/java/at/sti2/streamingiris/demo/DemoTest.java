package at.sti2.streamingiris.demo;

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
// }
