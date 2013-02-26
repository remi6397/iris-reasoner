package at.sti2.streamingiris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.streamingiris.api.IKnowledgeBase;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.evaluation.IEvaluationStrategy;
import at.sti2.streamingiris.evaluation.OptimisedProgramStrategyAdaptor;
import at.sti2.streamingiris.facts.Facts;
import at.sti2.streamingiris.facts.FactsWithExternalData;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.rules.RuleManipulator;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.threads.KnowledgeBaseServer;

/**
 * The concrete knowledge-base.
 */
public class KnowledgeBase implements IKnowledgeBase {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** The facts of the knowledge-base. */
	private final IFacts facts;

	/** The rules of the knowledge-base. */
	private final List<IRule> rules;

	/** The configuration object for the knowledge-base. */
	private final Configuration configuration;

	/** The evaluation strategy for the knowledge-base. */
	private IEvaluationStrategy evaluationStrategy;

	/** The sockets to output the results of the executed queries. */
	private Map<String, IIrisOutputStreamer> irisOutputStreamers;

	/**
	 * The map containing the information which query is registered for which
	 * listeners.
	 */
	private Map<IQuery, List<String>> queryListenerMap;

	/** The thread that handles incoming facts. */
	private KnowledgeBaseServer inputServerThread;

	/** Buffers the input until the evaluation is finished. */
	private InputBuffer inputBuffer;

	/**
	 * Constructor.
	 * 
	 * @param facts
	 *            The starting facts for the knowledge-base.
	 * @param ruleList
	 *            The rules of the knowledge-base.
	 * @param config
	 *            The configuration object for the knowledge-base.
	 * @throws EvaluationException
	 * @throws EvaluationException
	 */
	public KnowledgeBase(Map<IPredicate, IRelation> inputFacts,
			List<IRule> ruleList, Configuration config)
			throws EvaluationException {
		irisOutputStreamers = new HashMap<String, IIrisOutputStreamer>();
		queryListenerMap = new HashMap<IQuery, List<String>>();

		if (inputFacts == null)
			inputFacts = new HashMap<IPredicate, IRelation>();

		if (ruleList == null)
			ruleList = new ArrayList<IRule>();

		if (config == null)
			config = new Configuration();

		configuration = config;

		// Store the configuration object against the current thread.
		ConfigurationThreadLocalStorage.setConfiguration(configuration);

		// Set up the rule-base
		rules = ruleList;

		// Set up the facts object(s)
		IFacts newFacts = new Facts(inputFacts, configuration.relationFactory);

		if (configuration.externalDataSources.size() > 0)
			newFacts = new FactsWithExternalData(newFacts,
					configuration.externalDataSources);

		facts = newFacts;

		if (logger.isDebugEnabled()) {
			logger.debug("IRIS knowledge-base init");
			logger.debug("========================");

			for (IRule rule : ruleList) {
				logger.debug(rule.toString());
			}

			logger.debug("------------------------");

			for (IPredicate f : facts.getPredicates()) {
				IRelation relation = facts.get(f);
				for (int i = 0; i < relation.size(); i++) {
					ITuple tuple = relation.get(i);
					logger.debug("{} {}", f, tuple);
				}
			}

			logger.debug("------------------------");

		}

		// Store the configuration object against the current thread.
		ConfigurationThreadLocalStorage.setConfiguration(configuration);

		// initialize the evaluation strategy with the updated facts.
		if (configuration.programOptmimisers.size() > 0)
			evaluationStrategy = new OptimisedProgramStrategyAdaptor(facts,
					rules, configuration);
		else
			evaluationStrategy = configuration.evaluationStrategyFactory
					.createEvaluator(facts, rules, configuration);

		evaluationStrategy.evaluateRules(facts, 0);

		inputBuffer = new InputBuffer(this);

		inputServerThread = new KnowledgeBaseServer(inputBuffer,
				configuration.inputPort);
		inputServerThread.start();
	}

