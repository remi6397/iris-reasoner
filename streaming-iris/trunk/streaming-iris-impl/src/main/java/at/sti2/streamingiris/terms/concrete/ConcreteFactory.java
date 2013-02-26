package at.sti2.streamingiris.terms.concrete;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.factory.IConcreteFactory;
import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.concrete.IAnyURI;
import at.sti2.streamingiris.api.terms.concrete.IBase64Binary;
import at.sti2.streamingiris.api.terms.concrete.IBooleanTerm;
import at.sti2.streamingiris.api.terms.concrete.IByteTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTerm;
import at.sti2.streamingiris.api.terms.concrete.IDateTime;
import at.sti2.streamingiris.api.terms.concrete.IDayTimeDuration;
import at.sti2.streamingiris.api.terms.concrete.IDecimalTerm;
import at.sti2.streamingiris.api.terms.concrete.IDoubleTerm;
import at.sti2.streamingiris.api.terms.concrete.IDuration;
import at.sti2.streamingiris.api.terms.concrete.IENTITY;
import at.sti2.streamingiris.api.terms.concrete.IFloatTerm;
import at.sti2.streamingiris.api.terms.concrete.IGDay;
import at.sti2.streamingiris.api.terms.concrete.IGMonth;
import at.sti2.streamingiris.api.terms.concrete.IGMonthDay;
import at.sti2.streamingiris.api.terms.concrete.IGYear;
import at.sti2.streamingiris.api.terms.concrete.IGYearMonth;
import at.sti2.streamingiris.api.terms.concrete.IHexBinary;
import at.sti2.streamingiris.api.terms.concrete.IID;
import at.sti2.streamingiris.api.terms.concrete.IIDREF;
import at.sti2.streamingiris.api.terms.concrete.IIntTerm;
import at.sti2.streamingiris.api.terms.concrete.IIntegerTerm;
import at.sti2.streamingiris.api.terms.concrete.IIri;
import at.sti2.streamingiris.api.terms.concrete.ILanguage;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.api.terms.concrete.ILocal;
import at.sti2.streamingiris.api.terms.concrete.ILongTerm;
import at.sti2.streamingiris.api.terms.concrete.INCName;
import at.sti2.streamingiris.api.terms.concrete.INMTOKEN;
import at.sti2.streamingiris.api.terms.concrete.INOTATION;
import at.sti2.streamingiris.api.terms.concrete.IName;
import at.sti2.streamingiris.api.terms.concrete.INegativeInteger;
import at.sti2.streamingiris.api.terms.concrete.INonNegativeInteger;
import at.sti2.streamingiris.api.terms.concrete.INonPositiveInteger;
import at.sti2.streamingiris.api.terms.concrete.INormalizedString;
import at.sti2.streamingiris.api.terms.concrete.IPlainLiteral;
import at.sti2.streamingiris.api.terms.concrete.IPositiveInteger;
import at.sti2.streamingiris.api.terms.concrete.IQName;
import at.sti2.streamingiris.api.terms.concrete.IShortTerm;
import at.sti2.streamingiris.api.terms.concrete.ISqName;
import at.sti2.streamingiris.api.terms.concrete.ITime;
import at.sti2.streamingiris.api.terms.concrete.IToken;
import at.sti2.streamingiris.api.terms.concrete.IUnsignedByte;
import at.sti2.streamingiris.api.terms.concrete.IUnsignedInt;
import at.sti2.streamingiris.api.terms.concrete.IUnsignedLong;
import at.sti2.streamingiris.api.terms.concrete.IUnsignedShort;
import at.sti2.streamingiris.api.terms.concrete.IXMLLiteral;
import at.sti2.streamingiris.api.terms.concrete.IYearMonthDuration;

/**
 * <p>
 * Factory to create concrete terms.
 * </p>
 */
public class ConcreteFactory implements IConcreteFactory {

	private static final IConcreteFactory FACTORY = new ConcreteFactory();

	private ConcreteFactory() {
		// this is a singleton
	}

	public static IConcreteFactory getInstance() {
		return FACTORY;
	}

	public IBase64Binary createBase64Binary(final String s) {
		return new Base64Binary(s);
	}

	public IBooleanTerm createBoolean(final boolean b) {
		return new BooleanTerm(b);
	}

	public IBooleanTerm createBoolean(final String value) {
		return new BooleanTerm(value);
	}

	public IDateTerm createDate(final int year, final int month, final int day) {
		return new DateTerm(year, month, day);
	}

	public IDateTerm createDate(final int year, final int month, final int day,
			final int tzHour, final int tzMinute) {
		return new DateTerm(year, month, day, tzHour, tzMinute);
	}

	public IDateTime createDateTime(int year, int month, int day, int hour,
			int minute, int second, int millisecond, int tzHour, int tzMinute) {
		return new DateTime(year, month, day, hour, minute, second,
				millisecond, tzHour, tzMinute);
	}

	public IDateTime createDateTime(int year, int month, int day, int hour,
			int minute, double second, int tzHour, int tzMinute) {
		return new DateTime(year, month, day, hour, minute, second, tzHour,
				tzMinute);
	}

	public ITime createTime(final int hour, final int minute, final int second,
			final int millisecond, final int tzHour, final int tzMinute) {
		return new Time(hour, minute, second, millisecond, tzHour, tzMinute);
	}

	public ITime createTime(int hour, int minute, double second, int tzHour,
			int tzMinute) {
		return new Time(hour, minute, second, tzHour, tzMinute);
	}

	public IDecimalTerm createDecimal(double d) {
		return new DecimalTerm(d);
	}

