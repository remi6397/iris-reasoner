/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.EvaluationException;
import org.deri.iris.optimisations.magicsets.MagicSets;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.Relations;
import org.deri.iris.storage.simple.SimpleRelationFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * </p>
 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
 */
public class StressTest extends TestCase {

	/*
	 * general generation properties
	 */

	private static final int NUMBER_OF_RULES = 50;

	private static final int NUMBER_OF_LITERALS_PER_RULE = 20;

	private static final int NUMBER_OF_NEGATED_PREDICATES = 10;

	private static final boolean RECURSIVE_RULES = true;

	/*
	 * internal generation properties
	 */

	/** Prefix for not-head variables. */
	private static final String VARIABLE_PREFIX = "X";

	/** Prefix for head variables. */
	private static final String HEAD_VARIABLE_PREFIX = "H";

	/** Prefix for edb predicates. */
	private static final String EDB_PREDICATE_PREFIX = "e";

	/** Prefix for idb predicates. */
	private static final String IDB_PREDICATE_PREFIX = "i";

	/** The maximal possible aritiy for predicates. */
	private static final int MAX_PREDICATE_ARITY = (int) Math.ceil(NUMBER_OF_LITERALS_PER_RULE / 4f);

	/** The amount of facts per predicate to be generated per edb perdicate. */
	private static final int FACTS_PER_PREDICATE = NUMBER_OF_LITERALS_PER_RULE * 10;

	/** The maximal integer value to be generated as a term. */
	private static final int TERM_RANGE = (int) Math.ceil(FACTS_PER_PREDICATE / 10f);

	/**
	 * The percentage of idb literals per rule body (the other ones will be
	 * edb literals).
	 */
	private static final int PERCENTAGE_OF_IDB_LITERALS_PER_RULE = 25;

	/** The percentage of equal variables in a rule body. */
	// TODO: introduce a new variable for the query variables
	private static final int PERCENTAGE_OF_EQUAL_VARIABLES = 25;

	private static final int PERCENTAGE_OF_HEAD_VARIABLES_PER_RULE = 25;

	private static final int PERCENTAGE_OF_IDB_CONSTANTS_IN_QUERY = 25;

	private static final int PERCENTAGE_OF_QUERY_LITERALS_TO_BODY_LITERALS = 100;

	/*
	 * dumping properties
	 */

	private static final String DUMP_DIR = System.getProperty("java.io.tmpdir")
		+ System.getProperty("file.separator")
		+ "iris"
		+ System.getProperty("file.separator")
		+  "stresstest";

	private static final boolean DUMP_PROGRAM = true;

	private static final boolean DUMP_RESULTS = true;

	private static final boolean PURGE_DUMP_DIR = true;

	static {
		purgeDumpDir();
	}

	public void temp_disable_testStress() throws EvaluationException, FileNotFoundException {
		for (int rules = 1; rules < NUMBER_OF_RULES; rules++) {
			for (int literalsPerRule = 1; literalsPerRule < NUMBER_OF_LITERALS_PER_RULE; literalsPerRule++) {
				for (int negPredicates = 0;
						(negPredicates < NUMBER_OF_NEGATED_PREDICATES) && (negPredicates < literalsPerRule);
						negPredicates++) {
					for (final boolean recursive : new boolean[]{false, true}) {
						final String filePrefix = DUMP_DIR
									+ System.getProperty("file.separator")
									+ "ru" + rules
									+ "_li" + literalsPerRule
									+ "_ne" + negPredicates
									+ "_re" + recursive;

						// construct program and query
						final EvaluationData evalData = generateProgram(rules,
								literalsPerRule,
								negPredicates,
								recursive);
						dumpProgram(evalData, filePrefix + "_prog.txt");

						// construct expected result
						final IRelation plainResult = evaluatePlain(evalData);
						dumpResults(plainResult, filePrefix + "_plain.txt");

						// construct magic result
						final IRelation magicResult = evaluateMagic(evalData);
						dumpResults(magicResult, filePrefix + "_magic.txt");

						// assert results
						assertEquals("The result of the normal evaluation and the magic evaluation differs.",
								Relations.asSet(plainResult),
								Relations.asSet(magicResult));

						if (recursive == RECURSIVE_RULES) {
							break;
						}
					}
				}
			}
		}
	}

