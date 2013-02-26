package at.sti2.streamingiris.terms.concrete;

/**
 * <p>
 * Some helper methods for some operations on terms.
 * </p>
 * <p>
 * $Id: TermHelper.java,v 1.3 2007-10-09 20:29:38 bazbishop237 Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.3 $
 * @date $Date: 2007-10-09 20:29:38 $
 */
class TermHelper {
	private TermHelper() {
		// prevent subclassing
	}

	/**
	 * Returns the double value from a <code>INumericTerm</code> <b>This method
	 * assumes that only numbers are stored in <code>INumericTerm</code>.</b>
	 * 
	 * @param n
	 *            the term
	 * @return the double value
	 * @throws NullPointerException
	 *             if the term is null
	 * @see Number
	 */
	/*
	 * static double getDouble(final INumericTerm n) { if (n == null) { throw
	 * new NullPointerException("The term must not be null"); } // TODO: maybe
	 * instance check for Number return ((Number) n.getValue()).doubleValue(); }
	 */

	/**
	 * Returns the float value from a <code>INumericTerm</code> <b>This method
	 * assumes that only numbers are stored in <code>INumericTerm</code>.</b>
	 * 
	 * @param n
	 *            the term
	 * @return the float value
	 * @throws NullPointerException
	 *             if the term is null
	 * @see Number
	 */
	/*
	 * static float getFloat(final INumericTerm n) { if (n == null) { throw new
	 * NullPointerException("The term must not be null"); } // TODO: maybe
	 * instance check for Number return ((Number) n.getValue()).floatValue(); }
	 */

	/**
	 * Returns the int value from a <code>INumericTerm</code> <b>This method
	 * assumes that only numbers are stored in <code>INumericTerm</code>.</b>
	 * 
	 * @param n
	 *            the term
	 * @return the int value
	 * @throws NullPointerException
	 *             if the term is null
	 * @see Number
	 */
	/*
	 * static int getInt(final INumericTerm n) { if (n == null) { throw new
	 * NullPointerException("The term must not be null"); } return (int)
	 * getDouble(n); }
	 */
}
