package org.deri.iris.rdb.storage;

import java.util.Iterator;

/**
 * An {@link Iterator} that should be closed after it is not need anymore.
 * 
 * @param <E>
 *            The type this class iterates over.
 */
public interface CloseableIterator<E> extends Iterator<E> {

	/**
	 * Closes this iterator. This iterator can not be used anymore, after it has
	 * been closed.
	 */
	public void close();

}
