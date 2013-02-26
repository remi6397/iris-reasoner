package at.sti2.streamingiris;

import junit.framework.Assert;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * <p>
 * Static test collection for terms.
 * </p>
 * <p>
 * $Id: TermTests.java,v 1.1 2007-07-17 10:12:55 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public final class TermTests {

	private TermTests() {
		// prevent subclassing
	}

	public static void runTestGetMinValue(ITerm small) {
		Assert.assertTrue("The smallest value of "
				+ small.getClass().getSimpleName() + " mmst be smaller than "
				+ small, small.compareTo(null) > 0);
	}

}
