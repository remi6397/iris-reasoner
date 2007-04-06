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
package org.deri.iris.builtins;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.deri.iris.api.basics.IPredicate;

/**
 * <p>
 * Holds the informaitons about various registered builtins.
 * </p>
 * <p>
 * Before a builtin can be recognized by the parser as a builtin it must be
 * first registered in the BuiltinRegister of the program the parser uses. A
 * builtin can be registered at startup, or later by hand. In both cases the
 * builtin got to implement a <code>public static</code> method called
 * getBuiltinPredicate taking <b>no</b> arguments and returning the predicate
 * defining the builtin (the name and the arity of the builtin). To register a
 * builtin at startup there must be a file called &quot;<code>builtins.load</code>&quot;
 * in the classpath containing the classnames of the builtins to register with
 * one classname on each line. If you want to register a builtin by hand, you
 * simply call the {@link #registerBuiltin() registerBuiltin} method passing the
 * classname of the builtin to register.
 * </p>
 * <p>
 * $Id: BuiltinRegister.java,v 1.1 2007-04-06 06:52:04 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.1 $
 */
public final class BuiltinRegister {

	/** Holding all the information about the builtins.
	 * <ul>
	 * <li>key: the name (predicate symbol of the builtin)</li>
	 * <li>value: the RegisterEntry containing the informaiton about the
	 * builtin</li>
	 * </ul>
	 */
	private final Map<String, RegisterEntry> reg = new HashMap<String, RegisterEntry>();

	/**
	 * Constructs a new builtin register. This constructor also reads the
	 * builtins.load file from the classpath.
	 */
	public BuiltinRegister() {
		final InputStream is = BuiltinRegister.class.getResourceAsStream("builtins.load");
		BufferedReader br = null;
		if (is != null) {
			br = new BufferedReader(new InputStreamReader(is));
			try {
				String line;
				while ((line = br.readLine()) != null) {
					try {
						privRegisterBuiltin(Class.forName(line));
					} catch (ClassNotFoundException e) {
						System.err.println("Couldn't load the class: " + line);
					}
				}
			} catch (IOException e) {
				System.err.println("Exception while reading the builtins.load file: " + e.getMessage());
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						System.err.println("Exception while closing the builtins.load file: " + 
								e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Registers a single builtin. This method searches for the
	 * &quot;<code>getBuiltinPredicate</code>&quot; method and ivokes it.
	 * @param c the builtinclass to register
	 * @throws NullPointerException if the class is <code>null</code>
	 */
	private void privRegisterBuiltin(final Class c) {
		if (c == null) {
			throw new NullPointerException("The class must not be null");
		}
		try {
			final RegisterEntry ent = new RegisterEntry(c, 
					(IPredicate) c.getMethod("getBuiltinPredicate").invoke(null));
			reg.put(ent.getName(), ent);
		} catch (NoSuchMethodException e) {
			System.err.println("Coundn't find the method: " + c.getName() + 
					".getBuiltinPredicate(): " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.err.println("Coundn't find the method: " + c.getName() + 
					".getBuiltinPredicate(): " + e.getMessage());
		} catch (InvocationTargetException e) {
			System.err.println("Coundn't find the method: " + c.getName() + 
					".getBuiltinPredicate(): " + e.getMessage());
		}
	}

	/**
	 * Registers a single builtin. This method searches for the
	 * &quot;<code>getBuiltinPredicate</code>&quot; method and ivokes it.
	 * @param c the builtinclass to register
	 * @throws NullPointerException if the class is <code>null</code>
	 */
	public void registerBuiltin(final Class c) {
		privRegisterBuiltin(c);
	}

	/**
	 * Returns the arity of a registered builtin.
	 * @param s the name (predicate symbol) of the builtin
	 * @return the arity, or -1 if such a builtin hasn't been registered yet
	 */
	public int getBuiltinArity(final String s) {
		final RegisterEntry re = reg.get(s);
		return (re == null) ? -1 : re.getArity();
	}

	/**
	 * Returns the class of a registered builtin.
	 * @param s the name (predicate symbol) of the builtin
	 * @return the class, or <code>null</code> if such a builtin hasn't been registered yet
	 */
	public Class getBuiltinClass(final String s) {
		final RegisterEntry re = reg.get(s);
		return (re == null) ? null : re.getBuiltinClass();
	}

	/**
	 * <p>
	 * Returns a short description of this object. <b>The format of
	 * the returned string is undocumented and subjet to change.</b>
	 * </p>
	 * <p>
	 * An example return string could be: <code>[ADD[3,
	 * org.deri.iris.builtins.AddBuiltin],SUBTRACT[3,
	 * org.deri.builtins.SubtractBuiltin]]</code>
	 * </p>
	 * @return the description
	 */
	public String toString() {
		return reg.values().toString();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof BuiltinRegister)) {
			return false;
		}
		return reg.equals(((BuiltinRegister) o).reg);
	}

	public int hashCode() {
		return reg.hashCode();
	}

	/**
	 * Holds various informations about a builtin.
	 */
	private static class RegisterEntry {

		/** The class of the builtin. */
		private Class builtinClass;

		/** The predicate defining the builtin. */
		private IPredicate pred;

		/**
		 * Constructs a registry entry.
		 * @param c the class of the builtin
		 * @param p the predicate of the builtin
		 * @throws NullPointerException if the class is <code>null</code>
		 * @throws NullPointerException if the predicate is <code>null</code>
		 */
		public RegisterEntry(final Class c, final IPredicate p) {
			if (c == null) {
				throw new NullPointerException("The class must not be null");
			}
			if (p == null) {
				throw new NullPointerException("The predicate must not be null");
			}
			builtinClass = c;
			pred = p;
		}

		/**
		 * Returns the class of the builtin.
		 * @return the class
		 */
		public Class getBuiltinClass() {
			return builtinClass;
		}

		/**
		 * Returns the arity of the builtin.
		 * @return the arity
		 */
		public int getArity() {
			return pred.getArity();
		}

		/**
		 * Returns the name (predicate symbol) of the builtin.
		 * @return the name
		 */
		public String getName() {
			return pred.getPredicateSymbol();
		}

		/**
		 * <p>
		 * Returns a short description of this object. <b>The format of
		 * the returned string is undocumented and subjet to change.</b>
		 * </p>
		 * <p>
		 * An example return string could be: <code>ADD[3,
		 * org.deri.iris.builtins.AddBuiltin)</code>
		 * </p>
		 * @return the description
		 */
		public String toString() {
			return getName() + "[" + getArity() + ", " + getBuiltinClass() + "]";
		}

		public boolean equals(final Object o) {
			if (!(o instanceof RegisterEntry)) {
				return false;
			}
			final RegisterEntry e = (RegisterEntry) o;
			return builtinClass.equals(e.builtinClass) && pred.equals(e.pred);
		}

		public int hashCode() {
			int res = 17;
			res = res * 37 + pred.hashCode();
			res = res * 37 + builtinClass.hashCode();
			return res;
		}
	}
}