	private static IRelation evaluateMagic(final EvaluationData data) throws EvaluationException {
		final Configuration configuration = new Configuration();
		configuration.programOptmimisers.add(new MagicSets());
		final KnowledgeBase knowledgeBase = new KnowledgeBase(data.facts, data.rules, configuration);
		return knowledgeBase.execute(data.query);
	}

	private static IRelation evaluatePlain(final EvaluationData data) throws EvaluationException {
		final KnowledgeBase knowledgeBase
			= new KnowledgeBase(data.facts, data.rules, new Configuration());
		return knowledgeBase.execute(data.query);
	}

	private static EvaluationData generateProgram(final int numberOfRules,
			final int literalsPerRule,
			final int negatedLiterals,
			final boolean recursive) {
		assert numberOfRules >= 0: "The number of rules must not be negative";
		assert literalsPerRule >= 0: "The literals per rule must not be negative";
		assert negatedLiterals >= 0: "The negated predicates must not be negative";

		// generate the predicates
		// FIXME: adjust the number of predicates generated
		final Set<IPredicate> edb = generatePredicates(literalsPerRule, EDB_PREDICATE_PREFIX);
		final Set<IPredicate> idb = generatePredicates(numberOfRules, IDB_PREDICATE_PREFIX);

		// generate the facts
		final Map<IPredicate, IRelation> facts = generateFacts(edb);

		// generate the rules
		final List<IRule> rules = generateRules(idb, edb, literalsPerRule, negatedLiterals, recursive);

		// generate the query
		final IQuery query = generateQuery(idb, literalsPerRule);

		return new EvaluationData(rules, query, facts);
	}

	private static IQuery generateQuery(final Set<IPredicate> idb, final int ruleBodyLiterals) {
		assert idb != null: "The predicate set must not be null";
		assert !idb.isEmpty(): "There must be at leas one idb predicate";
		assert ruleBodyLiterals > 0: "The number of body literals must be at least 1";

		final int numberOfQueryLiterals = Math.max(1,
				(int) Math.ceil(ruleBodyLiterals * PERCENTAGE_OF_QUERY_LITERALS_TO_BODY_LITERALS / 100f));
		final List<IPredicate> queryPredicates = pickRandom(numberOfQueryLiterals, idb);

		int numberOfNeededTerms = 0;
		for (final IPredicate predicate : queryPredicates) {
			numberOfNeededTerms += predicate.getArity();
		}
		final int numberOfConstants = Math.max(1,
				(int) Math.ceil(numberOfNeededTerms * PERCENTAGE_OF_IDB_CONSTANTS_IN_QUERY / 100f));
		final int numberOfDifferentVariables = (int) Math.ceil((numberOfNeededTerms - numberOfConstants)
				* PERCENTAGE_OF_EQUAL_VARIABLES / 100f);

		final List<ITerm> terms = new ArrayList<ITerm>(numberOfNeededTerms);
		terms.addAll(generateTuple(numberOfConstants));
		terms.addAll(pickRandom(numberOfNeededTerms - numberOfConstants,
					generateVariables(numberOfDifferentVariables, HEAD_VARIABLE_PREFIX)));
		Collections.shuffle(terms);

		final List<ILiteral> queryLiterals = new ArrayList<ILiteral>();
		final Iterator<IPredicate> predicateIterator = queryPredicates.iterator();
		final Iterator<ITerm> termIterator = terms.iterator();
		while (predicateIterator.hasNext()) {
			queryLiterals.add(createLiteral(true, predicateIterator, termIterator));
		}

		assert !queryLiterals.isEmpty(): "The query literals must not be empty";

		return BASIC.createQuery(queryLiterals);
	}

	private static Set<IPredicate> generatePredicates(final int amount, final String prefix) {
		assert amount >= 0: "The amount of predicates must not be negative";
		assert prefix != null: "The prefix must not be null";

		final Set<IPredicate> result= new HashSet<IPredicate>(amount);
		for (int i = 0; i < amount; i++) {
			result.add(BASIC.createPredicate(prefix + i, i % MAX_PREDICATE_ARITY + 1));
		}
		return result;
	}

