/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.compiler;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.parser.lexer.Lexer;
import org.deri.iris.parser.lexer.LexerException;
import org.deri.iris.parser.parser.ParserException;
import org.deri.iris.storage.IRelation;

/**
 * Parses a datalog program in human readable form in to an IRIS object model.
 */
public class Parser
{
	/**
	 * Default constructor.
	 * Uses a default BuiltinRegister.
	 */
	public Parser()
	{
		mBuiltinRegister = new BuiltinRegister();
	}
	
	/**
	 * Constructor for custom BuitinRegister.
	 * @param builtinRegister The built-in register to use.
	 */
	public Parser( BuiltinRegister builtinRegister )
	{
		mBuiltinRegister = builtinRegister;
	}
	
	/**
	 * Get the built-in register instance for adding or removing built-ins.
	 * @return The built-in register instance
	 */
	public BuiltinRegister getBuiltinRegister()
	{
		return mBuiltinRegister;
	}
	
	/**
	 * Parses a datalog program.
	 * @param program The program to parse. This must not be null.
	 * @return a newly created program represented the parsed one.
	 * @throws org.deri.iris.compiler.ParserException
	 */
	public void parse( String program ) throws org.deri.iris.compiler.ParserException
	{
		if ( program == null )
			throw new IllegalArgumentException("The reader must not be null");
		
		parse(new StringReader(program) );
	}
	
	public List<IRule> getRules()
	{
		return mTreeWalker.getRuleBase();
	}
	
	public Map<IPredicate,IRelation> getFacts()
	{
		return mTreeWalker.getFacts();
	}
	
	public List<IQuery> getQueries()
	{
		return mTreeWalker.getQueries();
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
	 * @throws org.deri.iris.compiler.ParserException if something went wrong while
	 * parsing
	 * @throws IllegalArgumentException if the reader is <code>null</code>
	 */
	public void parse(final Reader r) throws org.deri.iris.compiler.ParserException
	{
		if (r == null)
			throw new IllegalArgumentException("The reader must not be null");
		
		mTreeWalker = new TreeWalker( mBuiltinRegister );

		try
		{
			org.deri.iris.parser.parser.Parser parser = new org.deri.iris.parser.parser.Parser( new Lexer( new PushbackReader(r, 1024) ) );

			parser.parse().apply( mTreeWalker );
		}
		catch (ParserException e)
		{
			throw new org.deri.iris.compiler.ParserException( "Parser error: " + e.getMessage() );
		}
		catch (LexerException e)
		{
			throw new org.deri.iris.compiler.ParserException( "Lexer error: " + e.getMessage() );
		}
		catch (IOException e)
		{
			// This error condition is intentionally hidden, since it is considered very unlikely
			// to occur. Usually library users will pass a String containing the logic program, in
			// which case this exception type can not be thrown.
			throw new org.deri.iris.compiler.ParserException( "I/O error: " + e.getMessage() );
		}
		catch( IllegalArgumentException e )
		{
			// Some errors (such as wrong number of arguments for a type) manifest themselves as IllegalArgumentExceptions.
			throw new org.deri.iris.compiler.ParserException( e.getMessage() );
		}
	}
	
	private TreeWalker mTreeWalker;
	private final BuiltinRegister mBuiltinRegister;
}
