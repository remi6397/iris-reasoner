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
package org.deri.iris.compiler;

import static org.deri.iris.factory.Factory.PROGRAM;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

import org.deri.iris.api.IProgram;
import org.deri.iris.parser.lexer.Lexer;
import org.deri.iris.parser.lexer.LexerException;
import org.deri.iris.parser.parser.ParserException;

/**
 * <p>
 * Parser to parse datalog programs.
 * </p>
 * <p>
 * $Id: Parser.java,v 1.8 2007-06-20 12:07:09 poettler_ric Exp $
 * </p>
 * @author Francisco Garcia
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.8 $
 */
public class Parser {
     
	/**
	 * Parses a datalog program.
	 * @param prog the program to parse
	 * @return a newly created program represented the parsed one.
	 * @throws IllegalArgumentException if something went wrong while
	 * parsing
	 * @throws NullPointerException if the program is <code>null</code>
	 */
	public static IProgram parse(final String prog) {
		if (prog == null) {
			throw new NullPointerException("The string to parse must not be null");
		}
		return parse(prog, null);
	}

	/**
	 * Parses a datalog program.
	 * @param r the reader from where to read the program
	 * @return a newly created program represented the parsed one.
	 * @throws IllegalArgumentException if something went wrong while
	 * parsing
	 * @throws NullPointerException if the reader is <code>null</code>
	 */
	public static IProgram parse(final Reader r) {
		if (r == null) {
			throw new NullPointerException("The reader must not be null");
		}
		return parse(r, null);
	}

	/**
	 * Parses a datalog string. The parsed object will be add to the
	 * submitted program.
	 * @param prog the string to parse
	 * @param p the program where to add the objects. Might be
	 * <code>null</code>
	 * @return the input program, or a newly created one (if the input
	 * program was <code>null</code>) containing all the objects of the
	 * parsed one.
	 * @throws IllegalArgumentException if something went wrong while
	 * parsing
	 * @throws NullPointerException if the string is <code>null</code>
	 */
	public static IProgram parse(final String prog, final IProgram p) {
		if (prog == null) {
			throw new NullPointerException("The string to parse must not be null");
		}
		return parse(new StringReader(prog), p);
	}

	/**
	 * Parses a datalog string. The parsed object will be add to the
	 * submitted program.
	 * @param r the reader from where to read the program
	 * @param p the program where to add the objects. Might be
	 * <code>null</code>
	 * @return the input program, or a newly created one (if the input
	 * program was <code>null</code>) containing all the objects of the
	 * parsed one.
	 * @throws IllegalArgumentException if something went wrong while
	 * parsing
	 * @throws NullPointerException if the reader is <code>null</code>
	 */
	public static IProgram parse(final Reader r, final IProgram p) {
		if (r == null) {
			throw new NullPointerException("The reader must not be null");
		}
		final IProgram prog = (p == null) ? PROGRAM.createProgram() : p;
		try {
			(new org.deri.iris.parser.parser.Parser(new Lexer(new PushbackReader(r, 1024))))
				.parse().apply(new TreeWalker(prog));
		} catch (ParserException e) {
			throw new IllegalArgumentException("Could not parse the program", e);
		} catch (LexerException e) {
			throw new IllegalArgumentException("Could not parse the program", e);
		} catch (IOException e) {
			throw new IllegalArgumentException("Something wrong while reading the program", e);
		}
		return prog;
	}
}
