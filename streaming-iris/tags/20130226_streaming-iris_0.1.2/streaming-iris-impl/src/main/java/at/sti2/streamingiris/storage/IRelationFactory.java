package at.sti2.streamingiris.storage;


/**
 * Interface of all relation factories.
 */
public interface IRelationFactory {
	/**
	 * Create a new relation.
	 * 
	 * @return The new relation index.
	 */
	IRelation createRelation();
}
