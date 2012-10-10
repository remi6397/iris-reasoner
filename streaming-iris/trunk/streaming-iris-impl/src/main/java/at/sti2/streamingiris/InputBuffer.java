package at.sti2.streamingiris;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.storage.IRelation;

public class InputBuffer {

	// private Logger logger = LoggerFactory.getLogger(getClass());

	private KnowledgeBase knowledgeBase;
	private Map<Long, Map<IPredicate, IRelation>> factMap;
	private boolean kbReady;

	private ExecutorService executor;

	// TODO profiling
	// private final Map<Long, UUID> tmpMap = new HashMap<Long, UUID>();

	public InputBuffer(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
		this.factMap = new HashMap<Long, Map<IPredicate, IRelation>>();
		this.kbReady = true;
		this.executor = Executors.newSingleThreadExecutor();
	}

	public void addFacts(final Map<IPredicate, IRelation> facts) {
		long currentTimeMillis = System.currentTimeMillis();

		// TODO profiling
		// final UUID uuid = UUID.randomUUID();
		// tmpMap.put(new Long(currentTimeMillis), uuid);
		// Profiler.addStartTime(uuid, currentTimeMillis);

		if (kbReady) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						knowledgeBase.addFacts(facts);

						// TODO profiling
						// long currentTime = System.currentTimeMillis();
						// Profiler.addEndTime(uuid, currentTime);

					} catch (EvaluationException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			synchronized (factMap) {
				factMap.put(new Long(currentTimeMillis), facts);
			}
		}
	}

	public synchronized void setKbReady() {
		synchronized (factMap) {
			this.kbReady = true;

			if (!factMap.isEmpty()) {
				sendFactsToKnowledgeBase();
			}
		}
	}

	public void setKbWorking() {
		this.kbReady = false;
	}

	private void sendFactsToKnowledgeBase() {
		final Map<Long, Map<IPredicate, IRelation>> facts = new HashMap<Long, Map<IPredicate, IRelation>>();

		for (Entry<Long, Map<IPredicate, IRelation>> entry : factMap.entrySet()) {
			facts.put(entry.getKey(), entry.getValue());
		}

		factMap.clear();

		// try {
		// knowledgeBase.addMultipleFacts(facts);
		// } catch (EvaluationException e) {
		// e.printStackTrace();
		// }

		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					knowledgeBase.addMultipleFacts(facts);

					// TODO profiling
					// long currentTime = System.currentTimeMillis();
					// for (Entry<Long, Map<IPredicate, IRelation>> entry :
					// facts
					// .entrySet()) {
					// Profiler.addEndTime(tmpMap.get(entry.getKey()),
					// currentTime);
					// }
				} catch (EvaluationException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void shutdown() {
		executor.shutdownNow();
	}
}
