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
package org.deri.iris.builtins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * Built-in to do regular expression matching.
 */
public class RegexBuiltin extends BooleanBuiltin
{
	/**
	 * Constructs a built-in. Two terms must be passed to the constructor,
	 * the second one must be a string with the regular expression pattern.
	 * @param terms the terms
	 */
	public RegexBuiltin( final ITerm... terms )
	{
		super( PREDICATE, terms );

		if( terms.length != 2 )
			throw new IllegalArgumentException( getClass().getSimpleName() + ": Constructor requires exactly two parameters (ITerm term, IStringTerm pattern)" );
		
		if( ! (terms[ 1 ] instanceof IStringTerm) )
			throw new IllegalArgumentException( getClass().getSimpleName() + ": The second argument of the constructor must be a string pattern" );

		String pattern = (String) terms[ 1 ].getValue();
		
		mPattern = Pattern.compile( pattern );
	}

	protected boolean computeResult( ITerm[] terms )
	{
		assert terms.length == 2;
		
		if( terms[ 0 ] instanceof IStringTerm )
		{
			String testString = (String) terms[ 0 ].getValue();
			Matcher m = mPattern.matcher( testString );
			boolean result = m.matches();
			
			return result;
		}
		else
			return false;
	}

	private final Pattern mPattern;

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate( "REGEX", 2 );
}
