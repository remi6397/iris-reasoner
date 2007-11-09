/**
 * 
 */
package org.deri.iris.storage;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests the IndexingOnTheFlyRelation relation.
 * </p>
 * <p>
 * $Id: IndexingOnTheFlyRelationTest.java,v 1.3 2007-07-17 10:12:56 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.3 $
 */
public class IndexingOnTheFlyRelationTest extends
		AbstractRelationTest<IndexingOnTheFlyRelation> {

	public static Test suite() {
		return new TestSuite(IndexingOnTheFlyRelationTest.class,
				IndexingOnTheFlyRelationTest.class.getSimpleName());
	}

	@Override
	public void setUp() {
		r = new IndexingOnTheFlyRelation(4);
	}
}
