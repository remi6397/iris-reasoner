package at.sti2.streamingiris.storage.simple;

import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.storage.IRelationFactory;

/**
 * Factory for simple relations.
 */
public class SimpleRelationFactory implements IRelationFactory {
	public IRelation createRelation() {
		return new SimpleRelation();
	}
}
