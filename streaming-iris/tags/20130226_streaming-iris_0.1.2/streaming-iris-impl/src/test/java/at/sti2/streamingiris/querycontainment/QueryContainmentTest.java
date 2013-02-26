package at.sti2.streamingiris.querycontainment;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.compiler.Parser;

/**
 * <p>
 * Tests the query containment functionality.
 * </p>
 * <p>
 * $Id: QueryContainmentTest.java,v 1.1 2007-11-07 16:13:31 nathaliest Exp $
 * </p>
 * 
 * @author Nathalie Steinmetz, DERI Innsbruck
 * @version $Revision: 1.1 $
 */
public class QueryContainmentTest extends TestCase {

	public static Test suite() {
		return new TestSuite(QueryContainmentTest.class,
				QueryContainmentTest.class.getSimpleName());
	}

	/**
	 * Tests whether one query is contained within another query.
	 * 
	 * @throws Exception
	 */
	public void testQueryContaiment() throws Exception {
		final String prog = "vehicle(?X) :- car(?X)." + "?-car(?x)."
				+ "?-vehicle(?x).";

		Parser parser = new Parser();
		parser.parse(prog);
		List<IRule> rules = parser.getRules();
		List<IQuery> queries = parser.getQueries();

		final IQuery query1 = queries.get(0);
		final IQuery query2 = queries.get(1);

		final QueryContainment queryCont = new QueryContainment(rules);

		boolean result = queryCont.checkQueryContainment(query1, query2);

		assertTrue(result);
	}

	/**
	 * Tests whether one query is not contained within another query.
	 * 
	 * @throws Exception
	 */
	public void testQueryContaiment2() throws Exception {
		final String prog = "vehicle(?X) :- car(?X)." + "?-vehicle(?x)."
				+ "?-car(?x).";

		Parser parser = new Parser();
		parser.parse(prog);
		List<IRule> rules = parser.getRules();
		List<IQuery> queries = parser.getQueries();

		final IQuery query1 = queries.get(0);
		final IQuery query2 = queries.get(1);

		final QueryContainment queryCont = new QueryContainment(rules);

		boolean result = queryCont.checkQueryContainment(query1, query2);

		assertFalse(result);
	}

	/**
	 * Tests whether one query is contained within another query.
	 * 
	 * @throws Exception
	 */
	public void testTransitiveQueryContaiment() throws Exception {
		final String prog = "path(?X, ?Y) :- path(?X, ?Z), path(?Z, ?Y)."
				+ "?-path(?X, ?Y)." + "?-path(?X, ?Z), path(?Z, ?Y).";

		Parser parser = new Parser();
		parser.parse(prog);
		List<IRule> rules = parser.getRules();
		List<IQuery> queries = parser.getQueries();

		final IQuery query1 = queries.get(0);
		final IQuery query2 = queries.get(1);

		final QueryContainment queryCont = new QueryContainment(rules);

		boolean result = queryCont.checkQueryContainment(query1, query2);

		assertFalse(result);
	}

	/**
	 * Tests whether one query is contained within another query.
	 * 
	 * @throws Exception
	 */
	public void testTransitiveQueryContaiment2() throws Exception {
		final String prog = "path(?X, ?Y) :- path(?X, ?Z), path(?Z, ?Y)."
				+ "?-path(?X, ?Z), path(?Z, ?Y)."
				+ "?-path(?X, ?Z1), path(?Z1, ?Y).";

		Parser parser = new Parser();
		parser.parse(prog);
		List<IRule> rules = parser.getRules();
		List<IQuery> queries = parser.getQueries();

		final IQuery query1 = queries.get(0);
		final IQuery query2 = queries.get(1);

		final QueryContainment queryCont = new QueryContainment(rules);

		boolean result = queryCont.checkQueryContainment(query1, query2);

		assertTrue(result);
	}

}