	private static Map<IPredicate, IRelation> generateFacts(final Collection<IPredicate> predicates) {
		assert predicates != null: "The predicates must not be null";

		final Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>();
		for (final IPredicate predicate : predicates) {
			final IRelation relation = new SimpleRelationFactory().createRelation();
			for (int i = 0; i < FACTS_PER_PREDICATE; i++) {
				relation.add(generateTuple(predicate.getArity()));
			}
			facts.put(predicate, relation);
		}

		return facts;
	}

	private static ITuple generateTuple(final int arity) {
		assert arity >= 0: "The arity must not be negative";

		final Random random = new Random();
		final ITerm[] terms = new ITerm[arity];
		for (int i = 0; i < arity; i++) {
			terms[i] = CONCRETE.createInteger(random.nextInt(TERM_RANGE));
		}

		return BASIC.createTuple(terms);
	}

	private static List<IRule> generateRules(final Set<IPredicate> idb,
			final Set<IPredicate> edb,
			final int literalsPerRule,
			final int negatedLiterals,
			final boolean recursive) {
		assert edb != null: "The edb predicates must not be null";
		assert idb != null: "The idb predicates must not be null";
		assert negatedLiterals >= 0: "The negated literals must not be negative";
		assert literalsPerRule >= 0: "The literals per rule must not be negative";
		assert negatedLiterals < literalsPerRule:
			"The number of negated literals must be smaller than the number of literals in the body";

		final List<IRule> result = new ArrayList<IRule>();

		final int idbLiteralsPerRule = recursive
			? (int) Math.ceil(literalsPerRule * PERCENTAGE_OF_IDB_LITERALS_PER_RULE / 100f)
			: 0;
		// edb literals per rule
		final int edbLiteralsPerRule = literalsPerRule - idbLiteralsPerRule;

		// constructing the rules
		for (final IPredicate headPredicate : idb) {
			// constructing the head
			final ILiteral headLiteal = generateHead(headPredicate);
			// constructing the body
			final List<ILiteral> bodyLiterals = generateBody(idbLiteralsPerRule,
					idb,
					edbLiteralsPerRule,
					edb,
					headLiteal.getAtom().getTuple().getVariables(),
					negatedLiterals);
			result.add(BASIC.createRule(Arrays.asList(headLiteal), bodyLiterals));
		}

		return result;
	}

	private static ILiteral generateHead(final IPredicate headPredicate) {
		assert headPredicate != null: "The head predicate must not be null";

		return BASIC.createLiteral(true, headPredicate,
				BASIC.createTuple(generateVariables(headPredicate.getArity(),
						HEAD_VARIABLE_PREFIX)));
	}

	private static List<ITerm> generateVariables(final int amount, final String prefix) {
		assert amount >= 0: "The amount must not be negative";
		assert prefix != null: "The prefix must not be null";

		final List<ITerm> vars = new ArrayList<ITerm>(amount);
		for (int i = 0; i < amount; i++) {
			vars.add(TERM.createVariable(prefix + i));
		}
		return vars;
	}

