package at.sti2.streamingiris.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

/**
 * Modified array list implementation that enforces uniqueness, but maintains
 * ordering.
 * 
 * @param <E>
 *            The type of the elements of this collection.
 */
public class UniqueList<E> extends ArrayList<E> {
	// private Logger logger = LoggerFactory.getLogger(getClass());

	/** Serialisation ID */
	private static final long serialVersionUID = 1L;

	/** The set used to enforce uniqueness. */
	private final Map<E, Long> mMap = new HashMap<E, Long>();

	/**
	 * Constructor.
	 * 
	 * @param initialCapacity
	 *            Space for this many elements is reserved.
	 */
	public UniqueList(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Default constructor.
	 */
	public UniqueList() {
	}

	@Override
	public boolean add(E o) {
		boolean added = false;
		if (!mMap.containsKey(o)) {
			super.add(o);
			added = true;
		}
		mMap.put(o, new Long(0));
		return added;
	}

	public boolean add(E o, long timestamp) {
		boolean added = false;
		if (!mMap.containsKey(o)) {
			mMap.put(o, new Long(timestamp));
			added = true;
			super.add(o);
		} else if (mMap.get(o) != 0) {
			mMap.put(o, new Long(timestamp));
		}
		return added;
	}

	@Override
	public void add(int index, E element) {
		if (!mMap.containsKey(element)) {
			super.add(index, element);
		}
		mMap.put(element, new Long(0));
	}

	public void add(int index, E element, long timestamp) {
		if (!mMap.containsKey(element)) {
			mMap.put(element, new Long(timestamp));
			super.add(index, element);
		} else if (mMap.get(element) != 0) {
			mMap.put(element, new Long(timestamp));
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed = false;

		for (E element : c) {
			if (!mMap.containsKey(element)) {
				super.add(element);
				changed = true;
			}
			mMap.put(element, new Long(0));
		}
		return changed;
	}

	public boolean addAll(Collection<? extends E> c, long timestamp) {
		boolean changed = false;

		for (E element : c) {
			if (!mMap.containsKey(element)) {
				mMap.put(element, new Long(timestamp));
				super.add(element);
				changed = true;
			} else if (mMap.get(element) != 0) {
				mMap.put(element, new Long(timestamp));
			}
		}
		return changed;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean changed = false;

		for (E element : c) {
			if (!mMap.containsKey(element)) {
				super.add(index++, element);
				changed = true;
			}
			mMap.put(element, new Long(0));
		}
		return changed;
	}

	public boolean addAll(int index, Collection<? extends E> c, long timestamp) {
		boolean changed = false;

		for (E element : c) {
			if (!mMap.containsKey(element)) {
				mMap.put(element, new Long(timestamp));
				super.add(index++, element);
				changed = true;
			} else if (mMap.get(element) != 0) {
				mMap.put(element, new Long(timestamp));
			}
		}
		return changed;
	}

	@Override
	public E remove(int index) {
		E removed = super.remove(index);
		mMap.remove(removed);
		return removed;
	}

	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);

		if (result) {
			// logger.debug("REMOVED: {}", o);
			mMap.remove(o);
		}

		return result;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		int count = toIndex - fromIndex;
		for (int i = 0; i < count; ++i)
			remove(fromIndex);
	}

	@Override
	public E set(int index, E newElement) {
		E previous = get(index);

		if (mMap.containsKey(newElement)) {
			// Can't add a non-unique value so just remove the old one
			mMap.remove(previous);
			super.remove(index);
			return previous;
		} else {
			mMap.remove(previous);
			mMap.put(newElement, new Long(0));
			return super.set(index, newElement);
		}
	}

	public E set(int index, E newElement, long timestamp) {
		E previous = get(index);

		if (mMap.containsKey(newElement)) {
			// Can't add a non-unique value so just remove the old one
			mMap.remove(previous);
			super.remove(index);
			return previous;
		} else {
			mMap.remove(previous);
			mMap.put(newElement, new Long(timestamp));
			return super.set(index, newElement);
		}
	}

	@Override
	public boolean contains(Object element) {
		return mMap.containsKey(element);
	}

	/**
	 * Returns the timestamp of the element.
	 * 
	 * @param element
	 *            The element.
	 * @return Returns the timestamp if the element exists, -1 otherwise.
	 */
	public long getTimestamp(E element) {
		if (mMap.containsKey(element)) {
			return mMap.get(element);
		} else {
			return -1;
		}
	}

	/**
	 * Sets the timestamp of the element, if the element exists.
	 * 
	 * @param element
	 *            The element.
	 * @param timestamp
	 *            The new timestamp for the element.
	 */
	public void setTimestamp(E element, long timestamp) {
		if (mMap.containsKey(element)) {
			mMap.put(element, new Long(timestamp));
		}
	}

	public int getIndex(E element) {
		return super.indexOf(element);
	}

	/**
	 * An iterator that will not allow modification.
	 */
	class UniqueIterator implements Iterator<E> {
		public UniqueIterator(Iterator<E> child) {
			mChild = child;
		}

		public boolean hasNext() {
			return mChild.hasNext();
		}

		public E next() {
			return mChild.next();
		}

		public void remove() {
			throw new UnsupportedOperationException(
					"UniqueList iterators do not allow modification");
		}

		private Iterator<E> mChild;
	}

	@Override
	public Iterator<E> iterator() {
		return new UniqueIterator(super.iterator());
	}

	/**
	 * An iterator that will not allow modification.
	 */
	class UniqueListIterator implements ListIterator<E> {
		public UniqueListIterator(ListIterator<E> child) {
			mChild = child;
		}

		public void add(E o) {
			throw new UnsupportedOperationException(
					"UniqueList iterators do not allow modification");
		}

		public boolean hasNext() {
			return mChild.hasNext();
		}

		public boolean hasPrevious() {
			return mChild.hasPrevious();
		}

		public E next() {
			return mChild.next();
		}

		public int nextIndex() {
			return mChild.nextIndex();
		}

		public E previous() {
			return mChild.previous();
		}

		public int previousIndex() {
			return mChild.previousIndex();
		}

		public void remove() {
			throw new UnsupportedOperationException(
					"UniqueList iterators do not allow modification");
		}

		public void set(E o) {
			throw new UnsupportedOperationException(
					"UniqueList iterators do not allow modification");
		}

		private ListIterator<E> mChild;
	}

	@Override
	public ListIterator<E> listIterator() {
		return new UniqueListIterator(super.listIterator());
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new UniqueListIterator(super.listIterator(index));
	}
}
