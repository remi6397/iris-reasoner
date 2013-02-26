package at.sti2.streamingiris.optimisations.magicsets;

/**
 * <p>
 * Represents a state of an adornment.
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
 */
public enum Adornment {
	BOUND("b"), FREE("f");

	private final String representation;

	private Adornment(final String representation) {
		this.representation = representation;
	}

	/**
	 * Returns a string representation of this adornment.
	 * 
	 * @return b for BOUND and f for FREE
	 */
	public String toString() {
		return representation;
	}
}
