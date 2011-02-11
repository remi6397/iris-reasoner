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
package org.deri.iris.facts;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.Configuration;
import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.compiler.Parser;
import org.deri.iris.storage.IRelation;

/**
 * <p>
 * Tests the functionality of the data sources.
 * </p>
 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
 */
public class DataSourceTest extends TestCase {

	/** The predicate for <code>testSimpleSource</code>. */
	private static final IPredicate A1 = BASIC.createPredicate("a", 1);

	/** The tuple for <code>testSimpleSource</code>. */
	private static final ITuple T_A = BASIC.createTuple(TERM.createString("a"));

	public static Test suite() {
		return new TestSuite(DataSourceTest.class, DataSourceTest.class.getSimpleName());
	}

	/**
	 * Tests the handling of a simple data source.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1846034&group_id=167309&atid=842437">maintenance #1846034: Create a interface to plug in external datasources</a>
	 */
	public void testSimpleSource() throws Exception {
		final String prog = "?- a(?X).";

		final Parser p = new Parser();
		p.parse(prog);

		final IQuery q = p.getQueries().iterator().next();

		// setting up the configuration
		final Configuration conf = KnowledgeBaseFactory.getDefaultConfiguration();
		conf.externalDataSources.add(new SimpleSource());

		// setting up the knowledge base
		IKnowledgeBase kb = KnowledgeBaseFactory.createKnowledgeBase(null, null, conf);

		// executing the query
		final IRelation result = kb.execute(q);

		// asserting the result
		assertEquals("There must be exactly one fact", 1, result.size());
		assertEquals("Couldn't find the correct fact", T_A, result.get(0));
	}

	/**
	 * Simple data source for the <code>testSimpleSource</code> method.
	 */
	private static class SimpleSource implements IDataSource {

		public void get(final IPredicate p, final ITuple from, final ITuple To, final IRelation r) {
			if (p.equals(A1)) {
				r.add(T_A);
			}
		}
	}
}
