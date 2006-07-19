/*
 * MINS (Mins Is Not Silri) A Prolog Egine based on the Silri  
 * 
 * Copyright (C) 1999-2005  Juergen Angele and Stefan Decker
 *                          University of Innsbruck, Austria  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.deri.iris.compiler;

import java.util.*;

import org.deri.mins.Head;
import org.deri.mins.Rule;
import org.deri.mins.terms.ConstTerm;
import org.deri.mins.terms.Term;

/**
 * maps internal MINS to ids used in parsing
 *
 * <pre>
 * Created on 14.11.2005
 * Committed by $Author: darko $
 * $Source: /tmp/iris-cvsbackup/iris/parser/org/deri/iris/compiler/SymbolMap.java,v $,
 * </pre>
 *
 * @author Holger Lausen
 *
 * @version $Revision: 1.1 $ $Date: 2006-07-19 10:12:44 $
 */
public class SymbolMap {
    
    int predSymId = 100;
    int constSymId = 0; 
    
    /*
     * chache for current rule
     */
    private List<String> local_var_symbols;
    
    HashMap<Rule,List<String>> var_symbols = new HashMap<Rule,List<String>>();

    HashMap<String, Integer> p_symbol2number = new HashMap<String, Integer>();
    HashMap<Integer, String> p_number2symbol = new HashMap<Integer, String>();
    
    HashMap<String, Integer> c_symbol2number = new HashMap<String, Integer>();
    HashMap<Integer, String> c_number2symbol = new HashMap<Integer, String>();

    
    /**
     * only used by parser since it does not know rule yet needs to cache
     */
    protected int getOrAddVariableNo(String symbol){
        if (local_var_symbols == null){
            local_var_symbols = new ArrayList<String>();
        }
        int ret = local_var_symbols.indexOf(symbol);
        if (ret == -1 ){
            local_var_symbols.add(symbol);
            ret = local_var_symbols.indexOf(symbol);
        }
        return ret;
    }
    
    protected  void setRuleForLocalVars(Rule r){
        var_symbols.put(r,local_var_symbols);
        local_var_symbols = new ArrayList<String>();
    }
    
    public int getVariableNo(String symbol, Rule rule){
        List<String> local_var_symbols = var_symbols.get(rule);
        //make new List if is unkown Rule
        if (local_var_symbols == null){
            return -1;
        }
        return local_var_symbols.indexOf(symbol);
    }

    public String getVariableSymbol(int number, Rule rule){
        List<String> local_var_symbols = var_symbols.get(rule);
        if (local_var_symbols==null || number <0 || number >= local_var_symbols.size()){
            return "unkownVar for rule " +rule;
        }
        return local_var_symbols.get(number);
    }
    
    public int getConstantNo(String symbol){
        Integer ret = c_symbol2number.get(symbol);
        if (ret != null){
            return ret;
        }
        int no = constSymId++;
        c_number2symbol.put(no,symbol);
        c_symbol2number.put(symbol, no);
        return no;
    }

    public String getConstantSymbol(int number){
        return c_number2symbol.get(number);
    }

    public String getConstantSymbol(Term t){
        if(!t.isConstTerm()){
            return null;
        }
        String pars = "";
        if(t.pars.length!=0){
            pars = "(";
            for (int i=0; i<t.anzpars(); i++){
                pars += getConstantSymbol(t.pars[i]);
                if (i<t.pars.length-1) pars += ",";
            }
            pars += ")";
        }
        return getConstantSymbol(((ConstTerm)t).symbol) + pars;
    }

    public int getPredicateNo(String symbol, int arity){
        //don't give same symbol to different arity
        symbol = symbol+"/"+arity;
        Integer ret = p_symbol2number.get(symbol);
        if (ret != null){
            return ret;
        }
        int no = predSymId++;
        p_number2symbol.put(no,symbol);
        p_symbol2number.put(symbol, no);
        return no;
    }

    public String getPredicateSymbol(int number){
        return p_number2symbol.get(number);
    }
}
