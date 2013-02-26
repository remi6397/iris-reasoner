package at.sti2.streamingiris.compiler;

import java.util.HashMap;
import java.util.Map;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.builtins.IBuiltinAtom;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.BooleanEqualBuiltin;
import at.sti2.streamingiris.builtins.BooleanGreaterBuiltin;
import at.sti2.streamingiris.builtins.BooleanLessBuiltin;
import at.sti2.streamingiris.builtins.BooleanNotBuiltin;
import at.sti2.streamingiris.builtins.EncodeForUriBuiltin;
import at.sti2.streamingiris.builtins.IriStringBuiltin;
import at.sti2.streamingiris.builtins.datatype.*;
import at.sti2.streamingiris.builtins.date.AddDayTimeDurationToDateBuiltin;
import at.sti2.streamingiris.builtins.date.AddDayTimeDurationToDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.AddDayTimeDurationToTimeBuiltin;
import at.sti2.streamingiris.builtins.date.AddYearMonthDurationToDateBuiltin;
import at.sti2.streamingiris.builtins.date.AddYearMonthDurationToDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.DateEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DateGreaterBuiltin;
import at.sti2.streamingiris.builtins.date.DateLessBuiltin;
import at.sti2.streamingiris.builtins.date.DateLessEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DateNotEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DateSubtractBuiltin;
import at.sti2.streamingiris.builtins.date.DateTimeEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DateTimeGreaterBuiltin;
import at.sti2.streamingiris.builtins.date.DateTimeGreaterEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DateTimeLessBuiltin;
import at.sti2.streamingiris.builtins.date.DateTimeLessEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DateTimeNotEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DateTimeSubtractBuiltin;
import at.sti2.streamingiris.builtins.date.DayFromDateBuiltin;
import at.sti2.streamingiris.builtins.date.DayFromDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.DayPartBuiltin;
import at.sti2.streamingiris.builtins.date.DayTimeDurationAddBuiltin;
import at.sti2.streamingiris.builtins.date.DayTimeDurationDivideBuiltin;
import at.sti2.streamingiris.builtins.date.DayTimeDurationDivideByDayTimeDurationBuiltin;
import at.sti2.streamingiris.builtins.date.DayTimeDurationGreaterBuiltin;
import at.sti2.streamingiris.builtins.date.DayTimeDurationGreaterEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DayTimeDurationLessBuiltin;
import at.sti2.streamingiris.builtins.date.DayTimeDurationLessEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DayTimeDurationMultiplyBuiltin;
import at.sti2.streamingiris.builtins.date.DayTimeDurationSubtractBuiltin;
import at.sti2.streamingiris.builtins.date.DaysFromDurationBuiltin;
import at.sti2.streamingiris.builtins.date.DurationEqualBuiltin;
import at.sti2.streamingiris.builtins.date.DurationNotEqualBuiltin;
import at.sti2.streamingiris.builtins.date.HourPartBuiltin;
import at.sti2.streamingiris.builtins.date.HoursFromDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.HoursFromDurationBuiltin;
import at.sti2.streamingiris.builtins.date.HoursFromTimeBuiltin;
import at.sti2.streamingiris.builtins.date.MinutePartBuiltin;
import at.sti2.streamingiris.builtins.date.MinutesFromDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.MinutesFromDurationBuiltin;
import at.sti2.streamingiris.builtins.date.MinutesFromTimeBuiltin;
import at.sti2.streamingiris.builtins.date.MonthFromDateBuiltin;
import at.sti2.streamingiris.builtins.date.MonthFromDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.MonthPartBuiltin;
import at.sti2.streamingiris.builtins.date.MonthsFromDurationBuiltin;
import at.sti2.streamingiris.builtins.date.SecondPartBuiltin;
import at.sti2.streamingiris.builtins.date.SecondsFromDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.SecondsFromTimeBuiltin;
import at.sti2.streamingiris.builtins.date.SubtractDayTimeDurationFromDateBuiltin;
import at.sti2.streamingiris.builtins.date.SubtractDayTimeDurationFromDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.SubtractDayTimeDurationFromTimeBuiltin;
import at.sti2.streamingiris.builtins.date.SubtractYearMonthDurationFromDateBuiltin;
import at.sti2.streamingiris.builtins.date.SubtractYearMonthDurationFromDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.TimeEqualBuiltin;
import at.sti2.streamingiris.builtins.date.TimeGreaterBuiltin;
import at.sti2.streamingiris.builtins.date.TimeGreaterEqualBuiltin;
import at.sti2.streamingiris.builtins.date.TimeLessBuiltin;
import at.sti2.streamingiris.builtins.date.TimeLessEqualBuiltin;
import at.sti2.streamingiris.builtins.date.TimeNotEqualBuiltin;
import at.sti2.streamingiris.builtins.date.TimeSubtractBuiltin;
import at.sti2.streamingiris.builtins.date.TimezoneFromDateBuiltin;
import at.sti2.streamingiris.builtins.date.TimezoneFromDateTimeBuiltin;
import at.sti2.streamingiris.builtins.date.TimezoneFromTimeBuiltin;
import at.sti2.streamingiris.builtins.date.TimezonePartBuiltin;
import at.sti2.streamingiris.builtins.date.YearFromDateBuiltin;
import at.sti2.streamingiris.builtins.date.YearMonthDurationAddBuiltin;
import at.sti2.streamingiris.builtins.date.YearMonthDurationDivideBuiltin;
import at.sti2.streamingiris.builtins.date.YearMonthDurationDivideByYearMonthDurationBuiltin;
import at.sti2.streamingiris.builtins.date.YearMonthDurationGreaterBuiltin;
import at.sti2.streamingiris.builtins.date.YearMonthDurationGreaterEqualBuiltin;
import at.sti2.streamingiris.builtins.date.YearMonthDurationLessBuiltin;
import at.sti2.streamingiris.builtins.date.YearMonthDurationLessEqualBuiltin;
import at.sti2.streamingiris.builtins.date.YearMonthDurationMultiplyBuiltin;
import at.sti2.streamingiris.builtins.date.YearMonthDurationSubtractBuiltin;
import at.sti2.streamingiris.builtins.date.YearPartBuiltin;
import at.sti2.streamingiris.builtins.date.YearsFromDurationBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericAddBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericDivideBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericEqualBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericGreaterBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericGreaterEqualBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericIntegerDivideBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericLessBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericLessEqualBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericModulusBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericMultiplyBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericNotEqualBuiltin;
import at.sti2.streamingiris.builtins.numeric.NumericSubtractBuiltin;
import at.sti2.streamingiris.builtins.string.MatchesLanguageRangeBuiltin;
import at.sti2.streamingiris.builtins.string.XMLLiteralEqualBuiltin;
import at.sti2.streamingiris.builtins.string.XMLLiteralNotEqualBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * <p>
 * Holds the registry of built-in atoms that the parser can use to translate
 * predicates to built-ins as the logic program is parsed.
 * </p>
 * <p>
 * Before a built-in can be recognized by the parser it must first be registered
 * with the associated BuiltinRegister. The BuiltinRegister uses instances of
 * the built-ins to get their predicate names and their class instances. The
 * BuiltinRegister basically holds a map between predicate symbol and class
 * instance. This class initialises itself with all the standard IRIS built-ins.
 * </p>
 * <p>
 * To add more, simply call registerBuiltin() with an instance of the new
 * built-in type.
 * </p>
 * <p>
 * To unregister a built-in call unregisterBuiltin(). However, beware that
 * unregistering any of the standard built-ins can lead to undefined behaviour.
 * </p>
 */
