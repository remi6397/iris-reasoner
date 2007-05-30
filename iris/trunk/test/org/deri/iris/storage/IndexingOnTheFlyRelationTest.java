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
 * $Id: IndexingOnTheFlyRelationTest.java,v 1.2 2007-05-30 14:06:07 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.2 $
 */
public class IndexingOnTheFlyRelationTest extends
		GenericRelationTest<IndexingOnTheFlyRelation> {

	public static Test suite() {
		return new TestSuite(IndexingOnTheFlyRelationTest.class,
				IndexingOnTheFlyRelationTest.class.getSimpleName());
	}

	@Override
	public void setUp() {
		r = new IndexingOnTheFlyRelation(4);
	}
}
