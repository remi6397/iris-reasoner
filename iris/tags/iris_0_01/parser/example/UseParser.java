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

package example;

import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserImpl;
import org.deri.iris.compiler.SymbolMap;

/**
 * @author Holger Lausen
 */
public class UseParser {
	public static void main(String[] args) throws Exception {
        UseParser i = new UseParser();
        i.test();
    }

    public void test() throws Exception{
        String test = "" +
                "isa(?x,?z) :- isa(?x,?y) and sub(?y,?z). \n" +
                "sub(bigdog,dog). \n" +
                "isa(wau,bigdog). \n" +
                "hasValue(lisa,owns,wau). " +
                "";
        String qry = "?- hasValue(?person,owns,?value) and naf isa(?value,dog) . ";
        //qry="?- isa(?x,dog) .";
        
        Parser p = new ParserImpl();
        RuleSet rs = new RuleSet(new BuiltinConfig(), new DB());
        rs.setEvaluationMethod(2);
        SymbolMap sm = p.getSymbolMap();
        rs.debuglevel=0;
        p.compileKB(test,rs);
        Rule query = p.compileRule(qry);
        //System.out.println(query);
        rs.addRule(query);
        rs.evaluate();
        Substitution s = rs.getSubstitution(query);
        GroundAtom a = s.First();
        while (a!=null){
            for (int i=0; i<a.terms.length; i++){
                System.out.println(
                        sm.getVariableSymbol(i, query) + ": " +
                        sm.getConstantSymbol(a.terms[i]));
            }
            a = s.Next();
        }
    }
}
