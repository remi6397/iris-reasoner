package at.sti2.streamingiris.api.graph;

/**
 * <p>
 * A simple implementation for a labeled edge.
 * </p>
 */
public interface ILabeledEdge<V, L> {

	public V getSource();

	public V getTarget();

	/**
	 * Returns the actual label of the edge.
	 * 
	 * @return the label
	 */
	public L getLabel();

	/**
	 * Returns whether there is actually a label set.
	 * 
	 * @return true if the label is not null, otherwise false
	 */
	public boolean hasLabel();

}