public final class BuiltinRegister {

	/**
	 * Holding all the information about the built-ins.
	 * <ul>
	 * <li>key: the name (predicate symbol of the built-in)</li>
	 * <li>value: the RegisterEntry containing the information about the
	 * built-in</li>
	 * </ul>
	 */
	private final Map<String, RegisterEntry> reg = new HashMap<String, RegisterEntry>();

	/**
	 * Constructs a new built-in register and register all the standard IRIS
	 * built-ins.
	 */
	public BuiltinRegister() {
		ITerm t1 = Factory.TERM.createVariable("a");
		ITerm t2 = Factory.TERM.createVariable("b");
		ITerm t3 = Factory.TERM.createVariable("c");
		ITerm t4 = Factory.TERM.createVariable("d");
		ITerm t5 = Factory.TERM.createVariable("e");

		registerBuiltin(new at.sti2.streamingiris.builtins.AddBuiltin(t1, t2,
				t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.SubtractBuiltin(t1,
				t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.MultiplyBuiltin(t1,
				t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.DivideBuiltin(t1,
				t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.ModulusBuiltin(t1,
				t2, t3));

		registerBuiltin(new at.sti2.streamingiris.builtins.MaxBuiltin(t1, t2,
				t3));

		registerBuiltin(new at.sti2.streamingiris.builtins.EqualBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.NotEqualBuiltin(t1,
				t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.ExactEqualBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.NotExactEqualBuiltin(
				t1, t2));

		registerBuiltin(new at.sti2.streamingiris.builtins.LessBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.LessEqualBuiltin(t1,
				t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.GreaterBuiltin(t1,
				t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.GreaterEqualBuiltin(
				t1, t2));

		registerBuiltin(new at.sti2.streamingiris.builtins.TrueBuiltin());
		registerBuiltin(new at.sti2.streamingiris.builtins.FalseBuiltin());

		registerBuiltin(new at.sti2.streamingiris.builtins.RegexBuiltin(t1,
				Factory.TERM.createString("")));
		registerBuiltin(new IriStringBuiltin(t1, t2));

		// Datatype check built-ins.
		registerBuiltin(new IsAnyURIBuiltin(t1));
		registerBuiltin(new IsBase64BinaryBuiltin(t1));
		registerBuiltin(new IsBooleanBuiltin(t1));
		registerBuiltin(new IsByteBuiltin(t1));
		registerBuiltin(new IsDateBuiltin(t1));
		registerBuiltin(new IsDateTimeBuiltin(t1));
		registerBuiltin(new IsDateTimeStampBuiltin(t1));
		registerBuiltin(new IsDayTimeDurationBuiltin(t1));
		registerBuiltin(new IsDecimalBuiltin(t1));
		registerBuiltin(new IsDoubleBuiltin(t1));
		registerBuiltin(new IsDurationBuiltin(t1));
		registerBuiltin(new IsFloatBuiltin(t1));
		registerBuiltin(new IsGDayBuiltin(t1));
		registerBuiltin(new IsGMonthBuiltin(t1));
		registerBuiltin(new IsGMonthDayBuiltin(t1));
		registerBuiltin(new IsGYearBuiltin(t1));
		registerBuiltin(new IsGYearMonthBuiltin(t1));
		registerBuiltin(new IsHexBinaryBuiltin(t1));
		registerBuiltin(new IsIDBuiltin(t1));
		registerBuiltin(new IsIDREFBuiltin(t1));
		registerBuiltin(new IsIntBuiltin(t1));
		registerBuiltin(new IsIntegerBuiltin(t1));
		registerBuiltin(new IsIriBuiltin(t1));
		registerBuiltin(new IsLanguageBuiltin(t1));
		registerBuiltin(new IsListBuiltin(t1));
		registerBuiltin(new IsLongBuiltin(t1));
		registerBuiltin(new IsNameBuiltin(t1));
		registerBuiltin(new IsNCNameBuiltin(t1));
		registerBuiltin(new IsNegativeIntegerBuiltin(t1));
		registerBuiltin(new IsNMTOKENBuiltin(t1));
		registerBuiltin(new IsNonNegativeIntegerBuiltin(t1));
		registerBuiltin(new IsNonPositiveIntegerBuiltin(t1));
		registerBuiltin(new IsNormalizedStringBuiltin(t1));
		registerBuiltin(new IsNOTATIONBuiltin(t1));
		registerBuiltin(new IsNumericBuiltin(t1));
		registerBuiltin(new IsPlainLiteralBuiltin(t1));
		registerBuiltin(new IsPositiveIntegerBuiltin(t1));
		registerBuiltin(new IsQNameBuiltin(t1));
		registerBuiltin(new IsShortBuiltin(t1));
		registerBuiltin(new IsSqNameBuiltin(t1));
		registerBuiltin(new IsStringBuiltin(t1));
		registerBuiltin(new IsTimeBuiltin(t1));
		registerBuiltin(new IsTokenBuiltin(t1));
		registerBuiltin(new IsUnsignedByteBuiltin(t1));
		registerBuiltin(new IsUnsignedIntBuiltin(t1));
		registerBuiltin(new IsUnsignedLongBuiltin(t1));
		registerBuiltin(new IsUnsignedShortBuiltin(t1));
		registerBuiltin(new IsXMLLiteralBuiltin(t1));
		registerBuiltin(new IsYearMonthDurationBuiltin(t1));

		// Negated datatype check built-ins.
		registerBuiltin(new IsNotAnyURIBuiltin(t1));
		registerBuiltin(new IsNotBase64BinaryBuiltin(t1));
		registerBuiltin(new IsNotBooleanBuiltin(t1));
		registerBuiltin(new IsNotByteBuiltin(t1));
		registerBuiltin(new IsNotDateBuiltin(t1));
		registerBuiltin(new IsNotDateTimeBuiltin(t1));
		registerBuiltin(new IsNotDateTimeStampBuiltin(t1));
		registerBuiltin(new IsNotDayTimeDurationBuiltin(t1));
		registerBuiltin(new IsNotDecimalBuiltin(t1));
		registerBuiltin(new IsNotDoubleBuiltin(t1));
		registerBuiltin(new IsNotDurationBuiltin(t1));
		registerBuiltin(new IsNotFloatBuiltin(t1));
		registerBuiltin(new IsNotGDayBuiltin(t1));
		registerBuiltin(new IsNotGMonthBuiltin(t1));
		registerBuiltin(new IsNotGMonthDayBuiltin(t1));
		registerBuiltin(new IsNotGYearBuiltin(t1));
		registerBuiltin(new IsNotGYearMonthBuiltin(t1));
		registerBuiltin(new IsNotHexBinaryBuiltin(t1));
		registerBuiltin(new IsNotIDBuiltin(t1));
		registerBuiltin(new IsNotIDREFBuiltin(t1));
		registerBuiltin(new IsNotIntBuiltin(t1));
		registerBuiltin(new IsNotIntegerBuiltin(t1));
		registerBuiltin(new IsNotIriBuiltin(t1));
		registerBuiltin(new IsNotLanguageBuiltin(t1));
		registerBuiltin(new IsNotListBuiltin(t1));
		registerBuiltin(new IsNotLongBuiltin(t1));
		registerBuiltin(new IsNotNameBuiltin(t1));
		registerBuiltin(new IsNotNCNameBuiltin(t1));
		registerBuiltin(new IsNotNegativeIntegerBuiltin(t1));
		registerBuiltin(new IsNotNMTOKENBuiltin(t1));
		registerBuiltin(new IsNotNonNegativeIntegerBuiltin(t1));
		registerBuiltin(new IsNotNonPositiveIntegerBuiltin(t1));
		registerBuiltin(new IsNotNormalizedStringBuiltin(t1));
		registerBuiltin(new IsNotNOTATIONBuiltin(t1));
		registerBuiltin(new IsNotNumericBuiltin(t1));
		registerBuiltin(new IsNotPlainLiteralBuiltin(t1));
		registerBuiltin(new IsNotPositiveIntegerBuiltin(t1));
		registerBuiltin(new IsNotQNameBuiltin(t1));
		registerBuiltin(new IsNotShortBuiltin(t1));
		registerBuiltin(new IsNotSqNameBuiltin(t1));
		registerBuiltin(new IsNotStringBuiltin(t1));
		registerBuiltin(new IsNotTimeBuiltin(t1));
		registerBuiltin(new IsNotTokenBuiltin(t1));
		registerBuiltin(new IsNotUnsignedByteBuiltin(t1));
		registerBuiltin(new IsNotUnsignedIntBuiltin(t1));
		registerBuiltin(new IsNotUnsignedLongBuiltin(t1));
		registerBuiltin(new IsNotUnsignedShortBuiltin(t1));
		registerBuiltin(new IsNotXMLLiteralBuiltin(t1));
		registerBuiltin(new IsNotYearMonthDurationBuiltin(t1));

		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.IsDatatypeBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.IsNotDatatypeBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.SameTypeBuiltin(
				t1, t2));

		// Datatype conversion builtins.
		registerBuiltin(new ToAnyURIBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToBase64Builtin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToBooleanBuiltin(
				t1, t2));
		registerBuiltin(new ToByteBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToDateBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToDateTimeBuiltin(
				t1, t2));
		registerBuiltin(new ToDateTimeStampBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToDayTimeDurationBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToDecimalBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToDoubleBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToDurationBuiltin(
				t1, t2));
		registerBuiltin(new ToENTITYBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToFloatBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToGDayBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToGMonthBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToGMonthDayBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToGYearBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToGYearMonthBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToHexBinaryBuiltin(
				t1, t2));
		registerBuiltin(new ToIDBuiltin(t1, t2));
		registerBuiltin(new ToIDREFBuiltin(t1, t2));
		registerBuiltin(new ToIntBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToIntegerBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToIriBuiltin(
				t1, t2));
		registerBuiltin(new ToLanguageBuiltin(t1, t2));
		registerBuiltin(new ToLongBuiltin(t1, t2));
		registerBuiltin(new ToNameBuiltin(t1, t2));
		registerBuiltin(new ToNCNameBuiltin(t1, t2));
		registerBuiltin(new ToNegativeIntegerBuiltin(t1, t2));
		registerBuiltin(new ToNMTokenBuiltin(t1, t2));
		registerBuiltin(new ToNonNegativeIntegerBuiltin(t1, t2));
		registerBuiltin(new ToNonPositiveIntegerBuiltin(t1, t2));
		registerBuiltin(new ToNormalizedStringBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToPlainLiteralBuiltin(
				t1, t2));
		registerBuiltin(new ToPositiveIntegerBuiltin(t1, t2));
		registerBuiltin(new ToQNameBuiltin(t1, t2));
		registerBuiltin(new ToShortBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToStringBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToTimeBuiltin(
				t1, t2));
		registerBuiltin(new ToTokenBuiltin(t1, t2));
		registerBuiltin(new ToUnsignedByteBuiltin(t1, t2));
		registerBuiltin(new ToUnsignedIntBuiltin(t1, t2));
		registerBuiltin(new ToUnsignedLongBuiltin(t1, t2));
		registerBuiltin(new ToUnsignedShortBuiltin(t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToXMLLiteralBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.datatype.ToYearMonthDurationBuiltin(
				t1, t2));

		// Date builtins.
		registerBuiltin(new AddDayTimeDurationToDateBuiltin(t1, t2, t3));
		registerBuiltin(new AddDayTimeDurationToDateTimeBuiltin(t1, t2, t3));
		registerBuiltin(new AddDayTimeDurationToTimeBuiltin(t1, t2, t3));
		registerBuiltin(new AddYearMonthDurationToDateBuiltin(t1, t2, t3));
		registerBuiltin(new AddYearMonthDurationToDateTimeBuiltin(t1, t2, t3));
		registerBuiltin(new DateEqualBuiltin(t1, t2));
		registerBuiltin(new DateGreaterBuiltin(t1, t2));
		registerBuiltin(new DateLessBuiltin(t1, t2));
		registerBuiltin(new DateLessEqualBuiltin(t1, t2));
		registerBuiltin(new DateNotEqualBuiltin(t1, t2));
		registerBuiltin(new DateSubtractBuiltin(t1, t2, t3));
		registerBuiltin(new DateTimeEqualBuiltin(t1, t2));
		registerBuiltin(new DateTimeGreaterBuiltin(t1, t2));
		registerBuiltin(new DateTimeGreaterEqualBuiltin(t1, t2));
		registerBuiltin(new DateTimeLessBuiltin(t1, t2));
		registerBuiltin(new DateTimeLessEqualBuiltin(t1, t2));
		registerBuiltin(new DateTimeNotEqualBuiltin(t1, t2));
		registerBuiltin(new DateTimeSubtractBuiltin(t1, t2, t3));
		registerBuiltin(new DayFromDateBuiltin(t1, t2));
		registerBuiltin(new DayFromDateTimeBuiltin(t1, t2));
		registerBuiltin(new DayPartBuiltin(t1, t2));
		registerBuiltin(new DaysFromDurationBuiltin(t1, t2));
		registerBuiltin(new DayTimeDurationAddBuiltin(t1, t2, t3));
		registerBuiltin(new DayTimeDurationDivideBuiltin(t1, t2, t3));
		registerBuiltin(new DayTimeDurationDivideByDayTimeDurationBuiltin(t1,
				t2, t3));
		registerBuiltin(new DayTimeDurationGreaterBuiltin(t1, t2));
		registerBuiltin(new DayTimeDurationGreaterEqualBuiltin(t1, t2));
		registerBuiltin(new DayTimeDurationLessBuiltin(t1, t2));
		registerBuiltin(new DayTimeDurationLessEqualBuiltin(t1, t2));
		registerBuiltin(new DayTimeDurationMultiplyBuiltin(t1, t2, t3));
		registerBuiltin(new DayTimeDurationSubtractBuiltin(t1, t2, t3));
		registerBuiltin(new DurationEqualBuiltin(t1, t2));
		registerBuiltin(new DurationNotEqualBuiltin(t1, t2));
		registerBuiltin(new HourPartBuiltin(t1, t2));
		registerBuiltin(new HoursFromDateTimeBuiltin(t1, t2));
		registerBuiltin(new HoursFromDurationBuiltin(t1, t2));
		registerBuiltin(new HoursFromTimeBuiltin(t1, t2));
		registerBuiltin(new MinutePartBuiltin(t1, t2));
		registerBuiltin(new MinutesFromDateTimeBuiltin(t1, t2));
		registerBuiltin(new MinutesFromDurationBuiltin(t1, t2));
		registerBuiltin(new MinutesFromTimeBuiltin(t1, t2));
		registerBuiltin(new MonthFromDateBuiltin(t1, t2));
		registerBuiltin(new MonthFromDateTimeBuiltin(t1, t2));
		registerBuiltin(new MonthPartBuiltin(t1, t2));
		registerBuiltin(new MonthsFromDurationBuiltin(t1, t2));
		registerBuiltin(new SecondPartBuiltin(t1, t2));
		registerBuiltin(new SecondsFromDateTimeBuiltin(t1, t2));
		registerBuiltin(new SecondsFromTimeBuiltin(t1, t2));
		registerBuiltin(new SubtractDayTimeDurationFromDateBuiltin(t1, t2, t3));
		registerBuiltin(new SubtractDayTimeDurationFromDateTimeBuiltin(t1, t2,
				t3));
		registerBuiltin(new SubtractDayTimeDurationFromTimeBuiltin(t1, t2, t3));
		registerBuiltin(new SubtractYearMonthDurationFromDateBuiltin(t1, t2, t3));
		registerBuiltin(new SubtractYearMonthDurationFromDateTimeBuiltin(t1,
				t2, t3));
		registerBuiltin(new TimeEqualBuiltin(t1, t2));
		registerBuiltin(new TimeGreaterBuiltin(t1, t2));
		registerBuiltin(new TimeGreaterEqualBuiltin(t1, t2));
		registerBuiltin(new TimeLessBuiltin(t1, t2));
		registerBuiltin(new TimeLessEqualBuiltin(t1, t2));
		registerBuiltin(new TimeNotEqualBuiltin(t1, t2));
		registerBuiltin(new TimeSubtractBuiltin(t1, t2, t3));
		registerBuiltin(new TimezoneFromDateBuiltin(t1, t2));
		registerBuiltin(new TimezoneFromDateTimeBuiltin(t1, t2));
		registerBuiltin(new TimezoneFromTimeBuiltin(t1, t2));
		registerBuiltin(new TimezonePartBuiltin(t1, t2));
		registerBuiltin(new YearFromDateBuiltin(t1, t2));
		registerBuiltin(new YearMonthDurationAddBuiltin(t1, t2, t3));
		registerBuiltin(new YearMonthDurationDivideBuiltin(t1, t2, t3));
		registerBuiltin(new YearMonthDurationDivideByYearMonthDurationBuiltin(
				t1, t2, t3));
		registerBuiltin(new YearMonthDurationGreaterBuiltin(t1, t2));
		registerBuiltin(new YearMonthDurationGreaterEqualBuiltin(t1, t2));
		registerBuiltin(new YearMonthDurationLessBuiltin(t1, t2));
		registerBuiltin(new YearMonthDurationLessEqualBuiltin(t1, t2));
		registerBuiltin(new YearMonthDurationMultiplyBuiltin(t1, t2, t3));
		registerBuiltin(new YearMonthDurationSubtractBuiltin(t1, t2, t3));
		registerBuiltin(new YearPartBuiltin(t1, t2));
		registerBuiltin(new YearsFromDurationBuiltin(t1, t2));

		// Numeric built-ins.
		registerBuiltin(new NumericAddBuiltin(t1, t2, t3));
		registerBuiltin(new NumericDivideBuiltin(t1, t2, t3));
		registerBuiltin(new NumericEqualBuiltin(t1, t2));
		registerBuiltin(new NumericGreaterBuiltin(t1, t2));
		registerBuiltin(new NumericGreaterEqualBuiltin(t1, t2));
		registerBuiltin(new NumericIntegerDivideBuiltin(t1, t2, t3));
		registerBuiltin(new NumericLessBuiltin(t1, t2));
		registerBuiltin(new NumericLessEqualBuiltin(t1, t2));
		registerBuiltin(new NumericModulusBuiltin(t1, t2, t3));
		registerBuiltin(new NumericMultiplyBuiltin(t1, t2, t3));
		registerBuiltin(new NumericNotEqualBuiltin(t1, t2));
		registerBuiltin(new NumericSubtractBuiltin(t1, t2, t3));

		registerBuiltin(new EncodeForUriBuiltin(t1, t2));

		// String built-ins.
		registerBuiltin(new at.sti2.streamingiris.builtins.string.LangFromPlainLiteralBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.PlainLiteralCompareBuiltin(
				t1, t2, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.PlainLiteralFromStringBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.PlainLiteralFromStringLangBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.PlainLiteralLengthBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringCompareBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringConcatBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringContainsBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringContainsWithoutCollationBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringEndsWithBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringEndsWithWithoutCollationBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringEscapeHtmlUriBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringFromPlainLiteralBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringIriToUriBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringJoinBuiltin(
				t1, t2, t3, t4));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringLengthBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringMatchesBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringMatchesWithoutFlagsBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringReplaceBuiltin(
				t1, t2, t3, t4, t5));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringReplaceWithoutFlagsBuiltin(
				t1, t2, t3, t4));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringStartsWithBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringStartsWithWithoutCollationBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringSubstringAfterBuiltin(
				t1, t2, t3, t4));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringSubstringAfterWithoutCollationBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringSubstringBeforeBuiltin(
				t1, t2, t3, t4));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringSubstringBeforeWithoutCollationBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringSubstringBuiltin(
				t1, t2, t3, t4));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringSubstringUntilEndBuiltin(
				t1, t2, t3));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringToLowerBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringToUpperBuiltin(
				t1, t2));
		registerBuiltin(new at.sti2.streamingiris.builtins.string.StringUriEncodeBuiltin(
				t1, t2));
		registerBuiltin(new XMLLiteralEqualBuiltin(t1, t2));
		registerBuiltin(new XMLLiteralNotEqualBuiltin(t1, t2));
		registerBuiltin(new MatchesLanguageRangeBuiltin(t1, t2));

		// Boolean built-ins.
		registerBuiltin(new BooleanEqualBuiltin(t1, t2));
		registerBuiltin(new BooleanGreaterBuiltin(t1, t2));
		registerBuiltin(new BooleanLessBuiltin(t1, t2));
		registerBuiltin(new BooleanNotBuiltin(t1));
	}

	/**
	 * Registers a single built-in. Already registered built-ins will be
	 * overwritten.
	 * 
	 * @param builtin
	 *            an instance of the built-in to register
	 * @throws IllegalArgumentException
	 *             if the class is <code>null</code>
	 */
	public void registerBuiltin(IBuiltinAtom builtin) {
		if (builtin == null)
			throw new IllegalArgumentException(
					"The builtin atom must not be null");

		final Class<?> c = builtin.getClass();

		final RegisterEntry ent = new RegisterEntry(c, builtin.getPredicate());
		reg.put(ent.getName(), ent);
	}

	/**
	 * De-registers a single built-in.
	 * 
	 * @param builtin
	 *            an instance of the built-in to de-register
	 * @throws IllegalArgumentException
	 *             if the class is <code>null</code>
	 */
	public void unregisterBuiltin(IBuiltinAtom builtin) {
		if (builtin == null)
			throw new IllegalArgumentException(
					"The builtin atom must not be null");

		reg.remove(builtin.getPredicate());
	}

	/**
	 * Returns the arity of a registered built-in.
	 * 
	 * @param s
	 *            the name (predicate symbol) of the built-in
	 * @return the arity, or -1 if such a built-in hasn't been registered yet
	 */
	public int getBuiltinArity(final String s) {
		final RegisterEntry re = reg.get(s);
		return (re == null) ? -1 : re.getArity();
	}

	/**
	 * Returns the class of a registered built-in.
	 * 
	 * @param s
	 *            the name (predicate symbol) of the built-in
	 * @return the class, or <code>null</code> if such a built-in hasn't been
	 *         registered yet
	 */
	public Class<?> getBuiltinClass(final String s) {
		final RegisterEntry re = reg.get(s);
		return (re == null) ? null : re.getBuiltinClass();
	}

	/**
	 * <p>
	 * Returns a short description of this object. <b>The format of the returned
	 * string is undocumented and subject to change.</b>
	 * </p>
	 * <p>
	 * An example return string could be: <code>[ADD[3,
	 * org.deri.iris.builtins.AddBuiltin],SUBTRACT[3,
	 * org.deri.builtins.SubtractBuiltin]]</code>
	 * </p>
	 * 
	 * @return the description
	 */
	public String toString() {
		return reg.values().toString();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof BuiltinRegister)) {
			return false;
		}
		return reg.equals(((BuiltinRegister) o).reg);
	}

	public int hashCode() {
		return reg.hashCode();
	}

	/**
	 * Holds various informations about a built-in.
	 */
	private static class RegisterEntry {

		/** The class of the built-in. */
		private Class<?> builtinClass;

		/** The predicate defining the built-in. */
		private IPredicate pred;

		/**
		 * Constructs a registry entry.
		 * 
		 * @param c
		 *            the class of the built-in
		 * @param p
		 *            the predicate of the built-in
		 * @throws NullPointerException
		 *             if the class is <code>null</code>
		 * @throws NullPointerException
		 *             if the predicate is <code>null</code>
		 */
		public RegisterEntry(final Class<?> c, final IPredicate p) {
			if (c == null) {
				throw new NullPointerException("The class must not be null");
			}
			if (p == null) {
				throw new NullPointerException("The predicate must not be null");
			}
			builtinClass = c;
			pred = p;
		}

		/**
		 * Returns the class of the built-in.
		 * 
		 * @return the class
		 */
		public Class<?> getBuiltinClass() {
			return builtinClass;
		}

		/**
		 * Returns the arity of the built-in.
		 * 
		 * @return the arity
		 */
		public int getArity() {
			return pred.getArity();
		}

		/**
		 * Returns the name (predicate symbol) of the built-in.
		 * 
		 * @return the name
		 */
		public String getName() {
			return pred.getPredicateSymbol();
		}

		/**
		 * <p>
		 * Returns a short description of this object. <b>The format of the
		 * returned string is undocumented and subject to change.</b>
		 * </p>
		 * <p>
		 * An example return string could be: <code>ADD[3,
		 * org.deri.iris.builtins.AddBuiltin)</code>
		 * </p>
		 * 
		 * @return the description
		 */
		public String toString() {
			return getName() + "[" + getArity() + ", " + getBuiltinClass()
					+ "]";
		}

		public boolean equals(final Object o) {
			if (!(o instanceof RegisterEntry)) {
				return false;
			}
			final RegisterEntry e = (RegisterEntry) o;
			return builtinClass.equals(e.builtinClass) && pred.equals(e.pred);
		}

		public int hashCode() {
			int res = 17;
			res = res * 37 + pred.hashCode();
			res = res * 37 + builtinClass.hashCode();
			return res;
		}
	}
}
