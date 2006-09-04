package org.deri.iris.evaluation.seminaive.model;

import org.deri.iris.api.evaluation.seminaive.model.INaturalJoin;

public class NaturalJoin  extends Composite implements INaturalJoin{
	NaturalJoin() {}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("NATURAL_JOIN\n\t{(");
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