	private static List<ILiteral> generateBody(final int idb,
			final Set<IPredicate> idbPredicates,
			final int edb,
			final Set<IPredicate> edbPredicates,
			final Set<IVariable> headVariables,
			final int negativeLiterals) {
		assert idb >= 0: "The amount of idb literals must not be negative";
		assert idbPredicates != null: "The idb predicates must not be null";
		assert edb >= 0: "The amount of edb literals must not be negative";
		assert edbPredicates != null: "The edb predicates must not be null";
		assert headVariables != null: "The head variables must not be null";
		assert negativeLiterals >= 0: "The amount of negative literals must not be null";

		// determine all predicates
		final List<IPredicate> predicates = new ArrayList<IPredicate>(idb + edb);
		predicates.addAll(pickRandom(idb, idbPredicates));
		predicates.addAll(pickRandom(edb, edbPredicates));

		assert predicates.size() == idb + edb: "Not enough predicates generated";

		// determine all variables
		int allVariables = 0;
		for (final IPredicate p : predicates) {
			allVariables += p.getArity();
		}

		// calculates the number of different variables to use
		final int differentVariables = (int) Math.ceil(allVariables * (100 - PERCENTAGE_OF_EQUAL_VARIABLES) / 100f);
		// calculates how many different non-head variables to use
		final int differentOrdinaryVariables = Math.max(differentVariables - headVariables.size(), 0);

		// the real number of head variables to use
		final int numberOfHeadVariables = Math.max(headVariables.size(),
			(int) Math.ceil(differentVariables * PERCENTAGE_OF_HEAD_VARIABLES_PER_RULE / 100f));
		// the real number of non-head variables to use
		final int numberOfOrdinaryVariables = Math.max(allVariables - numberOfHeadVariables, 0);

		final List<ITerm> variables = new ArrayList<ITerm>(allVariables);
		variables.addAll(pickRandom(numberOfHeadVariables, headVariables));
		variables.addAll(pickRandom(numberOfOrdinaryVariables,
				generateVariables(differentOrdinaryVariables, VARIABLE_PREFIX)));

		// shuffle the lists
		final Iterator<IPredicate> predicateIterator = randomCycleIterator(predicates);
		final Iterator<ITerm> variableIterator = randomCycleIterator(variables);

		// put all together
		final List<ILiteral> literals = new ArrayList<ILiteral>(idb + edb);
		final Set<ITerm> remainingHeadVariables = new HashSet<ITerm>(headVariables);
		int termCounter = 0;
		// create the positive literals
		for (int positive = edb + idb - negativeLiterals; termCounter < positive; termCounter++) {
			final ILiteral literal = createLiteral(true, predicateIterator, variableIterator);
			remainingHeadVariables.removeAll(literal.getAtom().getTuple().getVariables());
			literals.add(literal);
		}
		// create the negative literals
		for (int end = edb + idb; termCounter < end; termCounter++) {
			literals.add(createLiteral(false, predicateIterator, variableIterator));
		}
		// ensure, that all head variables appear in positive literals
		final Iterator<ITerm> remainingHeadVariablesIterator = remainingHeadVariables.iterator();
		while (remainingHeadVariablesIterator.hasNext()) {
			final Iterator<ITerm> tmpVariables = new TreeIterator<ITerm>(remainingHeadVariablesIterator, variableIterator);
			literals.add(createLiteral(true, predicateIterator, tmpVariables));
		}

		Collections.shuffle(literals);
		return literals;
	}

	private static ILiteral createLiteral(final boolean positive, final Iterator<IPredicate> predicates, final Iterator<ITerm> variables) {
		final IPredicate predicate = predicates.next();
		// create the tuple
		final ITerm[] terms = new ITerm[predicate.getArity()];
		for (int i = 0; i < terms.length; i++) {
			terms[i] = variables.next();
		}
		final ITuple tuple = BASIC.createTuple(terms);
		// create the literal
		return BASIC.createLiteral(true, predicate, tuple);
	}

	private static <Type> List<Type> pickRandom(final int amount, final Collection<Type> collection) {
		assert collection != null: "The collection must not be null";
		assert amount >= 0: "The amount must not be negative";

		final int max = (amount < 0) ? collection.size() : amount;

		final List<Type> result = new ArrayList<Type>(amount);
		final Iterator<Type> iterator = randomCycleIterator(collection);
		for (int i = 0; (i < max) && iterator.hasNext(); i++) {
			result.add(iterator.next());
		}
		return result;
	}

	private static <Type> Iterator<Type> randomCycleIterator(final Collection<Type> collection) {
		assert collection != null: "The collection must not be null";

		final List<Type> list = new ArrayList<Type>(collection);
		Collections.shuffle(list);
		return new CycleIterator<Type>(list);
	}