	public IDecimalTerm createDecimal(BigDecimal value) {
		return new DecimalTerm(value);
	}

	public IDoubleTerm createDouble(final double d) {
		return new DoubleTerm(d);
	}

	public IDuration createDuration(boolean positive, int year, int month,
			int day, int hour, int minute, int second, int millisecond) {
		return new Duration(positive, year, month, day, hour, minute, second,
				millisecond);
	}

	public IDuration createDuration(boolean positive, int year, int month,
			int day, int hour, int minute, double second) {
		return new Duration(positive, year, month, day, hour, minute, second);
	}

	public IDuration createDuration(final long millis) {
		return new Duration(millis);
	}

	public IFloatTerm createFloat(final float f) {
		return new FloatTerm(f);
	}

	public IGDay createGDay(final int day) {
		return new GDay(day);
	}

	public IGMonthDay createGMonthDay(final int month, final int day) {
		return new GMonthDay(month, day);
	}

	public IGMonth createGMonth(final int month) {
		return new GMonth(month);
	}

	public IGYearMonth createGYearMonth(final int year, final int month) {
		return new GYearMonth(year, month);
	}

	public IGYear createGYear(final int year) {
		return new GYear(year);
	}

	public IHexBinary createHexBinary(final String s) {
		return new HexBinary(s);
	}

	public IIntegerTerm createInteger(int i) {
		return new IntegerTerm(i);
	}

	public IIntegerTerm createInteger(BigInteger i) {
		return new IntegerTerm(i);
	}

	public IIri createIri(final String s) {
		return new Iri(s);
	}

	public ISqName createSqName(final String s) {
		return new SqName(s);
	}

	public ISqName createSqName(final IIri iri, final String name) {
		return new SqName(iri, name);
	}

	public IPlainLiteral createPlainLiteral(final String string,
			final String language) {
		return new PlainLiteral(string, language);
	}

	public IPlainLiteral createPlainLiteral(final String string) {
		return new PlainLiteral(string);
	}

	public IXMLLiteral createXMLLiteral(String string) {
		return new XMLLiteral(string);
	}

	public IXMLLiteral createXMLLiteral(String string, String lang) {
		return new XMLLiteral(string, lang);
	}

	public IYearMonthDuration createYearMonthDuration(boolean positive,
			int year, int month) {
		return new YearMonthDuration(positive, year, month);
	}

	public IDayTimeDuration createDayTimeDuration(boolean positive, int day,
			int hour, int minute, double second) {
		return new DayTimeDuration(positive, day, hour, minute, second);
	}

	public IDayTimeDuration createDayTimeDuration(boolean positive, int day,
			int hour, int minute, int second, int millisecond) {
		return new DayTimeDuration(positive, day, hour, minute, second,
				millisecond);
	}

	public IAnyURI createAnyURI(URI uri) {
		return new AnyURI(uri);
	}

	public IByteTerm createByte(byte value) {
		return new ByteTerm(value);
	}

	public IENTITY createEntity(String entity) {
		return new ENTITY(entity);
	}

	public IID createID(String id) {
		return new ID(id);
	}

	public IIDREF createIDREF(String idRef) {
		return new IDREF(idRef);
	}

	public ILanguage createLanguage(String language) {
		return new Language(language);
	}

	public IList createList(IConcreteTerm... terms) {
		if (terms == null) {
			return new List();
		} else {
			return new List(terms);
		}
	}

	public ILocal createLocal(String value, Object context) {
		return new Local(value, context);
	}

	public ILongTerm createLong(long value) {
		return new LongTerm(value);
	}

	public INCName createNCName(String name) {
		return new NCName(name);
	}

	public INMTOKEN createNMTOKEN(String token) {
		return new NMTOKEN(token);
	}

	public IName createName(String name) {
		return new Name(name);
	}

	public INegativeInteger createNegativeInteger(BigInteger value) {
		return new NegativeInteger(value);
	}

	public INonNegativeInteger createNonNegativeInteger(BigInteger value) {
		return new NonNegativeInteger(value);
	}

	public INonPositiveInteger createNonPositiveInteger(BigInteger value) {
		return new NonPositiveInteger(value);
	}

	public INormalizedString createNormalizedString(String string) {
		return new NormalizedString(string);
	}

	public IPositiveInteger createPositiveInteger(BigInteger value) {
		return new PositiveInteger(value);
	}

	public IShortTerm createShort(short value) {
		return new ShortTerm(value);
	}

	public IToken createToken(String token) {
		return new Token(token);
	}

	public IUnsignedByte createUnsignedByte(short value) {
		return new UnsignedByte(value);
	}

	public IUnsignedInt createUnsignedInt(long value) {
		return new UnsignedInt(value);
	}

	public IUnsignedLong createUnsignedLong(long value) {
		return new UnsignedLong(BigInteger.valueOf(value));
	}

	public IUnsignedLong createUnsignedLong(BigInteger value) {
		return new UnsignedLong(value);
	}

	public IUnsignedShort createUnsignedShort(int value) {
		return new UnsignedShort(value);
	}

	public IIntTerm createInt(int value) {
		return new IntTerm(value);
	}

	public IDateTime createDateTimeStamp(int year, int month, int day,
			int hour, int minute, double second, int tzHour, int tzMinute) {
		return new DateTimeStamp(year, month, day, hour, minute, second,
				tzHour, tzMinute);
	}

	public INOTATION createNOTATION(String namespaceName, String localPart) {
		return new NOTATION(namespaceName, localPart);
	}

	public IQName createQName(String namespaceName, String localPart) {
		return new QName(namespaceName, localPart);
	}
}
