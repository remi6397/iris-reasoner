package at.sti2.streamingiris.storage;

import java.util.List;

import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * Represents an index on something, usually a relation. This index interface
 * can be used when sort order is not relevant.
 */
public interface IIndex {
	/**
	 * Return all tuples matching the given key.
	 * 
	 * @param key
	 *            The collection (possibly empty) of terms in the key.
	 * @return The list of matching tuples. TODO This might change to returning
	 *         Iterator<ITuple> to allow for very large data sets.
	 */
	List<ITuple> get(List<ITerm> key);
}
