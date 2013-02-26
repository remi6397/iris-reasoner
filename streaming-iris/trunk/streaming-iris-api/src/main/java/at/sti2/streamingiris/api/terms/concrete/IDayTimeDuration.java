package at.sti2.streamingiris.api.terms.concrete;

/*
 * W3C specification: http://www.w3.org/TR/xpath-functions/#dt-dayTimeDuration
 */

/**
 * <p>
 * An interface for representing the xs:dayTimeDuration data-type.
 * xs:dayTimeDuration is derived from xs:duration by restricting its lexical
 * representation to contain only the days, hours, minutes and seconds
 * components.
 * </p>
 * <p>
 * Remark: IRIS supports data types according to the standard specification for
 * primitive XML Schema data types.
 * </p>
 */
public interface IDayTimeDuration extends IDuration {

	/**
	 * Returns a canonical representation of dayTimeDuration as defined in
	 * http://www.w3.org/TR/xpath-functions/#canonical-dayTimeDuration.
	 * 
	 * @return A canonical representation of this dayTimeDuration instance.
	 */
	public IDayTimeDuration toCanonical();

}
