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
 * $Id: IndexingOnTheFlyRelationTest.java,v 1.1 2007-02-16 09:02:38 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.1 $
 */
public class IndexingOnTheFlyRelationTest extends
		GenericRelationTest<IndexingOnTheFlyRelation> {

	public static Test suite() {
		return new TestSuite(IndexingOnTheFlyRelationTest.class,
				IndexingOnTheFlyRelationTest.class.getSimpleName());
	}

	@Override
	public void setUp() {
		r = new IndexingOnTheFlyRelation();
	}
}
