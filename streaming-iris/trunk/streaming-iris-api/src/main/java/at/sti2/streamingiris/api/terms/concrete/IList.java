package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.utils.equivalence.IEquivalentTerms;

public interface IList extends IConcreteTerm, java.util.List<IConcreteTerm> {

	/**
	 * Defines the RIF List URI.
	 */
	public static String DATATYPE_URI = "http://www.w3.org/2007/rif#List";

	/**
	 * Checks if this list equals the specified list taking into account the
	 * specified equivalent terms to determine the equality of the items of the
	 * lists.
	 * 
	 * @param obj
	 *            The object representing a list.
	 * @param equivalentTerms
	 *            The equivalent terms used to determine the equality of the
	 *            elements of the lists.
	 * @return Returns <code>true</code> if and only if the specified object is
	 *         also a list, both lists have the same size, and all corresponding
	 *         pairs of elements in the two lists are <i>equal</i> are
	 *         equivalent according to the specified equivalent terms.
	 */
	public boolean equals(Object obj, IEquivalentTerms equivalentTerms);

}