	private static void dumpResults(final IRelation relation, final String path)
		throws FileNotFoundException {
		assert relation != null: "The relation must not be null";
		assert path != null: "The file must not be null";

		if (!DUMP_RESULTS) {
			return;
		}

		final File file = new File(path);
		// create the parent directories
		file.getParentFile().mkdirs();
		// write the file
		final PrintStream ps = new PrintStream(file);
		for (final ITuple tuple : Relations.asList(relation)) {
			ps.println(tuple);
		}
		ps.close();
	}

	private static void dumpProgram(final EvaluationData data, final String path)
		throws FileNotFoundException {
		assert data != null: "The data must not be null";
		assert path != null: "The file must not be null";

		if (!DUMP_PROGRAM) {
			return;
		}

		final File file = new File(path);
		// create the parent directories
		file.getParentFile().mkdirs();
		// write the file
		final PrintStream ps = new PrintStream(file);
		ps.println(data.toString(System.getProperty("line.separator")));
		ps.close();
	}

	private static void purgeDumpDir() {
		if (!PURGE_DUMP_DIR) {
			return;
		}

		deleteRecursively(new File(DUMP_DIR));
	}

	private static void deleteRecursively(final File file) {
		assert file != null: "The file must not be null";

		if (file.isDirectory()) {
			for (final File f : file.listFiles()) {
				deleteRecursively(f);
			}
		} else if (file.isFile()) {
			file.delete();
		}
	}

	public static Test suite() {
		return new TestSuite(StressTest.class, StressTest.class.getSimpleName());
	}

	private static class CycleIterator<Type> implements Iterator<Type> {

		private final Collection<Type> collection;
		private Iterator<Type> iterator;

		public CycleIterator(final Collection<Type> collection) {
			if (collection == null) {
				throw new IllegalArgumentException("The collection must not be null.");
			}

			this.collection = collection;
			this.iterator = collection.iterator();
		}

		public boolean hasNext() {
			return !collection.isEmpty();
		}

		public Type next() {
			if (!iterator.hasNext()) { // renew the iterator
				iterator = collection.iterator();
			}
			return iterator.next();
		}

		public void remove() {
			throw new UnsupportedOperationException("not implemented yet");
		}
	}

	private static class TreeIterator<Type> implements Iterator<Type> {

		private int nextIndex = 1;
		private final Iterator<Type>[] iterators;
		private Iterator<Type> actual;

		public TreeIterator(final Iterator<Type>... iterators) {
			if (iterators == null) {
				throw new IllegalArgumentException("The iterators must not be null.");
			}
			if (iterators.length <= 0) {
				throw new IllegalArgumentException("There must be at least one iterator given.");
			}

			this.iterators = iterators;
			this.actual = iterators[0];
		}

		private void checkNextIterator() {
			while (!actual.hasNext() && (nextIndex < iterators.length)) {
				actual = iterators[nextIndex++];
			}
		}

		public boolean hasNext() {
			checkNextIterator();
			return actual.hasNext();
		}

		public Type next() {
			checkNextIterator();
			return actual.next();
		}

		public void remove() {
			throw new UnsupportedOperationException("not implemented yet");
		}
	}

	private static class EvaluationData {

		public final List<IRule> rules;

		public final IQuery query;

		public final Map<IPredicate, IRelation> facts;

		public EvaluationData(final List<IRule> rules, final IQuery query, final Map<IPredicate, IRelation> facts) {
			this.rules = rules;
			this.query = query;
			this.facts = facts;
		}

		public String toString() {
			return toString(null);
		}

		public String toString(final String delimiter) {
			final String delim = (delimiter == null) ? "" : delimiter;
			final StringBuilder buffer = new StringBuilder();

			if (rules != null) {
				for (final IRule rule : rules) {
					buffer.append(rule).append(delim);
				}
			}
			if (query != null) {
				buffer.append(query).append(delim);
			}
			if (facts != null) {
				for (final Map.Entry<IPredicate, List<ITuple>> entry
						: Relations.toPredicateListMapping(facts).entrySet()) {
					for (final ITuple tuple : entry.getValue()) {
						buffer.append(entry.getKey()).append(tuple).append(".");
						buffer.append(delim);
					}
				}
			}
			return buffer.toString();
		}
	}
}
