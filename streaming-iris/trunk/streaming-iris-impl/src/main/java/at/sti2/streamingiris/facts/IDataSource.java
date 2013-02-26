package at.sti2.streamingiris.facts;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.storage.IRelation;

/**
 * <p>
 * Interface for pluggable datasources for iris.
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 */
public interface IDataSource {

	/**
	 * <p>
	 * Retrieves some tuples for a given predicate from the data source and adds
	 * it to a given relation.
	 * </p>
	 * <p>
	 * The terms in <code>from</code> and <code>to</code> set the lower and
	 * upper bounds for the terms in the corresponding columns of the tuples,
	 * which should be added to the tuple collection. <code>null</code> in the
	 * <code>from</code> or <code>to</code> list, stands for the smallest,
	 * respectively biggest possible term for this column.
	 * </p>
	 * 
	 * @param p
	 *            the predicate for which to retrieve the tuples (because one
	 *            data source might hold tuples for multiple predicates)
	 * @param from
	 *            the lower bound for the tuples which should be added to the
	 *            relation (<code>null</code> is equivalent to a tuple
	 *            containing only <code>null</code>s)
	 * @param to
	 *            the upper bound for the tuples which should be added to the
	 *            relation (<code>null</code> is equivalent to a tuple
	 *            containing only <code>null</code>s)
	 * @param r
	 *            the relation where to add the tuples
	 */
	public void get(final IPredicate p, final ITuple from, final ITuple to,
			final IRelation r);
}
