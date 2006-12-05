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

import org.deri.iris.api.*;
import org.deri.iris.api.basics.*;

/**
 * Interface or class description
 *
 * <pre>
 * Created on 16.11.2005
 * Committed by $Author: richardpoettler $
 * $Source: /tmp/iris-cvsbackup/iris/parser/org/deri/iris/compiler/Parser.java,v $,
 * </pre>
 *
 * @author Francisco Garcia
 *
 * @version $Revision: 1.4 $ $Date: 2006-12-05 08:19:50 $
 */
public interface Parser {

    /**
     * This method compiles a RuleSet from a given test input according 
     * to the following grammar:
     *      
    program      = expr* ;
    
    expr         = {rule} rule |
                   {fact} fact |
                   {query} query;
    
    rule 		= predicate t_impliedby body t_dot;
    
    fact 		= predicate t_dot;
    
    query		= t_query body t_dot;
    
    body	 	= literal |
                  {and} body t_and literal |
                  {comma} body t_comma literal;
    
    literal     = {negated} t_not predicate |
                   predicate;
                
    predicate    = t_id paramlist?;
    

    paramlist    = t_lpar termlist? t_rpar;

    termlist     = {term} term |
                   termlist t_comma term;
                   
    term         = {function} t_id paramlist |
                   {var} t_variable |
                   {constant} t_id; // constant
     
     * @param reader Reader containing a String adhering to above grammar
     * @param rs non null RuleSet that programm gets inserted
     * @throws Exception
     */
    public void compileKB(String kb, IProgram p) throws Exception;

    public IRule compileRule(String rule) throws Exception;

    public IAtom compileFact(String fact) throws Exception;

}