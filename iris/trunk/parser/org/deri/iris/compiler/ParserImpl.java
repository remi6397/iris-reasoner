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

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.IProgram;
import org.deri.iris.parser.lexer.Lexer;
import org.deri.iris.parser.lexer.LexerException;
import org.deri.iris.parser.node.Node;
import org.deri.iris.parser.parser.Parser;
import org.deri.iris.parser.parser.ParserException;
import java.io.StringReader;
import java.io.PushbackReader;
import java.io.IOException;
import org.deri.iris.parser.parser.ParserException;
import org.deri.iris.parser.lexer.LexerException;

/**
 * <p>
 * The parser implementation.
 * </p>
 * <p>
 * $Id: ParserImpl.java,v 1.6 2007-04-06 06:59:45 poettler_ric Exp $
 * </p>
 * @author Francisco Garcia
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.6 $
 */
public class ParserImpl implements org.deri.iris.compiler.Parser {
     
	public void compileKB(String kb, IProgram p) {
		try {
			generateParseTree(kb).apply(new TreeWalker(p));
		} catch (ParserException e) {
			throw new IllegalArgumentException("Could not parse the program", e);
		} catch (LexerException e) {
			throw new IllegalArgumentException("Could not parse the program", e);
		}
	}
    
	private Node generateParseTree(String reader) throws ParserException, LexerException {
		final StringReader r = new StringReader(reader);
		final Lexer lexer = new Lexer(new PushbackReader(r, 16384));
		final Parser parser = new Parser(lexer);
		try {
			return parser.parse();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
