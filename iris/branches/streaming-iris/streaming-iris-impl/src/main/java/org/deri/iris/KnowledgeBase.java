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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.demo.IIrisOutputStreamer;
import org.deri.iris.demo.IrisOutputStreamer;
import org.deri.iris.evaluation.IEvaluationStrategy;
import org.deri.iris.evaluation.OptimisedProgramStrategyAdaptor;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.FactsWithExternalData;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rules.RuleManipulator;
import org.deri.iris.storage.IRelation;
import org.deri.iris.threads.ExecutionThread;
import org.deri.iris.threads.GarbageCollectorThread;
import org.deri.iris.threads.KnowledgeBaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The concrete knowledge-base.
 */
public class KnowledgeBase implements IKnowledgeBase {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** The facts of the knowledge-base. */
	private final IFacts mFacts;

	/** The rules of the knowledge-base. */
	private final List<IRule> mRules;

	/** The configuration object for the knowledge-base. */
	private final Configuration mConfiguration;

	/** The evaluation strategy for the knowledge-base. */
	private IEvaluationStrategy mEvaluationStrategy;

	/** The queries registered at the knowledge-base. */
	private List<IQuery> mQueries;

	/** The sockets to output the results of the executed queries. */
	private Map<HostPortPair, IIrisOutputStreamer> mIrisOutputStreamers;

	/**
	 * The map containing the information which listener is registered for which
	 * query.
	 */
	private Map<HostPortPair, List<IQuery>> mListenerQueryMap;

	/** The thread that handles incoming facts. */
	private KnowledgeBaseServer inputServerThread;

	/** The thread that starts the periodically execution of the queries. */
	private ScheduledExecutorService executionExecutor;

	/** The thread that deletes obsolete facts from the knowledge-base. */
	private ScheduledExecutorService garbageCollectorExecutor;

	/**
	 * Constructor.
	 * 
	 * @param facts
	 *            The starting facts for the knowledge-base.
	 * @param rules
	 *            The rules of the knowledge-base.
	 * @param configuration
	 *            The configuration object for the knowledge-base.
	 * @throws EvaluationException
	 * @throws EvaluationException
	 */
	public KnowledgeBase(Map<IPredicate, IRelation> inputFacts,
			List<IRule> rules, Configuration configuration)
			throws EvaluationException {
		mQueries = new ArrayList<IQuery>();
		mIrisOutputStreamers = new HashMap<HostPortPair, IIrisOutputStreamer>();
		mListenerQueryMap = new HashMap<HostPortPair, List<IQuery>>();

		if (inputFacts == null)
			inputFacts = new HashMap<IPredicate, IRelation>();

		if (rules == null)
			rules = new ArrayList<IRule>();

		if (configuration == null)
			configuration = new Configuration();

		mConfiguration = configuration;

		// Store the configuration object against the current thread.
		ConfigurationThreadLocalStorage.setConfiguration(mConfiguration);

		// Set up the rule-base
		mRules = rules;

		// Set up the facts object(s)
		IFacts facts = new Facts(inputFacts, mConfiguration.relationFactory);

		if (mConfiguration.externalDataSources.size() > 0)
			facts = new FactsWithExternalData(facts,
					mConfiguration.externalDataSources);

		mFacts = facts;

		if (logger.isDebugEnabled()) {
			logger.debug("IRIS knowledge-base init");
			logger.debug("========================");

			for (IRule rule : rules) {
				logger.debug(rule.toString());
			}

			logger.debug("------------------------");

			for (IPredicate f : mFacts.getPredicates()) {
				IRelation relation = mFacts.get(f);
				for (int i = 0; i < relation.size(); i++) {
					ITuple tuple = relation.get(i);
					logger.debug("{} {}", f, tuple);
				}
			}

			logger.debug("------------------------");

		}

		// Store the configuration object against the current thread.
		ConfigurationThreadLocalStorage.setConfiguration(mConfiguration);

		// initialize the evaluation strategy with the updated facts.
		if (mConfiguration.programOptmimisers.size() > 0)
			mEvaluationStrategy = new OptimisedProgramStrategyAdaptor(mFacts,
					mRules, mConfiguration);
		else
			mEvaluationStrategy = mConfiguration.evaluationStrategyFactory
					.createEvaluator(mFacts, mRules, mConfiguration);

		inputServerThread = new KnowledgeBaseServer(this,
				mConfiguration.inputPort);
		inputServerThread.start();

		garbageCollectorExecutor = Executors.newSingleThreadScheduledExecutor();
		garbageCollectorExecutor.scheduleAtFixedRate(
				new GarbageCollectorThread(this),
				mConfiguration.executionIntervallMilliseconds,
				mConfiguration.executionIntervallMilliseconds,
				TimeUnit.MILLISECONDS);

		executionExecutor = Executors.newSingleThreadScheduledExecutor();
		executionExecutor.scheduleAtFixedRate(new ExecutionThread(this),
				mConfiguration.executionIntervallMilliseconds / 2,
				mConfiguration.executionIntervallMilliseconds,
				TimeUnit.MILLISECONDS);
	}

