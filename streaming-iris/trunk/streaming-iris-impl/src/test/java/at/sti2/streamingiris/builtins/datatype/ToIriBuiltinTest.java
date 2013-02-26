package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.ISqName;

/**
 * Test for ToIriBuiltin.
 */
public class ToIriBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public ToIriBuiltinTest(String name) {
		super(name);
	}

	public void testBase64() throws EvaluationException {
		fails(CONCRETE.createBase64Binary("c3VyZS4="));
	}

	public void testBoolean() throws EvaluationException {
		fails(CONCRETE.createBoolean(true));
	}

	public void testDate() throws EvaluationException {
		fails(CONCRETE.createDate(2009, 4, 28));
	}

	public void testDateTime() throws EvaluationException {
		fails(CONCRETE.createDateTime(2009, 04, 28, 8, 36, 25, -3, -33));
	}

	public void testDayTimeDuration() throws EvaluationException {
		fails(CONCRETE.createDayTimeDuration(false, 2, 2, 3, 0));
	}

	public void testDecimal() throws EvaluationException {
		fails(CONCRETE.createDecimal(1.337));
	}

	public void testDouble() throws EvaluationException {
		fails(CONCRETE.createDouble(1.3));
	}

	public void testDuration() throws EvaluationException {
		fails(CONCRETE.createDuration(true, 2, 1, 0, 5, 4, 2.3));
	}

	public void testFloat() throws EvaluationException {
		fails(CONCRETE.createFloat(1.3f));
	}

	public void testGDay() throws EvaluationException {
		fails(CONCRETE.createGDay(27));
	}

	public void testGMonth() throws EvaluationException {
		fails(CONCRETE.createGMonth(4));
	}

	public void testGMonthDay() throws EvaluationException {
		fails(CONCRETE.createGMonthDay(4, 27));
	}

	public void testGYear() throws EvaluationException {
		fails(CONCRETE.createGYear(2009));
	}

	public void testGYearMonth() throws EvaluationException {
		fails(CONCRETE.createGYearMonth(2009, 4));
	}

	public void testHexBinary() throws EvaluationException {
		fails(CONCRETE.createHexBinary("0FB7"));
	}

	public void testInteger() throws EvaluationException {
		fails(CONCRETE.createInteger(1337));
	}

	public void testIri() throws EvaluationException {
		equals(CONCRETE.createIri("http://www.w3.org/2007/rif#iri"),
				CONCRETE.createIri("http://www.w3.org/2007/rif#iri"));
	}

	public void testSqName() throws EvaluationException {
		ISqName name = CONCRETE.createSqName(
				CONCRETE.createIri("http://www.w3.org/2002/07/owl#"), "owl");
		fails(name);
	}

	public void testString() throws EvaluationException {
		equals(CONCRETE.createIri("http://www.w3.org/2007/rif#iri"),
				TERM.createString("http://www.w3.org/2007/rif#iri"));
	}

	public void testText() throws EvaluationException {
		equals(CONCRETE.createIri("http://www.w3.org/2007/rif#iri"),
				CONCRETE.createPlainLiteral("http://www.w3.org/2007/rif#iri"));
	}

	public void testTime() throws EvaluationException {
		fails(CONCRETE.createTime(12, 45, 0, 0, 0));
	}

	public void testXMLLiteral() throws EvaluationException {
		equals(CONCRETE.createIri("http://www.w3.org/2007/rif#iri"),
				CONCRETE.createXMLLiteral("http://www.w3.org/2007/rif#iri",
						"de"));
	}

	public void testYearMonthDuration() throws EvaluationException {
		fails(CONCRETE.createYearMonthDuration(true, 2009, 4));
	}

	private void equals(ITerm expected, ITerm term) throws EvaluationException {
		ITuple expectedTuple = BASIC.createTuple(expected);
		ITuple actualTuple = compute(term);

		assertEquals(expectedTuple, actualTuple);
	}

	private void fails(ITerm term) throws EvaluationException {
		assertNull(compute(term));
	}

	private ITuple compute(ITerm term) throws EvaluationException {
		ToIriBuiltin builtin = new ToIriBuiltin(term, Y);

		ITuple arguments = BASIC.createTuple(X, Y);
		ITuple actualTuple = builtin.evaluate(arguments);

		return actualTuple;
	}

}
