package org.deri.iris.basics;

import java.util.HashSet;
import java.util.Set;

/**
 * This test was made to show, that the order of the elements stored in an
 * HashSet will not remain the same if new elements were added.
 * 
 * @author richi
 */
public class HashSetTest2 {

	/**
	 * Simple test run. If the order would be unchanged when new elements were
	 * added, it should print out two equal strings.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Set<String> set = new HashSet<String>();
		set.add("a");
		set.add("b");
		set.add("g");
		set.add("h");
		System.out.println(get(3, set));
		set.add("c");
		System.out.println(get(3, set));

	}

	/**
	 * Returns the element at a special position of the set. <b>ATTENTION: the
	 * order will not remain the same (if HashSet is used as Set
	 * implementatiion)</b>
	 * 
	 * @param i
	 *            the index of the element
	 * @param set
	 *            where to retrieve the element from
	 * @return the element
	 * @throws NullPointerException
	 *             if the set is null
	 * @throws IndexOutOfBoundsException
	 *             if the index is equal or bigger than the size of the array
	 */
	private static String get(final int i, final Set<String> set) {
		if (set == null) {
			throw new NullPointerException();
		}
		if (i >= set.size()) {
			throw new IndexOutOfBoundsException(
					"The index is bigger or equal to the size of the set");
		}
		String[] arr = new String[set.size()];
		arr = set.toArray(arr);
		return arr[i];
	}

}
