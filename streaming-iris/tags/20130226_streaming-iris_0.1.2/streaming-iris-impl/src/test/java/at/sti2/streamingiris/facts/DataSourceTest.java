package at.sti2.streamingiris.facts;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.KnowledgeBaseFactory;
import at.sti2.streamingiris.api.IKnowledgeBase;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.storage.IRelation;

/**
 * <p>
 * Tests the functionality of the data sources.
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
 */
public class DataSourceTest extends TestCase {

	/** The predicate for <code>testSimpleSource</code>. */
	private static final IPredicate A1 = BASIC.createPredicate("a", 1);

	/** The tuple for <code>testSimpleSource</code>. */
	private static final ITuple T_A = BASIC.createTuple(TERM.createString("a"));

	public static Test suite() {
		return new TestSuite(DataSourceTest.class,
				DataSourceTest.class.getSimpleName());
	}

	/**
	 * Tests the handling of a simple data source.
	 * 
	 * @see <a
	 *      href="http://sourceforge.net/tracker/index.php?func=detail&aid=1846034&group_id=167309&atid=842437">maintenance
	 *      #1846034: Create a interface to plug in external datasources</a>
	 */
	public void testSimpleSource() throws Exception {
		final String prog = "?- a(?X).";

		final Parser p = new Parser();
		p.parse(prog);

		final IQuery q = p.getQueries().iterator().next();

		// setting up the configuration
		final Configuration conf = KnowledgeBaseFactory
				.getDefaultConfiguration();
		conf.externalDataSources.add(new SimpleSource());

		// setting up the knowledge base
		IKnowledgeBase kb = KnowledgeBaseFactory.createKnowledgeBase(null,
				null, conf);

		// executing the query
		final IRelation result = kb.execute(q);

		// asserting the result
		assertEquals("There must be exactly one fact", 1, result.size());
		assertEquals("Couldn't find the correct fact", T_A, result.get(0));

		kb.shutdown();
	}

	/**
	 * Simple data source for the <code>testSimpleSource</code> method.
	 */
	private static class SimpleSource implements IDataSource {

		public void get(final IPredicate p, final ITuple from, final ITuple To,
				final IRelation r) {
			if (p.equals(A1)) {
				r.add(T_A);
			}
		}
	}
}