	@Override
	public void shutdown() {
		try {
			logger.info("Knowledge-Base shutting down ...");

			// Shut down the input streamer.
			if (!inputServerThread.shutdown())
				logger.error("InputStream could not be shut down!");

			inputBuffer.shutdown();

			// Shut down the output streamer.
			for (IIrisOutputStreamer streamer : irisOutputStreamers.values()) {
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
		inputBuffer.setKbWorking();
		try {
			long timestamp;
			synchronized (facts) {
				long currentTimeMillis = System.currentTimeMillis();
				// logger.info("Current time: {}", currentTimeMillis);
				timestamp = currentTimeMillis
						+ configuration.timeWindowMilliseconds;
				// logger.info("Timestamp: {}", timestamp);

				facts.clean(currentTimeMillis);

				facts.addFacts(newFacts, timestamp);

				// FIXME Norbert: does only work with
				// StratifiedBottomUpEvaluationStrategy
				evaluationStrategy.evaluateRules(facts, -1);
			}

			execute();

		} catch (EvaluationException e) {
			throw e;
		}

		inputBuffer.setKbReady();

		// Set<IPredicate> predicates = newFacts.keySet();
		// for (IPredicate predicate : predicates) {
		// IRelation relation = newFacts.get(predicate);
		// for (int i = 0; i < relation.size(); i++) {
		// ITuple tuple = relation.get(i);
		// logger.info("ADDED [" + timestamp + "]: " + predicate + " "
		// + tuple);
		// }
		// }
	}

	public synchronized void addMultipleFacts(
			Map<Long, Map<IPredicate, IRelation>> newFacts)
			throws EvaluationException {
		inputBuffer.setKbWorking();
		long timestamp;
		synchronized (facts) {
			for (Entry<Long, Map<IPredicate, IRelation>> entry : newFacts
					.entrySet()) {
				long time = entry.getKey();
				timestamp = time + configuration.timeWindowMilliseconds;
				facts.addFacts(entry.getValue(), timestamp);
			}

			facts.clean(System.currentTimeMillis());

			try {
				// FIXME Norbert: does only work with
				// StratifiedBottomUpEvaluationStrategy
				evaluationStrategy.evaluateRules(facts, -1);
			} catch (EvaluationException e) {
				throw e;
			}
		}

		execute();

		inputBuffer.setKbReady();
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

		synchronized (facts) {
			IRelation result = evaluationStrategy.evaluateQuery(
					RuleManipulator.removeDuplicateLiterals(query),
					variableBindings);

			return result;
		}
	}

	@Override
	public void execute() {
		try {
			ArrayList<IVariable> variableBindings;
			synchronized (facts) {
				for (IQuery query : queryListenerMap.keySet()) {
					variableBindings = new ArrayList<IVariable>();
					IRelation result = evaluationStrategy.evaluateQuery(
							RuleManipulator.removeDuplicateLiterals(query),
							variableBindings);

					// format the results.
					String results = ResultFormatter.format(query,
							variableBindings, result);

					// send results to listeners.
					sendResults(query, results);
				}
			}
		} catch (Exception e) {
			logger.error("Evaluation error occured: {}", e.toString());
		}
	}

	@Override
	public void registerQueryListener(IQuery query, String host, int port)
			throws EvaluationException {
		String hostPortString = host + ":" + port;

		synchronized (irisOutputStreamers) {
			synchronized (queryListenerMap) {
				if (!irisOutputStreamers.containsKey(hostPortString)) {
					IIrisOutputStreamer outputStreamer = createListener(host,
							port);
					irisOutputStreamers.put(hostPortString, outputStreamer);
				}

				if (queryListenerMap.containsKey(query)) {
					queryListenerMap.get(query).add(hostPortString);
				} else {
					ArrayList<String> hostPortPairList = new ArrayList<String>();
					hostPortPairList.add(hostPortString);
					queryListenerMap.put(query, hostPortPairList);
				}
			}
		}

		logger.info("Query registered: " + query + " [" + host + ", " + port
				+ "]");
	}

	@Override
	public void deregisterQueryListener(IQuery query, String host, int port) {
		String hostPortString = host + ":" + port;

		synchronized (irisOutputStreamers) {
			if (queryListenerMap.containsKey(query)) {
				List<String> list = queryListenerMap.get(query);
				if (list.contains(hostPortString)) {
					list.remove(hostPortString);
				}
				if (list.size() == 0) {
					queryListenerMap.remove(query);
				}
				logger.info("Query deregistered: {}", query);
			} else {
				logger.info("Query does not exist!");
			}
		}
	}

	/**
	 * This method writes the result of a query to the output sockets.
	 * 
	 * @param query
	 *            The query that has been executed.
	 * @param results
	 *            The results of the query.
	 */
	private void sendResults(IQuery query, String results) {
		List<String> list = queryListenerMap.get(query);

		for (String pair : list) {
			irisOutputStreamers.get(pair).stream(results);
		}
	}

	/**
	 * Returns all rules of this Knowledge Base.
	 */
	public List<IRule> getRules() {
		return rules;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (IRule rule : rules)
			result.append(rule.toString());

		synchronized (facts) {
			result.append(facts.toString());
		}

		return result.toString();
	}

	private IIrisOutputStreamer createListener(String host, int port) {
		// Start the knowledge base output thread
		IIrisOutputStreamer irisOutputStreamer = new IrisOutputStreamer(host,
				port);
		logger.info("Added listener [{}, {}]", host, port);
		return irisOutputStreamer;
	}

	@Override
	public void cleanKnowledgeBase() {
		synchronized (facts) {
			long currentTime = System.currentTimeMillis();
			// logger.info("Current time: {}", currentTime);

			facts.clean(currentTime);

			try {
				// FIXME Norbert: does only work with
				// StratifiedBottomUpEvaluationStrategy
				// using -1 so inferred facts get deleted before next execution
				evaluationStrategy.evaluateRules(facts, -1);
			} catch (EvaluationException e) {
				logger.error("Evaluation exception occured: {}", e.getMessage());
			}

			// logger.info("Current knowledge-base [{}]:", currentTime);
			// logger.info("----------------------------");
			// Set<IPredicate> predicates = facts.getPredicates();
			// for (IPredicate predicate : predicates) {
			// IRelation relation = facts.get(predicate);
			// for (int i = 0; i < relation.size(); i++) {
			// ITuple tuple = relation.get(i);
			// logger.info("[" + relation.getTimestamp(tuple) + "]: "
			// + predicate + " " + tuple);
			// }
			//
			// }
			// logger.info("============================");
		}
	}
}
