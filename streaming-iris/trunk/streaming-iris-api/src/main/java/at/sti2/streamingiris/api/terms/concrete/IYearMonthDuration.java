package at.sti2.streamingiris.api.terms.concrete;

/*
 * W3C specification: http://www.w3.org/TR/xpath-functions/#dt-yearMonthDuration
 */

/**
 * <p>
 * An interface for representing the xs:yearMonthDuration data-type.
 * xs:yearMonthDuration is derived from xs:duration by restricting its lexical
 * representation to contain only the year and month components.
 * </p>
 * <p>
 * Remark: IRIS supports data types according to the standard specification for
 * primitive XML Schema data types.
 * </p>
 */
public interface IYearMonthDuration extends IDuration {

	/**
	 * Returns a canonical representation of yearMonthDuration as defined in
	 * http://www.w3.org/TR/xpath-functions/#canonical-yearMonthDuration.
	 * 
	 * @return A canonical representation of this yearMonthDuration instance.
	 */
	public IYearMonthDuration toCanonical();

}