	@Override
	public void shutdown() {
		try {
			logger.info("Knowledge-Base shutting down ...");

			// Shut down the input streamer.
			if (!inputServerThread.shutdown())
				logger.error("InputStream could not be shut down!");

			// Shut down the executor.
			executionExecutor.shutdownNow();
			try {
				executionExecutor.awaitTermination(10, TimeUnit.SECONDS);
				logger.info("Execution thread shut down!");
			} catch (InterruptedException e) {
				logger.error("Execution thread could not be shut down!");

			}

			// Shut down the garbage collector.
			garbageCollectorExecutor.shutdownNow();
			try {
				garbageCollectorExecutor.awaitTermination(10, TimeUnit.SECONDS);
				logger.info("Garbage collector thread shut down!");
			} catch (InterruptedException e) {
				logger.error("Garbage collector thread could not be shut down!");

			}

			// Shut down the output streamer.
			for (IIrisOutputStreamer streamer : mIrisOutputStreamers.values()) {
				if (!streamer.shutdown())
					logger.error("IIrisOutputStreamer could not be shut down!");
				else
					logger.info("IIrisOutputStreamer shut down!");
			}
		} catch (Exception le) {
			logger.error("Exception occured!", le);
		}
	}

	@Override
	public void addFacts(Map<IPredicate, IRelation> newFacts)
			throws EvaluationException {
		long timestamp;
		synchronized (mFacts) {
			timestamp = System.currentTimeMillis()
					+ mConfiguration.timeWindowMilliseconds;

			mFacts.addFacts(newFacts, timestamp);

			// FIXME Norbert: does only work with
			// StratifiedBottomUpEvaluationStrategy
			mEvaluationStrategy.evaluateRules(mFacts, timestamp);
		}

		Set<IPredicate> predicates = newFacts.keySet();
		for (IPredicate predicate : predicates) {
			IRelation relation = newFacts.get(predicate);
			for (int i = 0; i < relation.size(); i++) {
				ITuple tuple = relation.get(i);
				logger.debug("Added [" + timestamp + "]: " + predicate + " "
						+ tuple);
			}
		}
	}

	@Override
	public IRelation execute(IQuery query)
			throws ProgramNotStratifiedException, RuleUnsafeException,
			EvaluationException {
		return execute(query, null);
	}

	@Override
	public IRelation execute(IQuery query, List<IVariable> variableBindings)
			throws EvaluationException {
		if (query == null)
			throw new IllegalArgumentException(
					"KnowledgeBase.execute() - the query is null.");

		// This prevents every strategy having to check for this.
		if (variableBindings == null)
			variableBindings = new ArrayList<IVariable>();

		synchronized (mFacts) {
			IRelation result = mEvaluationStrategy.evaluateQuery(
					RuleManipulator.removeDuplicateLiterals(query),
					variableBindings);

			return result;
		}
	}

	@Override
	public void execute() {
		try {
			ArrayList<IVariable> variableBindings = new ArrayList<IVariable>();
			synchronized (mFacts) {
				for (IQuery query : mQueries) {
					IRelation result = mEvaluationStrategy.evaluateQuery(
							RuleManipulator.removeDuplicateLiterals(query),
							variableBindings);

					// format the results.
					String results = ResultFormatter.format(query,
							variableBindings, result);

					// send results to listeners.
					sendResults(results);
				}
			}
		} catch (Exception e) {
			logger.error("Evaluation error occured: {}", e.getMessage());
		}
	}

