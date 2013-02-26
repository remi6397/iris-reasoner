package at.sti2.streamingiris.evaluation.equivalence;

import java.util.List;

import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.facts.IFacts;

/**
 * @author Adrian Marte
 */
public class Program {

	private List<IRule> rules;

	private IFacts facts;

	private List<IQuery> queries;

	public Program(IFacts facts, List<IRule> rules, List<IQuery> queries) {
		this.rules = rules;
		this.facts = facts;
		this.queries = queries;
	}

	public List<IRule> getRules() {
		return rules;
	}

	public void setRules(List<IRule> rules) {
		this.rules = rules;
	}

	public IFacts getFacts() {
		return facts;
	}

	public void setFacts(IFacts facts) {
		this.facts = facts;
	}

	public List<IQuery> getQueries() {
		return queries;
	}

	public void setQueries(List<IQuery> queries) {
		this.queries = queries;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Facts: ");
		buffer.append(facts);
		buffer.append("\n");

		buffer.append("Rules: ");
		buffer.append(rules);
		buffer.append("\n");

		buffer.append("Queries: ");
		buffer.append(queries);
		buffer.append("\n");

		return buffer.toString();
	}

}
