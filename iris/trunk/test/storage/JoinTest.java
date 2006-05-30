package storage;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.basics.Tuple;
import org.deri.iris.operations.relations.Join;
import org.deri.iris.storage.Relation;
import org.deri.iris.terms.Term;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.05.2006 13:59:43
 */
public class JoinTest {

	public JoinTest() {
		super();
	}

	public static void main(String[] args) throws Exception {
		JoinTest i = new JoinTest();
		i.test();
	}
	
	@SuppressWarnings("unchecked")
	public void test() throws Exception {
		IRelation relation0 = new Relation(3);
		IRelation relation1 = new Relation(3);
		
		relation0.add(this.createTuple("a","a","a"));
		relation0.add(this.createTuple("a","b","a"));
		
		relation1.add(this.createTuple("a","b","b"));
		relation1.add(this.createTuple("a","b","a"));
		relation1.add(this.createTuple("a","b","c"));
		
		Iterator i = relation0.iterator();
		while(i.hasNext()){
			System.out.println("relation0: " + ((Tuple)i.next()).toString());
		}
		i = relation1.iterator();
		while(i.hasNext()){
			System.out.println("relation1: " + i.next());
		}
		
		IJoin joiner = new Join();
		int[]indexes = new int[]{-1, -1, 0};
		IRelation result = joiner.join(relation0, relation1, indexes);
		
		i = result.iterator();
		while(i.hasNext()){
			System.out.println("result: " + i.next());
		}
		
		System.out.println("End");
	}
	
	private ITuple createTuple(String s0, String s1, String s2){
		Term term0 = new Term(s0);
		Term term1 = new Term(s1);
		Term term2 = new Term(s2);
		
		List termList = new LinkedList();
		termList.add(term0);
		termList.add(term1);
		termList.add(term2);
		
		ITuple tuple = new Tuple(termList);
		return tuple;
	}
}