	@Override
	public void registerQuery(IQuery query, String host, int port)
			throws EvaluationException {
		synchronized (mQueries) {
			mQueries.add(query);
		}

		synchronized (mIrisOutputStreamers) {
			synchronized (mListenerQueryMap) {
				HostPortPair pair = new HostPortPair(host, port);
				if (!mIrisOutputStreamers.containsKey(pair)) {
					addListener(host, port);
					ArrayList<IQuery> queryList = new ArrayList<IQuery>();
					queryList.add(query);
					mListenerQueryMap.put(pair, queryList);
				} else {
					mListenerQueryMap.get(pair).add(query);
				}
			}
		}

		logger.info("Query registered: {}", query);
	}

	@Override
	public void deregisterQuery(IQuery query) {
		List<HostPortPair> remove = new ArrayList<HostPortPair>();
		synchronized (mQueries) {
			synchronized (mIrisOutputStreamers) {
				synchronized (mListenerQueryMap) {

					if (mQueries.contains(query)) {
						mQueries.remove(query);
						for (Entry<HostPortPair, List<IQuery>> entry : mListenerQueryMap
								.entrySet()) {
							if (entry.getValue().contains(query)) {
								entry.getValue().remove(query);
								if (entry.getValue().size() == 0) {
									remove.add(entry.getKey());
								}
							}
						}

						for (HostPortPair entry : remove) {
							mIrisOutputStreamers.get(entry).shutdown();
							mIrisOutputStreamers.remove(entry);
						}
						logger.info("Query deregistered: {}", query);
					} else {
						logger.info("Query does not exist!");
					}
				}
			}
		}
	}

	/**
	 * This method writes the result of a query to the output sockets.
	 * 
	 * @param results
	 *            The results of the query.
	 */
	private void sendResults(String results) {
		for (IIrisOutputStreamer streamer : mIrisOutputStreamers.values()) {
			streamer.stream(results);
		}
	}

	/**
	 * Returns all rules of this Knowledge Base.
	 */
	public List<IRule> getRules() {
		return mRules;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (IRule rule : mRules)
			result.append(rule.toString());

		synchronized (mFacts) {
			result.append(mFacts.toString());
		}

		return result.toString();
	}

	private void addListener(String host, int port) {
		// Start the knowledge base output thread
		IIrisOutputStreamer irisOutputStreamer = new IrisOutputStreamer(host,
				port);
		irisOutputStreamer.connect();
		mIrisOutputStreamers.put(new HostPortPair(host, port),
				irisOutputStreamer);
		logger.info("Added listener [{}, {}]", host, port);
	}

	// @Override
	// public void deleteListener(String host, int port) {
	// HostPortPair pair = new HostPortPair(host, port);
	// for (HostPortPair entry : mIrisOutputStreamers.keySet()) {
	// if (pair.equals(entry)) {
	// mIrisOutputStreamers.get(entry).shutdown();
	// logger.info("Deleted listener [{}, {}]", host, port);
	// break;
	// }
	// }
	// }

	@Override
	public void cleanKnowledgeBase() {
		synchronized (mFacts) {
			long currentTime = System.currentTimeMillis();

			mFacts.clean(currentTime);

			try {
				// FIXME Norbert: does only work with
				// StratifiedBottomUpEvaluationStrategy
				mEvaluationStrategy.evaluateRules(mFacts, currentTime);
			} catch (EvaluationException e) {
				logger.error("Evaluation exception occured: {}", e.getMessage());
			}

			// TODO Norbert: logging
			// logger.debug("Current knowledge-base [{}]:", currentTime);
			// logger.debug("----------------------------");
			// Set<IPredicate> predicates = mFacts.getPredicates();
			// for (IPredicate predicate : predicates) {
			// IRelation relation = mFacts.get(predicate);
			// for (int i = 0; i < relation.size(); i++) {
			// ITuple tuple = relation.get(i);
			// logger.debug("[" + relation.getTimestamp(tuple) + "]: "
			// + predicate + " " + tuple);
			// }
			//
			// }
		}
	}
}
