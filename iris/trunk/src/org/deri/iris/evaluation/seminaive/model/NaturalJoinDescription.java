package org.deri.iris.evaluation.seminaive.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.evaluation.seminaive.model.INaturalJoin;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.evaluation.seminaive.model.Component;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.terms.Variable;

public class NaturalJoinDescription  extends Composite implements INaturalJoin{

	private List<String> variables = new LinkedList<String>();

	NaturalJoinDescription() {
	}

	
	public String getName() {
		return "natural join";
	}

	public int getArity() {
		return variables.size();
	}

	public void addVariable(String v){
		variables.add(v);
	}
	public void addVariable(IVariable v)
	{
		addVariable(((Variable)v).getName());
	}
	
	public void addAllVariables(List<IVariable> lv){
		for (IVariable v: lv)
			addVariable(((Variable)v).getName());
		
	}
	
	public void addVariables(List<String> lv){
		for (String v: lv)
			addVariable(v);		
	}
	
	public List<String> getVariables(){
		return variables;
	}
	
	public boolean hasVariable(String v){
		return variables.contains(v);
	}

	public void addAllVariables(Set<IVariable> lv){
		for (IVariable v: lv)
			addVariable(((Variable)v).getName());				
	}
	
	public boolean addComponent(Component c)
	{
		ITree t = (ITree)c;
		if (variables.size() == 0)
			addVariables(t.getVariables());
		else {
			List<String> vl = t.getVariables();
			for (String v: vl)
				if (!variables.contains(v))
					addVariable(v);
		}
		return super.addComponent(t);
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("NATURAL_JOIN");
		buffer.append("{");
		for (int i = 0; i < this.variables.size(); i++) {
			buffer.append(this.variables.get(i).toString());
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}");
		buffer.append("\n\t{(");
		for(int i = 0; i < this.getChildren().size(); i++)
		{
			buffer.append(this.getChildren().get(i).toString());
			buffer.append("),(");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}");
		return buffer.toString();
	}
}
