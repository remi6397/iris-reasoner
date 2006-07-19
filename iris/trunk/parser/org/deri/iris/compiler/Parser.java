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

import org.deri.mins.*;

/**
 * Interface or class description
 *
 * <pre>
 * Created on 16.11.2005
 * Committed by $Author: darko $
 * $Source: /tmp/iris-cvsbackup/iris/parser/org/deri/iris/compiler/Parser.java,v $,
 * </pre>
 *
 * @author Holger Lausen
 *
 * @version $Revision: 1.2 $ $Date: 2006-07-19 10:22:29 $
 */
public interface Parser {

    /**
     * This method compiles a RuleSet from a given test input according 
     * to the following grammar:
     *      expr         = {rule} predicate t_impliedby conjunction t_dot |
     {fact} predicate t_dot |
     {query} t_query conjunction t_dot;
     
     conjunction  = {body} body |
     conjunction t_and body;
     
     body         = {negated} t_not predicate |
     predicate;
     
     predicate    = id paramlist?;
     
     id           = t_alpha t_alphanum*;
     
     paramlist    = t_lpar termlist? t_rpar;
     
     termlist     = {term} term |
     termlist t_comma term;
     
     term         = id paramlist? | //constant or fsymbol
     {var} t_variable;
     
     * @param reader Reader containing a String adhering to above grammar
     * @param rs non null RuleSet that programm gets inserted
     * @throws Exception
     */
    public void compileKB(String kb, RuleSet rs) throws Exception;

    public void setSymbolMap(SymbolMap sm);

    public SymbolMap getSymbolMap();

    public Rule compileRule(String rule) throws Exception;

    public Fact compileFact(String fact) throws Exception;

}