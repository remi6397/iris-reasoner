package at.sti2.streamingiris;

import java.util.ArrayList;
import java.util.List;

import at.sti2.streamingiris.api.IProgramOptimisation;
import at.sti2.streamingiris.evaluation.IEvaluationStrategyFactory;
import at.sti2.streamingiris.evaluation.stratifiedbottomup.StratifiedBottomUpEvaluationStrategyFactory;
import at.sti2.streamingiris.evaluation.stratifiedbottomup.seminaive.SemiNaiveEvaluatorFactory;
import at.sti2.streamingiris.facts.IDataSource;
import at.sti2.streamingiris.rules.IRuleHeadEqualityPreProcessor;
import at.sti2.streamingiris.rules.IRuleOptimiser;
import at.sti2.streamingiris.rules.IRuleReOrderingOptimiser;
import at.sti2.streamingiris.rules.IRuleSafetyProcessor;
import at.sti2.streamingiris.rules.IRuleStratifier;
import at.sti2.streamingiris.rules.IgnoreRuleHeadEquality;
import at.sti2.streamingiris.rules.optimisation.JoinConditionOptimiser;
import at.sti2.streamingiris.rules.optimisation.ReOrderLiteralsOptimiser;
import at.sti2.streamingiris.rules.optimisation.RemoveDuplicateLiteralOptimiser;
import at.sti2.streamingiris.rules.optimisation.ReplaceVariablesWithConstantsOptimiser;
import at.sti2.streamingiris.rules.ordering.SimpleReOrdering;
import at.sti2.streamingiris.rules.safety.StandardRuleSafetyProcessor;
import at.sti2.streamingiris.rules.stratification.GlobalStratifier;
import at.sti2.streamingiris.rules.stratification.LocalStratifier;
import at.sti2.streamingiris.storage.IIndexFactory;
import at.sti2.streamingiris.storage.IRelationFactory;
import at.sti2.streamingiris.storage.simple.SimpleIndexFactory;
import at.sti2.streamingiris.storage.simple.SimpleRelationFactory;
import at.sti2.streamingiris.utils.equivalence.IEquivalentTermsFactory;
import at.sti2.streamingiris.utils.equivalence.TermEquivalenceFactory;

/**
 * This class holds all configuration data for a knowledge base.
 */
public class Configuration {
	/** The evaluation strategy to use. */
	public IEvaluationStrategyFactory evaluationStrategyFactory = new StratifiedBottomUpEvaluationStrategyFactory(
			new SemiNaiveEvaluatorFactory());

	/** The port number of the socket where IRIS is listening for new data. */
	public int inputPort = 8080;

	/** The evaluation timeout in milliseconds. Zero means no timeout. */
	public int evaluationTimeoutMilliseconds = 0;

	/**
	 * The time window in milliseconds when facts become obsolete. Default is 30
	 * seconds.
	 */
	public int timeWindowMilliseconds = 30000;

	/**
	 * The time window in milliseconds when queries get periodically executed.
	 * Default is 10 seconds.
	 */
	public long executionIntervallMilliseconds = 10000;

	/**
	 * The maximum number of tuples that are allowed to be generated during
	 * evaluation before evaluation is terminated. Zero means that there is no
	 * maximum.
	 */
	public int evaluationMaxTuples = 0;

	/**
	 * The maximum complexity (tree depth) of inferred constructed terms. This
	 * is a constraint that can avoid the non-convergence problem for
	 * knowledge-bases containing rules such as: p( f(X) ) :- p( X )
	 */
	public int evaluationMaxComplexity = 0;

	/**
	 * The different options for handling divide by zero in arithmetic built-ins
	 * during evaluation.
	 */
	public static enum DivideByZeroBehaviour {
		STOP, DISCARD_AND_IGNORE
	}

	/** The desired divide-by-zero behaviour. */
	public DivideByZeroBehaviour evaluationDivideByZeroBehaviour = DivideByZeroBehaviour.DISCARD_AND_IGNORE;

	/** The factory for creating relations required during evaluation. */
	public IRelationFactory relationFactory = new SimpleRelationFactory();

	/** The factory for creating indexes required during evaluation. */
	public IIndexFactory indexFactory = new SimpleIndexFactory();

	/** The number of bits of precision to use for comparing double term values. */
	public int floatingPointDoublePrecision = 42;

	/** The number of bits of precision to use for comparing float term values. */
	public int floatingPointFloatPrecision = 19;

	/** Add external data sources here. */
	public final List<IDataSource> externalDataSources = new ArrayList<IDataSource>();

	/** The collection of rule set stratifiers. */
	public final List<IRuleStratifier> stratifiers = new ArrayList<IRuleStratifier>();

	/** The collection of rule optimizers. */
	public final List<IRuleOptimiser> ruleOptimisers = new ArrayList<IRuleOptimiser>();

	/** The collection of rule-reordering optimizers. */
	public IRuleReOrderingOptimiser reOrderingOptimiser = new SimpleReOrdering();

	/** Collection of program optimizations. */
	public final List<IProgramOptimisation> programOptmimisers = new ArrayList<IProgramOptimisation>();

	/**
	 * Rule safety processors (e.g. standard rule-safety check and
	 * augmented-unsafe-rule modifier).
	 */
	public IRuleSafetyProcessor ruleSafetyProcessor = new StandardRuleSafetyProcessor();

	/** The rule head equality pre-processor. */
	public IRuleHeadEqualityPreProcessor ruleHeadEqualityPreProcessor = new IgnoreRuleHeadEquality();

	/** The equivalent terms factory to use. */
	public IEquivalentTermsFactory equivalentTermsFactory = new TermEquivalenceFactory();

	/**
	 * Constructor.
	 */
	public Configuration() {
		stratifiers.add(new GlobalStratifier());
		stratifiers.add(new LocalStratifier(true));
		stratifiers.add(new LocalStratifier(false));

		ruleOptimisers.add(new JoinConditionOptimiser());
		ruleOptimisers.add(new ReplaceVariablesWithConstantsOptimiser());
		ruleOptimisers.add(new ReOrderLiteralsOptimiser());
		ruleOptimisers.add(new RemoveDuplicateLiteralOptimiser());
	}
}
