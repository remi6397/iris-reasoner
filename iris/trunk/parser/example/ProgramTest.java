package example;

import java.util.Map;

import org.deri.iris.Executor;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.IResultSet;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserImpl;
import org.deri.iris.evaluation.algebra.ExpressionEvaluator;
import org.deri.iris.factory.Factory;

public class ProgramTest {
	public static void main(String[] args) throws Exception {
		ProgramTest i = new ProgramTest();
      
		//i.evluateAndPrint(i.test1());
		//i.evluateAndPrint(i.test2());
		//i.evluateAndPrint(i.test3());
		i.evluateAndPrint(i.test4());
    }

    public String test1(){
    	String program = 
		    "k('d', 'e')." +
		    "k('d', 'g')." +
		    "l('d')." +
		    
		    "w(?Y) :- k(?X, ?Y), l(?X)." +
		    "?- w(?X).";
    	return program;
    }
    
    public String test2(){
    	String program = 
 			"s('d')." +
		    "s('b')." +
		    "s('a')." +
		    
		    "r('d')." +
		    "r('c')." +
		    
		    "p('b')." +
		    "p('e')." +
		    
		    "q(?X) :- s(?X), not p(?X)." +
		    "p(?X) :- r(?X)." +
		    "?- q(?X).";
  	
    	return program;
    }
    
    public String test3(){
    	String program = 
   		
 			"s('d')." +
		    "s('b')." +
		    "s('a')." +
		    
		    "r('d')." +
		    
		    "p('b')." +
		    
		    "p(?X) :- r(?X)." +
		    // TODO: UnEqual built-in doesn't work currently
		    "w(?X) :- s(?X), p(?X), ?X='d'." +
		    "?- w(?X).";
   	
  	
    	return program;
    }
    
    public String test4(){
    	String program = 
		    "edge('a', 'b')." +
		    "path('b', 'c')." +
		    "edge('c', 'd')." +
		    
		    "path(?X, ?Y) :- edge(?X, ?Y)." +	
		    "path(?X, ?Y) :- path(?X, ?Z), edge(?Z, ?Y)." +
		    "?- path(?X, ?Y).";
  	
    	return program;
    }
    
    private void evluateAndPrint(String program) throws Exception{
    	Parser pa = new ParserImpl();
    	IProgram p = Factory.PROGRAM.createProgram();
    	pa.compileKB(program, p);
   		IExpressionEvaluator method = new ExpressionEvaluator();
		IExecutor exec = new Executor(p, method);
		exec.execute();
		IResultSet r = exec.getResultSet();
		
		Map<IPredicate, IRelation> m = r.getResults(); 
		for(IPredicate pr : m.keySet()){
			System.out.println(pr.toString());
			for(ITuple t : m.get(pr)){
				System.out.println(t.toString());
			}
    	}
    }
}