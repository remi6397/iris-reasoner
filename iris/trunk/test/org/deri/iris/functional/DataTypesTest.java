/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2007 Digital Enterprise Research Institute (DERI), 
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
package org.deri.iris.functional;

import junit.framework.TestCase;

/**
 * Tests for IRIS supported data types.
 */
public class DataTypesTest extends TestCase
{
	/**
	 * Create (valid) facts using all possible data types.
	 * @throws Exception 
	 */
	public void testValidLiterals() throws Exception
	{
		String allDataTypes =
			"p( _string( 'a string' ) )." +
			"p( 'literal string' )." +
			
			"p( _decimal( -1.11 ) )." +
			"p( 2.22 )." +
			
			"p( _integer( 333 ) )." +
			"p( -444 )." +
			
			"p( _float( 5.55 ) )." +
			
			"p( _double( 6.66 ) )." +
			
			"p( _iri( 'http://example.org/PersonOntology#Person' ) )." +
			"p( _'http://example.org/PersonOntology#Human' )." +
			
			"p( dc#title )." +
			"p( _sqname( foaf#name ) )." +

			"p( _boolean( 'true' ) )." +
			"p( _boolean( 'false' ) )." +

			"p( _duration( 1970, 1, 1, 23, 15, 30 ) )." +
			"p( _duration( 1970, 1, 1, 23, 15, 29, 99 ) )." +

			"p( _datetime( 1980, 2, 2, 1, 2, 3 ) )." +
			"p( _datetime( 1980, 2, 2, 1, 2, 3, 1, 30 ) )." +
			"p( _datetime( 1980, 2, 2, 1, 2, 3, 99, 1, 30 ) )." +
			
			"p( _date( 1981, 3, 3 ) )." +
			"p( _date( 1982, 4, 4, 13, 30 ) )." +
			
			"p( _time( 1, 2, 3 ) )." +
			"p( _time( 1, 2, 3, 1, 30 ) )." +
			"p( _time( 1, 2, 3, 99, 1, 30 ) )." +
			
			"p( _gyear( 1991 ) )." +
			"p( _gyearmonth( 1992, 2 ) )." +
			"p( _gmonth( 3 ) )." +
			"p( _gmonthday( 2, 28 ) )." +
			"p( _gday( 31 ) )." +
			
			"p( _hexbinary( '0FB7abcd' ) )." +
			"p( _base64binary( 'QmFycnkgQmlzaG9w' ) )." +
			"";

		String program =
			allDataTypes +
			"?- p( ?X ).";
		
       	String expectedResults = allDataTypes;

       	Helper.evaluateWithAllStrategies( program, expectedResults );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_String()
	{
		Helper.checkFailureWithAllStrategies( "p( _string( 'a', 'b' ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_Decimal()
	{
		Helper.checkFailureWithAllStrategies( "p( _decimal( -1.A1 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( 1.2B ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_Integer()
	{
		Helper.checkFailureWithAllStrategies( "p( _integer( -B ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( -C ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_Float()
	{
		Helper.checkFailureWithAllStrategies( "p( _float( 3.r3) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_Double()
	{
		Helper.checkFailureWithAllStrategies( "p( _double( -2.3u ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_IRI()
	{
		Helper.checkFailureWithAllStrategies( "p( _iri( 'http://example.org/PersonOntology #Person' ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _'http://example.org/ PersonOntology#Human' ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_SQName()
	{
		Helper.checkFailureWithAllStrategies( "p( dc #title ).", null );
		Helper.checkFailureWithAllStrategies( "p( _sqname( foaf name ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_Boolean()
	{
		// TODO This should fail - invalid value
		Helper.checkFailureWithAllStrategies( "p( _boolean( 'blah' ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_DateTime()
	{
		// Too few parameters
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 3, 4, 12, 30 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 3, 4, 12, 30, 0, 1, 2, 3, 4 ) ).", null );

		// Bad month
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 13, 4, 12, 30, 0 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 0, 4, 12, 30, 0 ) ).", null );

		// Bad day
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 32, 12, 30, 0 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 0, 12, 30, 0 ) ).", null );

		// Bad hour
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 24, 30, 0 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, -1, 30, 0 ) ).", null );

		// Bad minute
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 60, 0 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, -1, 0 ) ).", null );

		// Bad second, NB There can be leap seconds!
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 59, 61 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, 0, -1 ) ).", null );

		// Bad millisecond
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 59, 59, 1000 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, 0, 0, -1 ) ).", null );

		// Bad time zone hour
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 59, 59, 999, 25, 30 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, 0, 0, 0, -25, 0 ) ).", null );

		// TODO These should fail, but don't!
		// Bad time zone minute
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 12, 31, 23, 59, 59, 999, 1, 60 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _datetime( 1982, 1, 1, 23, 0, 0, 0, -1, -60 ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_Date()
	{
		// Wrong number of parameters
		Helper.checkFailureWithAllStrategies( "p( _date( 1982, 3 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _date( 1982, 3, 4, 12, 30, 1 ) ).", null );

		// Bad time zone hour
		Helper.checkFailureWithAllStrategies( "p( _date( 1982, 12, 31, 25, 30 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _date( 1982, 1, 1, -25, 0 ) ).", null );

		// TODO These should fail, but don't!
		// Bad time zone minute
		Helper.checkFailureWithAllStrategies( "p( _date( 1982, 12, 31, 1, 60 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _date( 1982, 1, 1, -1, -60 ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_Time()
	{
		// Wrong number of parameters
		Helper.checkFailureWithAllStrategies( "p( _time( 12, 30 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _time( 12, 30, 0, 99, 13, 0, 1 ) ).", null );

		// Bad hour
		Helper.checkFailureWithAllStrategies( "p( _time( 24, 30, 0 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _time( -1, 30, 0 ) ).", null );

		// Bad minute
		Helper.checkFailureWithAllStrategies( "p( _time( 23, 60, 0 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _time( 23, -1, 0 ) ).", null );

		// Bad second, NB There can be leap seconds!
		Helper.checkFailureWithAllStrategies( "p( _time( 23, 59, 61 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _time( 23, 0, -1 ) ).", null );

		// Bad millisecond
		Helper.checkFailureWithAllStrategies( "p( _time( 23, 59, 59, 1000 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _time( 23, 0, 0, -1 ) ).", null );

		// Bad time zone hour
		Helper.checkFailureWithAllStrategies( "p( _time( 1982, 12, 31, 23, 59, 59, 999, 25, 30 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _time( 1982, 1, 1, 23, 0, 0, 0, -25, 0 ) ).", null );

		// TODO These should fail, but don't!
		// Bad time zone minute
		Helper.checkFailureWithAllStrategies( "p( _time( 23, 59, 59, 999, 1, 60 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _time( 23, 0, 0, 0, -1, -60 ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_YearMonth()
	{
		Helper.checkFailureWithAllStrategies( "p( _yearmonth( 1980 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _yearmonth( 1980, 12, 1 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _yearmonth( 1980, 13 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _yearmonth( 1980, 0 ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_MonthDay()
	{
		Helper.checkFailureWithAllStrategies( "p( _monthday( 12 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _monthday( 12, 1, 1 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _monthday( 13, 1 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _monthday( 0, 1 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _monthday( 12, 32 ) ).", null );
		Helper.checkFailureWithAllStrategies( "p( _monthday( 1, 0 ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_HexBinary()
	{
		// Invalid hexa-decimal
		Helper.checkFailureWithAllStrategies( "p( _hexbinary( '0FB7abcdG' ) ).", null );
	}
	
	/**
	 * Check that badly formatted literals cause failures.
	 * @throws Exception 
	 */
	public void testInvalidLiteral_Base64Binary()
	{
		// Invalid base 64
		Helper.checkFailureWithAllStrategies( "p( _base64binary( 'QmFycnkgQmlzaG9wa' ) ).", null );
	}

	/**
     * Check that tuples with various types happily co-exist in the same relation.
     */
    public void testMixedDataTypeRelation() throws Exception
    {
    	String facts =
    		"p( 'a', 'string' )." +
    		"p( 'a', 7 )." +
    		"p( 'a', _integer( 8 ) )." +
    		"p( 'a', -7.123 )." +
    		"p( 'a', _decimal( -8.123 ) )." +
    		"p( 'a', _float( -9.123 ) )." +
    		"p( 'a', 'true' )." +
    		"p( 'a', _boolean( 'false' ) )." +
    		"p( 'a', _gmonthday( 6, 7 ) )." +
    		"p( 'a', _gyearmonth( 4, 5 ) )." +
    		"p( 'a', _gyear( 5 ) )." +
    		"p( 'a', _gmonth( 4 ) )." +
    		"p( 'a', _gday( 3 ) )." +
    		"p( 'a', _duration( 1, 2, 3, 4, 5, 6) )." +
    		"p( 'a', _time( 1, 1, 1 ) )." +
    		"p( 'a', _date( 2001, 8, 1 ) )." +
    		"p( 'a', _datetime(2000,1,1,2,2,2) ).";
    		
    	String program = facts +
    		"?- p(?X, ?Y ).";
    	
       	String expectedResults = facts;
    
       	Helper.evaluateWithAllStrategies( program, expectedResults );
    }


}
