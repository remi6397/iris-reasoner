/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris;

import java.lang.reflect.InvocationTargetException;

import junit.framework.Assert;

/**
 * @author richi
 * 
 */
public final class ObjectTest {

	private ObjectTest() {
		// prevent sublassing
	}

	public static void runTestEquals(final Object e0, final Object e1,
			final Object ue0) {
		Assert.assertEquals("A object must be equal to itself", e0, e0);
		Assert.assertEquals("The objects are equal", e0, e1);
		Assert.assertEquals("The objects are equal", e1, e0);
		Assert.assertFalse("The objects are unequal", e0.equals(ue0));
		Assert.assertFalse("The object is unequal to null", e0.equals(null));
	}

	public static void runTestClone(final Object o) {
		Assert.assertNotSame("Clone must not return the same object reference",
				o, runClone(o));
		Assert.assertEquals("Cloned objects must have the same classes", o
				.getClass(), runClone(o).getClass());
		Assert.assertEquals("Cloned objects must be equal", o, runClone(o));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Comparable> void runTestCompareTo(final T basic,
			final T equal, final T more, final T evenMore) {
		Assert.assertTrue("Something wrong with compareTo (" + basic
				+ " should be smaller than " + more + ")", basic
				.compareTo(more) < 0);
		Assert.assertTrue("Something wrong with compareTo (" + more
				+ " should be smaller than " + evenMore + ")", more
				.compareTo(evenMore) < 0);
		Assert.assertTrue("Something wrong with compareTo (" + basic
				+ " should be smaller than " + evenMore + ")", basic
				.compareTo(evenMore) < 0);

		Assert.assertTrue("Something wrong with compareTo (" + basic
				+ " should be equal to " + equal + ")",
				basic.compareTo(equal) == 0);

		Assert.assertTrue("Something wrong with compareTo (" + evenMore
				+ " should be greater than " + more + ")", evenMore
				.compareTo(more) > 0);
		Assert.assertTrue("Something wrong with compareTo (" + more
				+ " should be greater than " + basic + ")", more
				.compareTo(basic) > 0);
		Assert.assertTrue("Something wrong with compareTo (" + evenMore
				+ " should be greater than " + basic + ")", evenMore
				.compareTo(basic) > 0);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Comparable> void runTestCompareTo(final T basic,
			final T equal, final T more) {
		Assert.assertTrue("Something wrong with compareTo", basic
				.compareTo(more) < 0);

		Assert.assertTrue("Something wrong with compareTo", basic
				.compareTo(equal) == 0);

		Assert.assertTrue("Something wrong with compareTo", more
				.compareTo(basic) > 0);
	}

	public static void runTestHashCode(final Object basic, final Object equal) {
		Assert.assertEquals(
				"Two equal object should produce the same hashCode", basic
						.hashCode(), equal.hashCode());
	}

	/**
	 * Helpermethod to clone an object, because the Object.clone() method is
	 * protected. This Method clones a object using reflection.
	 * 
	 * @param o
	 *            the object to clone
	 * @return the clone
	 */
	private static Object runClone(final Object o) {
		Object clone = null;
		try {
			clone = o.getClass().getMethod("clone", (Class[]) null).invoke(o,
					(Object[]) null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return clone;
	}

}
